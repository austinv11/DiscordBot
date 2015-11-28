package com.austinv11.DiscordBot.api.plugins.api.util;

import java.time.*;

/**
 * A utility class for dealing with timestamps. Used statically (i.e. without an object instance), the API will
 * automatically use the current date and time, otherwise it'll use the stored time in the instance.
 */
public class Time {
	
	private LocalDateTime javaDateTime;
	
	public Time(LocalDateTime dateTime) {
		javaDateTime = dateTime;
	}
	
	public Time(LocalDate date) {
		this(LocalDateTime.of(date, LocalTime.now()));
	}
	
	public Time(LocalTime time) {
		this(LocalDateTime.of(LocalDate.now(), time));
	}
	
	private LocalDateTime getJavaDateTime() {
		return javaDateTime == null ? LocalDateTime.now() : javaDateTime;
	}
	
	/**
	 * Used for static methods
	 */
	public Time() {}
	
	/**
	 * Gets the month of the timestamp
	 * @return The month number
	 */
	public int getMonth() {
		return getJavaDateTime().getMonthValue();
	}
	
	/**
	 * Gets the day of the month of the timestamp
	 * @return The day of the month
	 */
	public int getDay() {
		return getJavaDateTime().getDayOfMonth();
	}
	
	/**
	 * Gets the year of the timestamp
	 * @return The year
	 */
	public int getYear() {
		return getJavaDateTime().getYear();
	}
	
	/**
	 * Gets the hour of the timestamp
	 * @return The hour (0-23)
	 */
	public int getHour() {
		return getJavaDateTime().getHour();
	}
	
	/**
	 * Gets the minute of the timestamp
	 * @return The minute (0-59)
	 */
	public int getMinute() {
		return getJavaDateTime().getMinute();
	}
	
	/**
	 * Gets the second of the timestamp
	 * @return the second (0-59)
	 */
	public int getSecond() {
		return getJavaDateTime().getSecond();
	}
	
	/**
	 * Gets the timestamp representing in milliseconds
	 * @return The millisecond timestamp
	 */
	public long getEpochMilli() {
		return getJavaDateTime().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
	}
	
	/**
	 * Gets the timestamp of now
	 * @return The timestamp from now
	 */
	public Time now() {
		return new Time(LocalDateTime.now());
	}
}
