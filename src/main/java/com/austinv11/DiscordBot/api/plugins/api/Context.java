package com.austinv11.DiscordBot.api.plugins.api;

import com.austinv11.DiscordBot.api.plugins.Plugin;

/**
 * The base class all context-objects derive from
 */
public class Context {
	
	/**
	 * The object representing the plugin itself
	 */
	public Plugin plugin;
	
	public Context(Plugin plugin) {
		this.plugin = plugin;
	}
	
	protected Context() {}
}
