package com.austinv11.DiscordBot.api.plugins.api.bot;

import com.austinv11.DiscordBot.DiscordBot;

/**
 * A class for dealing with permissions levels  
 */
public class PermissionsLevel {
	
	/**
	 * Permission level for the rank 'Anyone'
	 */
	public int ANYONE = DiscordBot.getLevelForRank("Anyone");
	/**
	 * Permission level for the rank 'Default'
	 */
	public int DEFAULT = DiscordBot.getLevelForRank("Default");
	/**
	 * Permission level for the rank 'Administrator'
	 */
	public int ADMINISTRATOR = DiscordBot.getLevelForRank("Administrator");
	/**
	 * Permission level for the rank 'Owner'
	 */
	public int OWNER = DiscordBot.getLevelForRank("Owner");
	
	/**
	 * Gets the permission level for a rank name
	 * @param rank The rank name
	 * @return The permission level
	 */
	public int getLevelForRank(String rank) {
		return DiscordBot.getLevelForRank(rank);
	}
	
	/**
	 * Gets the rank name for a permission level
	 * @param level The permission level
	 * @return The rank name
	 */
	public String getRankForLevel(int level) {
		return DiscordBot.getRankForLevel(level);
	}
}
