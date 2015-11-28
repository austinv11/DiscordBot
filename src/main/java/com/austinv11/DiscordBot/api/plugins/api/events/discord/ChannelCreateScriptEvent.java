package com.austinv11.DiscordBot.api.plugins.api.events.discord;

import com.austinv11.DiscordBot.api.plugins.api.IScriptEvent;
import com.austinv11.DiscordBot.api.plugins.api.discord.ScriptChannel;
import sx.blah.discord.handle.impl.events.ChannelCreateEvent;

/**
 * This event is posted when a channel is created
 */
public class ChannelCreateScriptEvent implements IScriptEvent {
	
	private ChannelCreateEvent javaEvent;
	
	public ChannelCreateScriptEvent(ChannelCreateEvent event) {
		this.javaEvent = event;
	}
	
	/**
	 * Gets the channel that was created
	 * @return The channel
	 */
	public ScriptChannel getChannel() {
		return new ScriptChannel(javaEvent.getChannel());
	}
}
