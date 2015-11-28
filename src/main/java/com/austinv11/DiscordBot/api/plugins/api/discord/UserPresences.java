package com.austinv11.DiscordBot.api.plugins.api.discord;

import sx.blah.discord.util.Presences;

/**
 * Represents a user's presence in a guild/channel
 */
public class UserPresences {
	
	/**
	 * Represents the user being offline
	 */
	public int OFFLINE = -1;
	/**
	 * Represents the user being idle
	 */
	public int IDLE = 0;
	/**
	 * Represents the user being online
	 */
	public int ONLINE = 1;
	
	protected static int scriptPresenceFromJavaPresence(Presences presence) {
		UserPresences presences = new UserPresences();
		switch (presence) {
			case OFFLINE:
				return presences.OFFLINE;
			
			case IDLE:
				return presences.IDLE;
			
			case ONLINE:
			default:
				return presences.ONLINE;
		}
	}
}
