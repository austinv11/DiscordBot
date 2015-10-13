package com.austinv11.DiscordBot.reference;

public class Config {
	
	public static char commandDiscriminator = '!';
	public static int maxUserMessagesPerSecond = Integer.MAX_VALUE;//TODO: change
	public static int cooldownTime = 10;
	public static String databaseFile = "permissions.db";
	public static boolean runServerFrontEnd = true;
	public static String hostName = null;
	public static int port = 8080;
}
