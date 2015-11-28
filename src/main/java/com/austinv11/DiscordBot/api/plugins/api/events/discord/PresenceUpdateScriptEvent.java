package com.austinv11.DiscordBot.api.plugins.api.events.discord;

import com.austinv11.DiscordBot.api.plugins.api.IScriptEvent;
import com.austinv11.DiscordBot.api.plugins.api.discord.ScriptGuild;
import com.austinv11.DiscordBot.api.plugins.api.discord.ScriptUser;
import com.austinv11.DiscordBot.api.plugins.api.discord.UserPresences;
import sx.blah.discord.handle.impl.events.PresenceUpdateEvent;

/**
 * This event is posted when a user's presence is updated
 */
public class PresenceUpdateScriptEvent implements IScriptEvent {
	
	private PresenceUpdateEvent javaEvent;
	
	public PresenceUpdateScriptEvent(PresenceUpdateEvent event) {
		this.javaEvent = event;
	}
	
	/**
	 * Gets the user's original presence
	 * @return The presence, corresponding to {@link UserPresences}
	 */
	public int getOldPresence() {
		return UserPresences.scriptPresenceFromJavaPresence(javaEvent.getOldPresence());
	}
	
	/**
	 * Gets the user's new presence
	 * @return The presence, corresponding to {@link UserPresences}
	 */
	public int getNewPresence() {
		return UserPresences.scriptPresenceFromJavaPresence(javaEvent.getNewPresence());
	}
	
	/**
	 * Gets the user whose presence changed
	 * @return The user
	 */
	public ScriptUser getUser() {
		return new ScriptUser(javaEvent.getUser());
	}
	
	/**
	 * Gets the guild the user's presence was changed in
	 * @return THe guild
	 */
	public ScriptGuild getGuild() {
		return new ScriptGuild(javaEvent.getGuild());
	}
}
