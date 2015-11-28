package com.austinv11.DiscordBot.api.plugins.api.discord;

import sx.blah.discord.handle.obj.Channel;
import sx.blah.discord.handle.obj.Guild;
import sx.blah.discord.handle.obj.User;

import java.util.ArrayList;
import java.util.List;

/**
 * This object represents a guild. This is an intermediary between Discord4J and a script in order to prevent Discord4J
 * breaking stuff in the future.
 */
public class ScriptGuild {
	
	protected Guild javaGuild;
	
	public ScriptGuild(Guild guild) {
		javaGuild = guild;
	}
	
	/**
	 * Gets all the channels in the guild
	 * @return The array of channels in the guild
	 */
	public ScriptChannel[] getChannels() {
		List<ScriptChannel> channels = new ArrayList<>();
		for (Channel channel : javaGuild.getChannels())
			channels.add(new ScriptChannel(channel));
		return channels.toArray(new ScriptChannel[channels.size()]);
	}
	
	/**
	 * Gets a single channel from a channel id
	 * @param id The channel id
	 * @return The channel (if found)
	 */
	public ScriptChannel getChannelByID(String id) {
		return new ScriptChannel(javaGuild.getChannelByID(id));
	}
	
	/**
	 * Gets all the users in the guid
	 * @return The array of users in the guild
	 */
	public ScriptUser[] getUsers() {
		List<ScriptUser> users = new ArrayList<>();
		for (User user : javaGuild.getUsers())
			users.add(new ScriptUser(user));
		return users.toArray(new ScriptUser[users.size()]);
	}
	
	/**
	 * Gets a single user from a user id
	 * @param id The user id
	 * @return The user (if found)
	 */
	public ScriptUser getUserByID(String id) {
		return new ScriptUser(javaGuild.getUserByID(id));
	}
	
	/**
	 * Gets the guild's name
	 * @return The guild name
	 */
	public String getName() {
		return javaGuild.getName();
	}
	
	/**
	 * Gets the guild's id
	 * @return The guild id
	 */
	public String getID() {
		return javaGuild.getID();
	}
}
