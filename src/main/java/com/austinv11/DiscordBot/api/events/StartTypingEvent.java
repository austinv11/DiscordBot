package com.austinv11.DiscordBot.api.events;

import sx.blah.discord.obj.Channel;
import sx.blah.discord.obj.User;

@DiscordEvent
public class StartTypingEvent {
	
	/**
	 * The user involved
	 */
	public User user;
	/**
	 * The channel involved
	 */
	public Channel channel;
	
	public StartTypingEvent(User user, Channel channel) {
		this.user = user;
		this.channel = channel;
	}
}
