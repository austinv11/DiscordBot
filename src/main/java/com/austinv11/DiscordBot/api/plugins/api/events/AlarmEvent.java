package com.austinv11.DiscordBot.api.plugins.api.events;

import com.austinv11.DiscordBot.api.plugins.api.IScriptEvent;

import java.util.UUID;

/**
 * Propagated when an alarm goes off
 */
public class AlarmEvent implements IScriptEvent {
	
	/**
	 * The unique id for this alarm
	 */
	public String id;
	
	public AlarmEvent(UUID uuid) {
		id = uuid.toString();
	}
}
