package com.austinv11.DiscordBot.api.plugins.api.events.discord;

import com.austinv11.DiscordBot.api.plugins.api.IScriptEvent;
import com.austinv11.DiscordBot.api.plugins.api.discord.ScriptChannel;
import com.austinv11.DiscordBot.api.plugins.api.discord.ScriptUser;
import sx.blah.discord.handle.impl.events.TypingEvent;

/**
 * This event is posted when a user is typing
 */
public class TypingScriptEvent implements IScriptEvent {
	
	private TypingEvent javaEvent;
	
	public TypingScriptEvent(TypingEvent event) {
		this.javaEvent = event;
	}
	
	/**
	 * Gets the user who started typing
	 * @return The user
	 */
	public ScriptUser getUser() {
		return new ScriptUser(javaEvent.getUser());
	}
	
	/**
	 * Gets the channel where the user started typing
	 * @return The channel
	 */
	public ScriptChannel getChannel() {
		return new ScriptChannel(javaEvent.getChannel());
	}
}
