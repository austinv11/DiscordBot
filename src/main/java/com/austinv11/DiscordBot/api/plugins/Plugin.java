package com.austinv11.DiscordBot.api.plugins;

import com.austinv11.DiscordBot.DiscordBot;
import com.austinv11.DiscordBot.api.CommandRegistry;
import com.austinv11.DiscordBot.api.commands.CommandSyntaxException;
import com.austinv11.DiscordBot.api.commands.ICommand;
import com.austinv11.DiscordBot.api.plugins.api.CommandContext;
import com.austinv11.DiscordBot.api.plugins.api.Context;
import com.austinv11.DiscordBot.handler.Logger;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import sx.blah.discord.handle.obj.Channel;
import sx.blah.discord.handle.obj.Message;
import sx.blah.discord.handle.obj.User;

import javax.script.ScriptEngine;
import javax.script.ScriptException;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Optional;
import java.util.zip.ZipFile;

/**
 * An object representing a plugin
 */
public class Plugin {
	
	/**
	 * The manifest for the plugin
	 */
	public PluginManifest manifest;
	
	/**
	 * A map holding cached event handlers, this is done to save time instead of dealing with file I/O
	 */
	public HashMap<String, String> eventHandlers = new HashMap<>();
	
	/**
	 * The file the plugin is contained in
	 */
	public File pluginFile;
	
	public Plugin(File pluginFile) throws IOException {
		this.pluginFile = pluginFile;
		Gson gson = new GsonBuilder().serializeNulls().create();
		ZipFile zipFile = new ZipFile(pluginFile);
		InputStreamReader reader = new InputStreamReader(zipFile.getInputStream(zipFile.getEntry("PLUGIN_MANIFEST.json")));
		manifest = gson.fromJson(reader, PluginManifest.class);
		try {
			insertProperInfo(manifest);
			for (PluginManifest.Command command : manifest.commands)
				insertProperInfo(command);
			for (PluginManifest.EventHandler handler : manifest.event_handlers)
				insertProperInfo(handler);
		} catch (IllegalAccessException e) {
			Logger.log(e);
		}
		
		if (manifest.commands != null && manifest.commands.length > 0) {
			for (PluginManifest.Command command : manifest.commands) {
				String contents = readContents(new BufferedReader(new InputStreamReader(zipFile.getInputStream(
						zipFile.getEntry(command.executor)))));
				Plugin plugin = this;
				CommandRegistry.registerCommand(new ICommand() {
					@Override
					public String getCommand() {
						return command.name;
					}
					
					@Override
					public String[] getAliases() {
						return command.aliases;
					}
					
					@Override
					public boolean removesCommandMessage() {
						return command.removes_command_message;
					}
					
					@Override
					public String getUsage() {
						return command.usage;
					}
					
					@Override
					public String getHelpMessage() {
						return command.help_message;
					}
					
					@Override
					public int getDefaultPermissionLevel() {
						return Integer.parseInt(command.permission_level);
					}
					
					@Override
					public Optional<String> executeCommand(String parameters, User executor, Channel channel, Message commandMessage) throws CommandSyntaxException {
						try {
							ScriptEngine engine = DiscordBot.getScriptEngineForLang(command.executor.split("\\.")[1]);
							engine.put("CONTEXT", new CommandContext(parameters, executor, channel, commandMessage, plugin.manifest));
							return Optional.of(String.valueOf(engine.eval(contents)));
						} catch (ScriptException e) {
							Logger.log(e);
							return Optional.of("Script error for plugin '"+manifest.plugin_id+"', see the bot's log for details");
						}
					}
				});
			}
		}
		
		if (manifest.event_handlers != null && manifest.event_handlers.length > 0) {
			for (PluginManifest.EventHandler handler : manifest.event_handlers) {
				String contents = readContents(new BufferedReader(new InputStreamReader(zipFile.getInputStream(
						zipFile.getEntry(handler.script)))));
				eventHandlers.put(handler.event_filter, contents);
			}
		}
	}
	
	private void insertProperInfo(Object toInsertInto) throws IllegalAccessException {
		Field[] fields = toInsertInto.getClass().getFields();
		for (Field field : fields) {
			field.setAccessible(true);
			if (String.class.isAssignableFrom(field.getType())) {
				String value = String.valueOf(field.get(toInsertInto));
				
				while (containsSurroundingChars(value, "%")) {
					int beginningIndex = value.indexOf('%');
					int endingIndex = value.replaceFirst("%", "").indexOf('%');
					String snippet = value.substring(beginningIndex+1, endingIndex+1);
					try {
						ScriptEngine engine = DiscordBot.getScriptEngineForLang("js");
						engine.put("CONTEXT", new Context(manifest));
						String eval = String.valueOf(engine.eval(snippet));
						value = value.replace("%"+snippet+"%", eval);
					} catch (ScriptException e) {
						Logger.log(e);
						value = value.replaceAll("%"+snippet+"%", "null");
						break;
					}
				}
				
				field.set(toInsertInto, value);
			}
		}
	}
	
	private boolean containsSurroundingChars(String string, String surrounding) {
		return string.contains(surrounding) && string.replaceFirst(surrounding, "").contains(surrounding);
	}
	
	private String readContents(BufferedReader reader) throws IOException {
		String contents = "";
		String next;
		while ((next = reader.readLine()) != null) {
			contents += next+"\n";
		}
		return contents.trim();
	}
}
