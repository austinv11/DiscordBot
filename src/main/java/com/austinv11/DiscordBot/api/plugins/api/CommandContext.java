package com.austinv11.DiscordBot.api.plugins.api;

import com.austinv11.DiscordBot.api.plugins.Plugin;
import sx.blah.discord.handle.obj.Channel;
import sx.blah.discord.handle.obj.Message;
import sx.blah.discord.handle.obj.User;

/**
 * An object representing the context for which a command is executed
 */
public class CommandContext extends Context { //TODO use custom implementations of User, Channel and Message
	
	/**
	 * The string representation of the message with the command prefix cut out
	 */
	public String parameters;
	/**
	 * The user who executed the command
	 */
	public User executor;
	/**
	 * The channel the command was executed in
	 */
	public Channel channel;
	/**
	 * The message object representing what executed the command
	 */
	public Message originalMessage;
	
	public CommandContext(String parameters, User executor, Channel channel, Message originalMessage, Plugin plugin) {
		this.parameters = parameters;
		this.executor = executor;
		this.channel = channel;
		this.originalMessage = originalMessage;
		this.plugin = plugin;
	}
}
