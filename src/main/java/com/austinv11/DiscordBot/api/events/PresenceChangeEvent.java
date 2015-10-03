package com.austinv11.DiscordBot.api.events;

import sx.blah.discord.obj.User;

@DiscordEvent
public class PresenceChangeEvent {
	
	/**
	 * The user involved
	 */
	public User user;
	/**
	 * The new presence
	 */
	public String presence;
	
	public PresenceChangeEvent(User user, String presence) {
		this.user = user;
		this.presence = presence;
	}
}
