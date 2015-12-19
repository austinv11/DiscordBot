package com.austinv11.DiscordBot.reference;

public class Config {
	
	/**
	 * The character used to prefix commands
	 */
	public char commandDiscriminator = '!';
	/**
	 * Set to true to prevent users from spamming chat, false to stop the protection
	 */
	public boolean enableSpamFilter = false;
	/**
	 * The max amount of messages a user could send in one second, depends on {@link #enableSpamFilter}
	 */
	public int maxUserMessagesPerSecond = 3;
	/**
	 * The cooldown time before a user could send messages again after {@link #maxUserMessagesPerSecond} has been exceeded
	 */
	public int cooldownTime = 10;
	/**
	 * The file name for the database
	 */
	public String databaseFile = "bot.db";
	/**
	 * Set to true to enable the server front end
	 */
	public boolean runServerFrontEnd = true;
	/**
	 * The hostname, or localhost if null, for the server front end
	 */
	public String hostName = null;
	/**
	 * The port for the server front end to use
	 */
	public int port = 8080;
}
