package com.austinv11.DiscordBot.api.plugins.api.events.discord;

import com.austinv11.DiscordBot.api.plugins.api.IScriptEvent;
import com.austinv11.DiscordBot.api.plugins.api.discord.ScriptChannel;
import sx.blah.discord.handle.impl.events.ChannelDeleteEvent;

/**
 * This event is posted when a channel is deleted
 */
public class ChannelDeleteScriptEvent implements IScriptEvent {
	
	private ChannelDeleteEvent javaEvent;
	
	public ChannelDeleteScriptEvent(ChannelDeleteEvent event) {
		this.javaEvent = event;
	}
	
	/**
	 * Gets the channel that was deleted
	 * @return The channel
	 */
	public ScriptChannel getChannel() {
		return new ScriptChannel(javaEvent.getChannel());
	}
}
