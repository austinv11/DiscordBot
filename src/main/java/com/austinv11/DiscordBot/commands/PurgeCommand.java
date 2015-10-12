package com.austinv11.DiscordBot.commands;

import com.austinv11.DiscordBot.DiscordBot;
import com.austinv11.DiscordBot.api.commands.CommandSyntaxException;
import com.austinv11.DiscordBot.api.commands.ICommand;
import com.austinv11.DiscordBot.reference.Config;
import sx.blah.discord.obj.Channel;
import sx.blah.discord.obj.Message;
import sx.blah.discord.obj.User;

import java.io.File;
import java.sql.SQLException;
import java.util.Optional;

public class PurgeCommand implements ICommand {
	
	@Override
	public String getCommand() {
		return "purge";
	}
	
	@Override
	public String[] getAliases() {
		return new String[]{"clean"};
	}
	
	@Override
	public boolean removesCommandMessage() {
		return false;
	}
	
	@Override
	public String getUsage() {
		return getCommand();
	}
	
	@Override
	public String getHelpMessage() {
		return "The purge command clears the database and restarts the bot";
	}
	
	@Override
	public int getDefaultPermissionLevel() {
		return ICommand.OWNER;
	}
	
	@Override
	public Optional<String> executeCommand(String parameters, User executor, Channel channel, Message commandMessage) throws CommandSyntaxException {
		try {
			DiscordBot.db.disconnect();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		new File("./"+Config.databaseFile).delete();
		DiscordBot.restart();
		return Optional.empty();
	}
}
