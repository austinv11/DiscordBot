package com.austinv11.DiscordBot.api.plugins.api.events.discord;

import com.austinv11.DiscordBot.api.plugins.api.IScriptEvent;
import com.austinv11.DiscordBot.api.plugins.api.discord.ScriptGuild;
import com.austinv11.DiscordBot.api.plugins.api.discord.ScriptUser;
import com.austinv11.DiscordBot.api.plugins.api.util.Time;
import sx.blah.discord.handle.impl.events.UserJoinEvent;

/**
 * The event is posted when a user joins a channel
 */
public class UserJoinScriptEvent implements IScriptEvent {
	
	private UserJoinEvent javaEvent;
	
	public UserJoinScriptEvent(UserJoinEvent event) {
		this.javaEvent = event;
	}
	
	/**
	 * Gets the time the user joined
	 * @return The join time
	 */
	public Time getJoinTime() {
		return new Time(javaEvent.getJoinTime());
	}
	
	/**
	 * Gets the user who joined
	 * @return The user
	 */
	public ScriptUser getUser() {
		return new ScriptUser(javaEvent.getUser());
	}
	
	/**
	 * Gets the guild the user joined
	 * @return The guild
	 */
	public ScriptGuild getGuild() {
		return new ScriptGuild(javaEvent.getGuild());
	}
}
