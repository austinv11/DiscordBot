package com.austinv11.DiscordBot.commands;

import com.austinv11.DiscordBot.DiscordBot;
import com.austinv11.DiscordBot.api.commands.CommandSyntaxException;
import com.austinv11.DiscordBot.api.commands.ICommand;
import com.austinv11.DiscordBot.api.plugins.Plugin;
import com.austinv11.DiscordBot.api.plugins.PluginManifest;
import sx.blah.discord.handle.obj.Channel;
import sx.blah.discord.handle.obj.Message;
import sx.blah.discord.handle.obj.User;

import java.util.Optional;

public class PluginsCommand implements ICommand {
	
	@Override
	public String getCommand() {
		return "plugins";
	}
	
	@Override
	public String[] getAliases() {
		return new String[]{"plugin", "mods", "mod"};
	}
	
	@Override
	public boolean removesCommandMessage() {
		return false;
	}
	
	@Override
	public String getUsage() {
		return getCommand()+" [optional: plugin name]";
	}
	
	@Override
	public String getHelpMessage() {
		return "This command lists all the loaded plugins or it lists the details regarding a specific plugin if a name is provided";
	}

	@Override
	public boolean isConsoleExecutionAllowed() {
		return true;
	}
	
	@Override
	public int getDefaultPermissionLevel() {
		return ICommand.DEFAULT;
	}
	
	@Override
	public Optional<String> executeCommand(String parameters, User executor, Channel channel, Message commandMessage) throws CommandSyntaxException {
		String returnVal = "";
		if (parameters.isEmpty()) {
			returnVal = "There are "+DiscordBot.plugins.length+" plugin(s) loaded";
			for (Plugin plugin : DiscordBot.plugins)
				returnVal += "\n* "+plugin.manifest.plugin_id;
		} else {
			for (Plugin plugin : DiscordBot.plugins)
				if (plugin.manifest.plugin_id.equalsIgnoreCase(parameters)) {
					returnVal = "Details for plugin '"+plugin.manifest.plugin_id+"':\n";
					returnVal += "Description: "+plugin.manifest.description+"\n";
					returnVal += "Version: "+plugin.manifest.version+"\n";
					returnVal += "Author(s): "+plugin.manifest.author+"\n";
					returnVal += "Plugin file: "+plugin.pluginFile.getName();
					if (plugin.manifest.commands.length > 0) {
						returnVal += "\n"+plugin.manifest.commands.length+" command(s) added by this plugin:";
						for (PluginManifest.Command command : plugin.manifest.commands)
							returnVal += "\n* "+command.name;
					}
					return Optional.of(returnVal);
				}
			throw new CommandSyntaxException("Plugin '"+parameters+"' does not exist!");
		}
		return Optional.of(returnVal);
	}
}
