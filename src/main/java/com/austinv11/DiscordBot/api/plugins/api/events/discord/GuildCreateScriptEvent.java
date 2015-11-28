package com.austinv11.DiscordBot.api.plugins.api.events.discord;

import com.austinv11.DiscordBot.api.plugins.api.IScriptEvent;
import com.austinv11.DiscordBot.api.plugins.api.discord.ScriptGuild;
import sx.blah.discord.handle.impl.events.GuildCreateEvent;

/**
 * This event is posted when a guild is created
 */
public class GuildCreateScriptEvent implements IScriptEvent {
	
	private GuildCreateEvent javaEvent;
	
	public GuildCreateScriptEvent(GuildCreateEvent event) {
		this.javaEvent = event;
	}
	
	/**
	 * Gets the guild created
	 * @return The guild
	 */
	public ScriptGuild getGuild() {
		return new ScriptGuild(javaEvent.getGuild());
	}
}
