package com.austinv11.DiscordBot.api.plugins.api.events.discord;

import com.austinv11.DiscordBot.api.plugins.api.IScriptEvent;
import com.austinv11.DiscordBot.api.plugins.api.discord.ScriptMessage;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;

/**
 * This event is posted when a message is received by the bot
 */
public class MessageReceivedScriptEvent implements IScriptEvent {
	
	private MessageReceivedEvent javaEvent;
	
	public MessageReceivedScriptEvent(MessageReceivedEvent event) {
		this.javaEvent = event;
	}
	
	/**
	 * Gets the message received by the bot
	 * @return The message
	 */
	public ScriptMessage getMessage() {
		return new ScriptMessage(javaEvent.getMessage());
	}
}
