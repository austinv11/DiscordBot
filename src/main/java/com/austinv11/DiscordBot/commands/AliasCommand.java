package com.austinv11.DiscordBot.commands;

import com.austinv11.DiscordBot.DiscordBot;
import com.austinv11.DiscordBot.api.CommandRegistry;
import com.austinv11.DiscordBot.api.commands.CommandSyntaxException;
import com.austinv11.DiscordBot.api.commands.ICommand;
import sx.blah.discord.handle.obj.Channel;
import sx.blah.discord.handle.obj.Message;
import sx.blah.discord.handle.obj.User;

import java.util.Optional;

public class AliasCommand implements ICommand {
	
	@Override
	public String getCommand() {
		return "alias";
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
		return getCommand()+" [alias] [command sequence]";
	}
	
	@Override
	public String getHelpMessage() {
		return "Creates a custom alias for a given command sequence";
	}
	
	@Override
	public int getDefaultPermissionLevel() {
		return ICommand.ADMINISTRATOR;
	}
	
	@Override
	public Optional<String> executeCommand(String parameters, User executor, Channel channel, Message commandMessage) throws CommandSyntaxException {
		if (parameters.split(" ").length < 2)
			throw new CommandSyntaxException("Invalid amount of parameters!");
		
		String alias = parameters.split(" ")[0];
		String commandSequence = parameters.substring(alias.length()+1);
		if (commandSequence.startsWith(String.valueOf(DiscordBot.CONFIG.commandDiscriminator)))
			commandSequence = commandSequence.substring(1);
		try {
			CommandRegistry.registerAlias(alias, commandSequence);
		} catch (NullPointerException e) {
			throw new CommandSyntaxException("Command ```"+commandSequence+"``` is invalid!");
		}
		return Optional.of("Command ```"+commandSequence+"``` recorded for alias ```"+alias+"```");
	}
}
