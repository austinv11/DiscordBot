package com.austinv11.DiscordBot.commands;

import com.austinv11.DiscordBot.DiscordBot;
import com.austinv11.DiscordBot.api.commands.CommandSyntaxException;
import com.austinv11.DiscordBot.api.commands.ICommand;
import org.apache.commons.lang3.StringEscapeUtils;
import org.json.simple.parser.ParseException;
import sx.blah.discord.handle.obj.Channel;
import sx.blah.discord.handle.obj.Message;
import sx.blah.discord.handle.obj.User;

import java.io.UnsupportedEncodingException;
import java.util.Optional;

public class NameCommand implements ICommand {
	
	@Override
	public String getCommand() {
		return "name";
	}
	
	@Override
	public String[] getAliases() {
		return new String[]{"nick", "nickname"};
	}
	
	@Override
	public boolean removesCommandMessage() {
		return false;
	}
	
	@Override
	public String getUsage() {
		return getCommand()+" [name]";
	}
	
	@Override
	public String getHelpMessage() {
		return "Changes the name of the bot";
	}
	
	@Override
	public int getDefaultPermissionLevel() {
		return ICommand.ADMINISTRATOR;
	}
	
	@Override
	public Optional<String> executeCommand(String parameters, User executor, Channel channel, Message commandMessage) throws CommandSyntaxException {
		if (parameters == null || parameters.isEmpty())
			throw new CommandSyntaxException("No name has been provided!");
		try {
			boolean truncated = parameters.length() > 32;
			DiscordBot.instance.changeAccountInfo(StringEscapeUtils.escapeJson(parameters.substring(0, truncated ?
					32 : parameters.length())), "", "");
			return Optional.of("The bot username has been changed to "+parameters.substring(0, truncated ?
					32 : parameters.length())+(truncated ? " (truncated)" : ""));
		} catch (UnsupportedEncodingException | ParseException e) {
			e.printStackTrace();
		}
		return Optional.empty();
	}
}
