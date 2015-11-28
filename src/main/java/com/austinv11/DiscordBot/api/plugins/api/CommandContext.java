package com.austinv11.DiscordBot.api.plugins.api;

import com.austinv11.DiscordBot.api.plugins.PluginManifest;
import com.austinv11.DiscordBot.api.plugins.api.discord.ScriptChannel;
import com.austinv11.DiscordBot.api.plugins.api.discord.ScriptMessage;
import com.austinv11.DiscordBot.api.plugins.api.discord.ScriptUser;
import sx.blah.discord.handle.obj.Channel;
import sx.blah.discord.handle.obj.Message;
import sx.blah.discord.handle.obj.User;

/**
 * An object representing the context for which a command is executed
 */
public class CommandContext extends Context {
	
	/**
	 * The string representation of the message with the command prefix cut out
	 */
	public String parameters;
	/**
	 * The user who executed the command
	 */
	public ScriptUser executor;
	/**
	 * The channel the command was executed in
	 */
	public ScriptChannel channel;
	/**
	 * The message object representing what executed the command
	 */
	public ScriptMessage originalMessage;
	
	public CommandContext(String parameters, User executor, Channel channel, Message originalMessage, PluginManifest plugin) {
		this.parameters = parameters;
		this.executor = new ScriptUser(executor);
		this.channel = new ScriptChannel(channel);
		this.originalMessage = new ScriptMessage(originalMessage);
		this.plugin = plugin;
	}
}
