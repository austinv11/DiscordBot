package com.austinv11.DiscordBot.commands;

import com.austinv11.DiscordBot.api.commands.CommandSyntaxException;
import com.austinv11.DiscordBot.api.commands.ICommand;
import sx.blah.discord.handle.obj.Channel;
import sx.blah.discord.handle.obj.Message;
import sx.blah.discord.handle.obj.User;

import java.util.Optional;

public class ShrugCommand implements ICommand {
	
	@Override
	public String getCommand() {
		return "shrug";
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
		return getCommand();
	}
	
	@Override
	public String getHelpMessage() {
		return "Prints a shrug emoticon ¯\\\\\\_(ツ)_/¯";
	}
	
	@Override
	public int getDefaultPermissionLevel() {
		return ICommand.DEFAULT;
	}
	
	@Override
	public Optional<String> executeCommand(String parameters, User executor, Channel channel, Message commandMessage) throws CommandSyntaxException {
		return Optional.of("¯\\\\\\_(ツ)_/¯");
	}
}
