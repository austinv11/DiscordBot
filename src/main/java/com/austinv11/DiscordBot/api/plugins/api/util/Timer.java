package com.austinv11.DiscordBot.api.plugins.api.util;

import com.austinv11.DiscordBot.api.plugins.api.events.AlarmEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * A utility class pertaining to timing for scripting
 */
public class Timer {
	
	private HashMap<UUID, Long> runningTimers = new HashMap<>();
	private List<UUID> usedUUIDs = new ArrayList<>();
	
	/**
	 * Starts an alarm, causes an {@link AlarmEvent} to be posted after the specified amount of time
	 * @param alarmTime The time (in milliseconds) to wait before the alarm goes off
	 * @return The unique id for the alarm
	 */
	public String startAlarm(final long alarmTime) {
		final UUID id = getUUID();
		new Thread(()->{
			try {
				Thread.sleep(alarmTime);
				new AlarmEvent(id).propagate();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}).start();
		return id.toString();
	}
	
	/**
	 * Starts a timer
	 * @return The unique id for the timer
	 */
	public String startTimer() {
		UUID id = getUUID();
		runningTimers.put(id, System.currentTimeMillis());
		return id.toString();
	}
	
	/**
	 * Retrieves the time (in milliseconds) since the timer was started
	 * @param timerId The unique id for the timer
	 * @return The time (in milliseconds) since the timer was started
	 */
	public long getTime(String timerId) {
		return System.currentTimeMillis() - runningTimers.get(UUID.fromString(timerId));
	}
	
	private UUID getUUID() {
		UUID uuid = UUID.randomUUID();
		while (usedUUIDs.contains(uuid)) {
			uuid = UUID.randomUUID();
		}
		usedUUIDs.add(uuid);
		return uuid;
	}
}
