package com.austinv11.DiscordBot.api.plugins.api.events.discord;

import com.austinv11.DiscordBot.api.plugins.api.IScriptEvent;
import com.austinv11.DiscordBot.api.plugins.api.discord.ScriptMessage;
import sx.blah.discord.handle.impl.events.MessageSendEvent;

/**
 * This event is posted when a message is sent by the bot
 */
public class MessageSendScriptEvent implements IScriptEvent {
	
	private MessageSendEvent javaEvent;
	
	public MessageSendScriptEvent(MessageSendEvent event) {
		this.javaEvent = event;
	}
	
	/**
	 * Gets the message sent by the bot
	 * @return The message
	 */
	public ScriptMessage getMessage() {
		return new ScriptMessage(javaEvent.getMessage());
	}
}
