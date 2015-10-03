package com.austinv11.DiscordBot.commands;

import com.austinv11.DiscordBot.api.commands.CommandSyntaxException;
import com.austinv11.DiscordBot.api.commands.ICommand;
import sx.blah.discord.obj.Channel;
import sx.blah.discord.obj.Message;
import sx.blah.discord.obj.User;

import java.util.Optional;

public class MeCommand implements ICommand {
	
	@Override
	public String getCommand() {
		return "me";
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
		return "Rephrases your message into an action";
	}
	
	@Override
	public Optional<String> executeCommand(String parameters, User executor, Channel channel, Message commandMessage) throws CommandSyntaxException {
		return Optional.of("*"+executor.getName()+" "+parameters+"*");
	}
}
