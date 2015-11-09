package com.austinv11.DiscordBot.commands;

import com.austinv11.DiscordBot.api.commands.CommandSyntaxException;
import com.austinv11.DiscordBot.api.commands.ICommand;
import sx.blah.discord.handle.obj.Channel;
import sx.blah.discord.handle.obj.Message;
import sx.blah.discord.handle.obj.User;

import java.time.ZoneId;
import java.util.Optional;

public class PingCommand implements ICommand {
	
	@Override
	public String getCommand() {
		return "ping";
	}
	
	@Override
	public String[] getAliases() {
		return new String[0];
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
		return "Returns 'pong', useful for making sure the bot is alive";
	}
	
	@Override
	public int getDefaultPermissionLevel() {
		return ICommand.DEFAULT;
	}
	
	@Override
	public Optional<String> executeCommand(String parameters, User executor, Channel channel, Message commandMessage) throws CommandSyntaxException {
		long responseTime = commandMessage.getTimestamp().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
		return Optional.of("Pong!\nResponse time: "+(System.currentTimeMillis()-responseTime)+" ms");
	}
}
