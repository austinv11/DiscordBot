package com.austinv11.DiscordBot.api.plugins.api.discord;

import com.austinv11.DiscordBot.DiscordBot;
import sx.blah.discord.handle.obj.User;

/**
 * This object represents a user. This is an intermediary between Discord4J and a script in order to prevent Discord4J
 * breaking stuff in the future.
 */
public class ScriptUser {
	
	protected User javaUser;
	
	public ScriptUser(User user) {
		this.javaUser = user;
	}
	
	/**
	 * Gets the user's id
	 * @return The user id
	 */
	public String getID() {
		return javaUser.getID();
	}
	
	/**
	 * Gets the user's name
	 * @return The user name
	 */
	public String getName() {
		return javaUser.getName();
	}
	
	/**
	 * Gets the url leading to the user's avatar image
	 * @return The avatar url
	 */
	public String getAvatarURL() {
		return javaUser.getAvatarURL();
	}
	
	/**
	 * Gets the user's presence. Corresponds to {@link UserPresences}
	 * @return The user's presence
	 */
	public int getPresence() {
		return UserPresences.scriptPresenceFromJavaPresence(javaUser.getPresence());
	}
	
	/**
	 * Formats the user's name into a string in a way so that you can @mention the user in a message
	 * @return The @metion formatted string
	 */
	public String mention() {
		return javaUser.mention();
	}
	
	/**
	 * Gets the game the user is currently playing
	 * @return The name of the game, or null if the user isn't currently playing a game
	 */
	public String getGame() {
		return DiscordBot.instance.getGameByID(javaUser.getGameID().orElse(null)).orElse(null);
	}
}
