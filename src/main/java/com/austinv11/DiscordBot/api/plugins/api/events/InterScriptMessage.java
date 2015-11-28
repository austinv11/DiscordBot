package com.austinv11.DiscordBot.api.plugins.api.events;

import com.austinv11.DiscordBot.DiscordBot;
import com.austinv11.DiscordBot.api.plugins.Plugin;
import com.austinv11.DiscordBot.api.plugins.PluginManifest;
import com.austinv11.DiscordBot.api.plugins.api.EventContext;
import com.austinv11.DiscordBot.api.plugins.api.IScriptEvent;
import com.austinv11.DiscordBot.api.plugins.api.util.ISM;
import com.austinv11.DiscordBot.handler.Logger;

import javax.script.ScriptEngine;
import javax.script.ScriptException;

/**
 * This event is posted when a script is sent a message from {@link ISM#sendMessage(String, Object)}
 */
public class InterScriptMessage implements IScriptEvent {
	
	private String target;
	
	/**
	 * The message sent
	 */
	public Object message;
	
	public InterScriptMessage(String target, Object message) {
		this.target = target;
		this.message = message;
	}
	
	@Override
	public void propagate() {
		for (Plugin plugin : DiscordBot.plugins) {
			for (PluginManifest.EventHandler handler : plugin.manifest.event_handlers) {
				if (handler.script.equals(target)) {
					try {
						ScriptEngine engine = DiscordBot.getScriptEngineForLang(handler.script.split("\\.")[1]);
						engine.put("CONTEXT", new EventContext(this, plugin.manifest));
						engine.eval(plugin.eventHandlers.get(handler.event_filter));
						return;
					} catch (ScriptException e) {
						Logger.log(e);
					}
				}
			}
			for (PluginManifest.Command command : plugin.manifest.commands) {
				if (command.executor.equals(target)) {
					try {
						ScriptEngine engine = DiscordBot.getScriptEngineForLang(command.executor.split("\\.")[1]);
						engine.put("CONTEXT", new EventContext(this, plugin.manifest));
						engine.eval(plugin.commandHandlers.get(command.name));
						return;
					} catch (ScriptException e) {
						Logger.log(e);
					}
				}
			}
		}
	}
}
