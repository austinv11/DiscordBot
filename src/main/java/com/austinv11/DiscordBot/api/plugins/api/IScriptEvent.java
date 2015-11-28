package com.austinv11.DiscordBot.api.plugins.api;

import com.austinv11.DiscordBot.DiscordBot;
import com.austinv11.DiscordBot.api.plugins.Plugin;
import com.austinv11.DiscordBot.api.plugins.PluginManifest;
import com.austinv11.DiscordBot.handler.Logger;

import javax.script.ScriptEngine;
import javax.script.ScriptException;

/**
 * Marker interface for events to be passed onto plugins
 */
public interface IScriptEvent {
	
	/**
	 * Gets the name which represents the event
	 * @return The name
	 */
	default String getName() {
		return this.getClass().getSimpleName();
	}
	
	/**
	 * Propagates the event to all the proper event handlers
	 */
	default void propagate() {
		for (Plugin plugin : DiscordBot.plugins)
			for (PluginManifest.EventHandler handler : plugin.manifest.event_handlers)
				if (handler.event_filter.equals(getName())) {
					try {
						ScriptEngine engine = DiscordBot.getScriptEngineForLang(handler.script.split("\\.")[1]);
						engine.put("CONTEXT", new EventContext(this, plugin.manifest));
						engine.eval(plugin.eventHandlers.get(getName()));
					} catch (ScriptException e) {
						Logger.log(e);
					}
				}
	}
}
