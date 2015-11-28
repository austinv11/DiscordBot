package com.austinv11.DiscordBot.api.plugins.api.util;

import com.austinv11.DiscordBot.handler.Logger;

/**
 * A utility class for logging messages to the bot's logger
 */
public class Log {
	
	/**
	 * Logs a message to the debug level
	 * @param message The message
	 */
	public void debug(Object message) {
		Logger.log(Logger.Level.DEBUG, message);
	}
	
	/**
	 * Logs a message to the info level
	 * @param message The message
	 */
	public void info(Object message) {
		Logger.log(Logger.Level.INFO, message);
	}
	
	/**
	 * Logs a message to the warning level
	 * @param message The message
	 */
	public void warning(Object message) {
		Logger.log(Logger.Level.WARNING, message);
	}
	
	/**
	 * Logs a message to the severe level
	 * @param message The message
	 */
	public void severe(Object message) {
		Logger.log(Logger.Level.SEVERE, message);
	}
	
	/**
	 * Logs a message to the fatal level
	 * @param message The message
	 */
	public void fatal(Object message) {
		Logger.log(Logger.Level.FATAL, message);
	}
}
