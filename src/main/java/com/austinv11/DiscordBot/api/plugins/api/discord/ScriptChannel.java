package com.austinv11.DiscordBot.api.plugins.api.discord;

import sx.blah.discord.handle.obj.Channel;
import sx.blah.discord.handle.obj.Message;
import sx.blah.discord.handle.obj.PrivateChannel;

import javax.script.ScriptException;
import java.util.ArrayList;
import java.util.List;

/**
 * This object represents a channel. This is an intermediary between Discord4J and a script in order to prevent Discord4J
 * breaking stuff in the future.
 */
public class ScriptChannel {
	
	protected Channel javaChannel;
	
	public ScriptChannel(Channel channel) {
		javaChannel = channel;
	}
	
	/**
	 * Gets the name of the channel
	 * @return The channel's name
	 */
	public String getName() {
		return javaChannel.getName();
	}
	
	/**
	 * Gets the id of the channel
	 * @return The channel's id
	 */
	public String getID() {
		return javaChannel.getID();
	}
	
	/**
	 * Gets the messages for the channel
	 * @return The array of all messages for the channel
	 */
	public ScriptMessage[] getMessages() {
		List<ScriptMessage> messages = new ArrayList<>();
		for (Message message : javaChannel.getMessages())
			messages.add(new ScriptMessage(message));
		return messages.toArray(new ScriptMessage[messages.size()]);
	}
	
	/**
	 * Gets a specific message by its id
	 * @param id The message id
	 * @return The message (if found)
	 */
	public ScriptMessage getMessageByID(String id) {
		return new ScriptMessage(javaChannel.getMessageByID(id));
	}
	
	/**
	 * Gets the guild the channel is a part of
	 * @return The guild
	 */
	public ScriptGuild getGuild() {
		return new ScriptGuild(javaChannel.getParent());
	}
	
	/**
	 * Checks if the channel is in fact a private channel
	 * @return True if private, false if otherwise
	 */
	public boolean isPrivate() {
		return javaChannel.isPrivate();
	}
	
	/**
	 * Converts the channel to a private channel object
	 * @return The private channel object
	 * @throws ScriptException Thrown if the channel isn't private
	 */
	public ScriptPrivateChannel convertToPrivateChannel() throws ScriptException {
		if (!isPrivate()) {
			throw new ScriptException("Error converting channel "+getName()+" with the id "+getID()+" to private. Are " +
					"you sure its a private channel?");
		}
		return new ScriptPrivateChannel((PrivateChannel) javaChannel);
	}
}
