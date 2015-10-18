package com.austinv11.DiscordBot.reference;

public class Config {
	
	public static char commandDiscriminator = '!';
	public static int maxUserMessagesPerSecond = 3;//TODO: change
	public static int cooldownTime = 10;
	public static String databaseFile = "permissions.db";
	public static boolean runServerFrontEnd = true;
	public static String hostName = null;
	public static int port = 8080;
	public static boolean enableSpamFilter = false;
	public static boolean enableRespectTables = true;
}
