package com.austinv11.DiscordBot.api.plugins.api.events.discord;

import com.austinv11.DiscordBot.api.plugins.api.IScriptEvent;
import com.austinv11.DiscordBot.api.plugins.api.discord.ScriptInvite;
import com.austinv11.DiscordBot.api.plugins.api.discord.ScriptMessage;
import sx.blah.discord.handle.impl.events.InviteReceivedEvent;

/**
 * This event is propagated when an invite is received by the bot
 */
public class InviteReceivedScriptEvent implements IScriptEvent {
	
	private InviteReceivedEvent javaEvent;
	
	public InviteReceivedScriptEvent(InviteReceivedEvent event) {
		this.javaEvent = event;
	}
	
	/**
	 * Gets the message which sent the invite
	 * @return The message
	 */
	public ScriptMessage getMessage() {
		return new ScriptMessage(javaEvent.getMessage());
	}
	
	/**
	 * Gets the invite sent to the bot
	 * @return The invite
	 */
	public ScriptInvite getInvite() {
		return new ScriptInvite(javaEvent.getInvite());
	}
}
