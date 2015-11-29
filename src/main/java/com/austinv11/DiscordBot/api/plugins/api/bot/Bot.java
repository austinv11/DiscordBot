package com.austinv11.DiscordBot.api.plugins.api.bot;

import com.austinv11.DiscordBot.DiscordBot;
import com.austinv11.DiscordBot.api.plugins.api.discord.ScriptChannel;
import com.austinv11.DiscordBot.api.plugins.api.discord.ScriptGuild;
import com.austinv11.DiscordBot.api.plugins.api.discord.ScriptPrivateChannel;
import com.austinv11.DiscordBot.api.plugins.api.discord.ScriptUser;
import sx.blah.discord.handle.obj.Guild;

import javax.script.ScriptEngine;
import javax.script.ScriptException;
import java.util.ArrayList;
import java.util.List;

/**
 * An api which allows miscellaneous interactions with the bot through scripts
 */
public class Bot {
	
	/**
	 * Runs a script from a string
	 * @param language The language to execute the script in
	 * @param script The script (in string form)
	 * @return The return value of the script
	 * @throws ScriptException
	 */
	public Object runScript(String language, String script) throws ScriptException {
		ScriptEngine engine = DiscordBot.getScriptEngineForLang(language);
		return engine.eval(script);
	}
	
	/**
	 * Gets all the guilds the bot is connected to
	 * @return The guilds the bot is connected to
	 */
	public ScriptGuild[] getConnectedGuilds() {
		List<ScriptGuild> guilds = new ArrayList<>();
		for (Guild guild : DiscordBot.instance.getGuilds()) {
			guilds.add(new ScriptGuild(guild));
		}
		return guilds.toArray(new ScriptGuild[guilds.size()]);
	}
	
	/**
	 * Gets a user by the user's id
	 * @param id The user id
	 * @return The user
	 */
	public ScriptUser getUserByID(String id) {
		return new ScriptUser(DiscordBot.instance.getUserByID(id));
	}
	
	/**
	 * Gets a channel by the channel's id
	 * @param id The channel id
	 * @return The channel
	 */
	public ScriptChannel getChannelByID(String id) {
		return new ScriptChannel(DiscordBot.instance.getChannelByID(id));
	}
	
	/**
	 * Gets a guild by the guild's id
	 * @param id The guild id
	 * @return The guild
	 */
	public ScriptGuild getGuildByID(String id) {
		return new ScriptGuild(DiscordBot.instance.getGuildByID(id));
	}
	
	/**
	 * Gets the user the bot is logged in as
	 * @return The user
	 */
	public ScriptUser getOurUser() {
		return new ScriptUser(DiscordBot.instance.getOurUser());
	}
	
	/**
	 * Gets/creates a private channel for the specified user
	 * @param user The user
	 * @return The private channel
	 */
	public ScriptPrivateChannel getPrivateChannel(ScriptUser user) throws Exception {
		return new ScriptPrivateChannel(DiscordBot.instance.getOrCreatePMChannel(DiscordBot.instance.getUserByID(user.getID())));
	}
	
	/**
	 * Kills the bot
	 */
	public void shutdown() {
		Runtime.getRuntime().exit(DiscordBot.INTERNAL_EXIT_CODE);
	}
	
	/**
	 * Restarts the bot
	 */
	public void restart() {
		DiscordBot.restart();
	}
}
