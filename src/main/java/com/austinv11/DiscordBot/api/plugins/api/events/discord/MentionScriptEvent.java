package com.austinv11.DiscordBot.api.plugins.api.events.discord;

import com.austinv11.DiscordBot.api.plugins.api.IScriptEvent;
import com.austinv11.DiscordBot.api.plugins.api.discord.ScriptMessage;
import sx.blah.discord.handle.impl.events.MentionEvent;

/**
 * This event is posted when the bot's account is @mention'd in a message
 */
public class MentionScriptEvent implements IScriptEvent {
	
	private MentionEvent javaEvent;
	
	public MentionScriptEvent(MentionEvent event) {
		this.javaEvent = event;
	}
	
	/**
	 * Gets the message which mentioned the bot
	 * @return The message
	 */
	public ScriptMessage getMessage() {
		return new ScriptMessage(javaEvent.getMessage());
	}
}
