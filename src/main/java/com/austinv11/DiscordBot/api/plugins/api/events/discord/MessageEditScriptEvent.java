package com.austinv11.DiscordBot.api.plugins.api.events.discord;

import com.austinv11.DiscordBot.api.plugins.api.IScriptEvent;
import com.austinv11.DiscordBot.api.plugins.api.discord.ScriptMessage;
import sx.blah.discord.handle.impl.events.MessageUpdateEvent;

/**
 * This event is posted when a message is edited
 */
public class MessageEditScriptEvent implements IScriptEvent {
	
	private MessageUpdateEvent javaEvent;
	
	public MessageEditScriptEvent(MessageUpdateEvent event) {
		this.javaEvent = event;
	}
	
	/**
	 * Gets the original, unedited message
	 * @return The message
	 */
	public ScriptMessage getOldMessage() {
		return new ScriptMessage(javaEvent.getOldMessage());
	}
	
	/**
	 * Gets the new, edited message
	 * @return The message
	 */
	public ScriptMessage getNewMessage() {
		return new ScriptMessage(javaEvent.getNewMessage());
	}
}
