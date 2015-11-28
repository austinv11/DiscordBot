package com.austinv11.DiscordBot.api.plugins.api.discord;

import com.austinv11.DiscordBot.DiscordBot;
import sx.blah.discord.handle.obj.Invite;
import sx.blah.discord.util.HTTP403Exception;

/**
 * This object represents an invite. This is an intermediary between Discord4J and a script in order to prevent Discord4J
 * breaking stuff in the future.
 */
public class ScriptInvite {
	
	protected Invite javaInvite;
	
	public ScriptInvite(Invite invite) {
		this.javaInvite = invite;
	}
	
	/**
	 * Gets the invite code (the invite url minus the https://discord.gg/)
	 * @return THe invite code
	 */
	public String getInviteCode() {
		return javaInvite.getInviteCode();
	}
	
	/**
	 * Accepts the invitation
	 * @throws HTTP403Exception
	 */
	public void accept() throws HTTP403Exception {
		javaInvite.accept();
	}
	
	/**
	 * Gets the guild the bot is being invited to
	 * @return The guild
	 * @throws Exception
	 */
	public ScriptGuild getGuild() throws Exception {
		return new ScriptGuild(DiscordBot.instance.getGuildByID(javaInvite.details().getGuildID()));
	}
	
	/**
	 * Gets the channel the bot is being invited to
	 * @return The channel
	 * @throws Exception
	 */
	public ScriptChannel getChannel() throws Exception {
		return new ScriptChannel(DiscordBot.instance.getChannelByID(javaInvite.details().getChannelID()));
	}
}
