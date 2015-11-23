package com.austinv11.DiscordBot.api.plugins.api;

import com.austinv11.DiscordBot.api.plugins.PluginManifest;

/**
 * An object representing the context for an event
 */
public class EventContext extends Context {
	
	/**
	 * The event
	 */
	public IScriptEvent event;
	
	public EventContext(IScriptEvent event, PluginManifest plugin) {
		this.event = event;
		this.plugin = plugin;
	}
}
