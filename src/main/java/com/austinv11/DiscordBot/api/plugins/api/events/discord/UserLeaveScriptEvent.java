package com.austinv11.DiscordBot.api.plugins.api.events.discord;

import com.austinv11.DiscordBot.api.plugins.api.IScriptEvent;
import com.austinv11.DiscordBot.api.plugins.api.discord.ScriptGuild;
import com.austinv11.DiscordBot.api.plugins.api.discord.ScriptUser;
import sx.blah.discord.handle.impl.events.UserLeaveEvent;

/**
 * This event is posted when a user leaves
 */
public class UserLeaveScriptEvent implements IScriptEvent {
	
	private UserLeaveEvent javaEvent;
	
	public UserLeaveScriptEvent(UserLeaveEvent event) {
		this.javaEvent = event;
	}
	
	/**
	 * Gets the user that left
	 * @return The user
	 */
	public ScriptUser getUser() {
		return new ScriptUser(javaEvent.getUser());
	}
	
	/**
	 * Gets the guild the user left
	 * @return The guild
	 */
	public ScriptGuild getGuild() {
		return new ScriptGuild(javaEvent.getGuild());
	}
}
