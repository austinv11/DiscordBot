package com.austinv11.DiscordBot.commands;

import com.austinv11.DiscordBot.DiscordBot;
import com.austinv11.DiscordBot.api.commands.CommandSyntaxException;
import com.austinv11.DiscordBot.api.commands.ICommand;
import sx.blah.discord.handle.obj.Channel;
import sx.blah.discord.handle.obj.Guild;
import sx.blah.discord.handle.obj.Message;
import sx.blah.discord.handle.obj.User;

import java.util.Optional;

public class WhoisCommand implements ICommand {
	
	@Override
	public String getCommand() {
		return "whois";
	}
	
	@Override
	public String[] getAliases() {
		return new String[]{"who"};
	}
	
	@Override
	public boolean removesCommandMessage() {
		return true;
	}
	
	@Override
	public String getUsage() {
		return getCommand()+" guild/channel/user [optional:guild/channel/user]";
	}
	
	@Override
	public String getHelpMessage() {
		return "This command will retrieve internal api information about a user";
	}
	
	@Override
	public int getDefaultPermissionLevel() {
		return ICommand.DEFAULT;
	}
	
	@Override
	public Optional<String> executeCommand(String parameters, User executor, Channel channel, Message commandMessage) throws CommandSyntaxException {
		String result = "No results found for your query!";
		if (parameters != null && !parameters.isEmpty()) {
			String command = parameters.split(" ")[0];
			String args = parameters.replaceFirst(command, "");
			if (args.length() > 0)
				args = args.replaceFirst(" ", "");
			if (command.equals("guild")) {
				if (args.isEmpty()) {
					outside: for (Guild guild : DiscordBot.instance.getGuildList())
						for (User user : guild.getUsers())
							if (user.getID().equals(executor.getID())) {
								result = "Guild "+guild.getName()+" has the id "+guild.getID();
								break outside;
							}
				} else {
					for (Guild guild : DiscordBot.instance.getGuildList())
						if (guild.getName().equalsIgnoreCase(args)) {
							result = "Guild "+args+" has the id "+guild.getID();
							break;
						}
				}
			} else if (command.equals("channel")) {
				if (args.isEmpty()) {
					result = "Channel "+channel.getName()+" has the id "+channel.getChannelID();
				} else {
					boolean found = false;
					 outside: for (Guild guild : DiscordBot.instance.getGuildList()) {
						 inside: for (User user : guild.getUsers())
							 if (user.getID().equals(executor.getID())) {
								 found = true;
								 break inside;
							 }
						 if (found) {
							 for (Channel channelFound : guild.getChannels()) {
								 if (channelFound.getName().equalsIgnoreCase(args)) {
									 result = "Channel "+args+" has the id "+channel.getChannelID();
									 break outside;
								 } else {
									 found = false;
								 }
							 }
						 }
					 }
				}
			} else if (command.equals("user")) {
				if (args.isEmpty()) {
					result = "User "+executor.getName()+" has the id "+executor.getID()+" and has a permission level of "+DiscordBot.getUserPermissionLevel(executor)+" with an avatar located at "+executor.getAvatarURL();
				} else {
					outside: for (Guild guild : DiscordBot.instance.getGuildList())
						for (User user : guild.getUsers()) {
							if (user.getName().equals(args)) { //Doesn't ignore case because there are too many users
								result = "User "+args+" has the id "+user.getID()+" with an avatar located at "+user.getAvatarURL();
								break outside;
							}
						}
				}
			} else {
				throw new CommandSyntaxException("Argument '"+command+"' is invalid!");
			}
		} else {
			throw new CommandSyntaxException("No argument provided!");
		}
		return Optional.ofNullable(result.isEmpty() ? null : result);
	}
}
