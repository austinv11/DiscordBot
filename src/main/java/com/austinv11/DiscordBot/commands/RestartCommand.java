package com.austinv11.DiscordBot.commands;

import com.austinv11.DiscordBot.DiscordBot;
import com.austinv11.DiscordBot.api.commands.CommandSyntaxException;
import com.austinv11.DiscordBot.api.commands.ICommand;
import sx.blah.discord.handle.obj.Channel;
import sx.blah.discord.handle.obj.Message;
import sx.blah.discord.handle.obj.User;

import java.util.Optional;

public class RestartCommand implements ICommand {
	
	@Override
	public String getCommand() {
		return "restart";
	}
	
	@Override
	public String[] getAliases() {
		return new String[]{"reboot"};
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
		return "Restarts the bot";
	}
	
	@Override
	public int getDefaultPermissionLevel() {
		return ICommand.OWNER;
	}
	
	@Override
	public Optional<String> executeCommand(String parameters, User executor, Channel channel, Message commandMessage) throws CommandSyntaxException {
		DiscordBot.restart();
		return Optional.empty();
	}
}
