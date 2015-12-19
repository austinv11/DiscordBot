package com.austinv11.DiscordBot.commands;

import com.austinv11.DiscordBot.api.commands.CommandSyntaxException;
import com.austinv11.DiscordBot.api.commands.ICommand;
import sx.blah.discord.handle.obj.Channel;
import sx.blah.discord.handle.obj.Message;
import sx.blah.discord.handle.obj.User;

import java.util.Optional;

public class SayCommand implements ICommand {
	
	@Override
	public String getCommand() {
		return "say";
	}
	
	@Override
	public String[] getAliases() {
		return new String[0];
	}
	
	@Override
	public boolean removesCommandMessage() {
		return true;
	}
	
	@Override
	public String getUsage() {
		return getCommand()+" [message]";
	}
	
	@Override
	public String getHelpMessage() {
		return "Makes the bot send a message";
	}
	
	@Override
	public int getDefaultPermissionLevel() {
		return ICommand.ANYONE;
	}
	
	@Override
	public Optional<String> executeCommand(String parameters, User executor, Channel channel, Message commandMessage) throws CommandSyntaxException {
		return Optional.ofNullable(parameters);
	}
}
