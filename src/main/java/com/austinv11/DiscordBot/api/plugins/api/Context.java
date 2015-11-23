package com.austinv11.DiscordBot.api.plugins.api;

import com.austinv11.DiscordBot.api.plugins.PluginManifest;

/**
 * The base class all context-objects derive from
 */
public class Context {
	
	/**
	 * The object representing the plugin itself
	 */
	public PluginManifest plugin;
	
	public Context(PluginManifest plugin) {
		this.plugin = plugin;
	}
	
	protected Context() {}
}
