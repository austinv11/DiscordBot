package com.austinv11.DiscordBot.reference;

public class Config {
	
	public char commandDiscriminator = '!';
	public int maxUserMessagesPerSecond = 3;
	public int cooldownTime = 10;
	public boolean enableSpamFilter = false;
	public String databaseFile = "permissions.db";
	public boolean runServerFrontEnd = true;
	public String hostName = null;
	public int port = 8080;
	public boolean enableRespectTables = true;
}
