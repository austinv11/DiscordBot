package com.austinv11.DiscordBot.api.plugins.api.events.discord;

import com.austinv11.DiscordBot.api.plugins.api.IScriptEvent;
import com.austinv11.DiscordBot.api.plugins.api.discord.ScriptMessage;
import sx.blah.discord.handle.impl.events.MessageDeleteEvent;

/**
 * This event is posted when a message is deleted
 */
public class MessageDeleteScriptEvent implements IScriptEvent {
	
	private MessageDeleteEvent javaEvent;
	
	public MessageDeleteScriptEvent(MessageDeleteEvent event) {
		this.javaEvent = event;
	}
	
	/**
	 * Gets the message that was deleted
	 * @return The message
	 */
	public ScriptMessage getMessage() {
		return new ScriptMessage(javaEvent.getMessage());
	}
}
