package com.austinv11.DiscordBot.api.plugins.api.events;

import com.austinv11.DiscordBot.api.plugins.PluginManifest;
import com.austinv11.DiscordBot.api.plugins.api.IScriptEvent;

/**
 * This event is posted once a plugin is loaded
 */
public class InitEvent implements IScriptEvent {
	
	private PluginManifest manifest;
	
	public InitEvent(PluginManifest manifest) {
		this.manifest = manifest;
	}
	
	/**
	 * Gets the plugin manifest for the plugin that was initialized
	 * @return The manifest
	 */
	public PluginManifest getManifest() {
		return manifest;
	}
}
