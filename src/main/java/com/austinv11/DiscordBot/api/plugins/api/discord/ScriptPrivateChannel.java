package com.austinv11.DiscordBot.api.plugins.api.discord;

import sx.blah.discord.handle.obj.Channel;
import sx.blah.discord.handle.obj.PrivateChannel;

/**
 * This object represents a private channel. This is an intermediary between Discord4J and a script in order to prevent 
 * Discord4J breaking stuff in the future.
 */
public class ScriptPrivateChannel extends ScriptChannel {
	
	private ScriptPrivateChannel(Channel channel) {
		super(channel);
	}
	
	public ScriptPrivateChannel(PrivateChannel channel) {
		super(channel);
	}
	
	/**
	 * Gets the user with whom you are communicating with
	 * @return The user you are communicating with
	 */
	public ScriptUser getUser() {
		return new ScriptUser(((PrivateChannel)javaChannel).getRecipient());
	}
}
