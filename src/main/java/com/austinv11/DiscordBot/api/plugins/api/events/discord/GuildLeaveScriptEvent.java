package com.austinv11.DiscordBot.api.plugins.api.events.discord;

import com.austinv11.DiscordBot.api.plugins.api.IScriptEvent;
import com.austinv11.DiscordBot.api.plugins.api.discord.ScriptGuild;
import sx.blah.discord.handle.impl.events.GuildLeaveEvent;

/**
 * This event is posted when a guild is left
 */
public class GuildLeaveScriptEvent implements IScriptEvent {
	
	private GuildLeaveEvent javaEvent;
	
	public GuildLeaveScriptEvent(GuildLeaveEvent event) {
		this.javaEvent = event;
	}
	
	/**
	 * Gets the guild that was left
	 * @return The guild
	 */
	public ScriptGuild getGuild() {
		return new ScriptGuild(javaEvent.getGuild());
	}
}
