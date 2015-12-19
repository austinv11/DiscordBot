package com.austinv11.DiscordBot.api.plugins.api.events.discord;

import com.austinv11.DiscordBot.DiscordBot;
import com.austinv11.DiscordBot.api.plugins.api.IScriptEvent;
import com.austinv11.DiscordBot.api.plugins.api.discord.ScriptGuild;
import com.austinv11.DiscordBot.api.plugins.api.discord.ScriptUser;
import sx.blah.discord.handle.impl.events.GameChangeEvent;

/**
 * This event is posted when a user changes the game being played
 */
public class GameChangeScriptEvent implements IScriptEvent {
	
	private GameChangeEvent javaEvent;
	
	public GameChangeScriptEvent(GameChangeEvent event) {
		this.javaEvent = event;
	}
	
	/**
	 * Gets the guild the event took place in
	 * @return The guild
	 */
	public ScriptGuild getGuild() {
		return new ScriptGuild(javaEvent.getGuild());
	}
	
	/**
	 * Gets the user the event came from
	 * @return The user
	 */
	public ScriptUser getUser() {
		return new ScriptUser(javaEvent.getUser());
	}
	
	/**
	 * Gets the original game played
	 * @return The original game (or null if not found)
	 */
	public String getOldGame() {
		return DiscordBot.instance.getGameByID(javaEvent.getOldGameID()).orElse(null);
	}
	
	/**
	 * Gets the new game played
	 * @return The new game (or null if not found)
	 */
	public String getNewGame() {
		return DiscordBot.instance.getGameByID(javaEvent.getNewGameID()).orElse(null);
	}
}
