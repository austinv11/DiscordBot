package com.austinv11.DiscordBot.commands;

import com.austinv11.DiscordBot.api.CommandRegistry;
import com.austinv11.DiscordBot.api.commands.CommandSyntaxException;
import com.austinv11.DiscordBot.api.commands.ICommand;
import sx.blah.discord.handle.obj.Channel;
import sx.blah.discord.handle.obj.Message;
import sx.blah.discord.handle.obj.User;

import java.sql.SQLException;
import java.util.Optional;

public class RemoveCommand implements ICommand {
	
	@Override
	public String getCommand() {
		return "remove";
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
		return getCommand()+" [alias to remove]";
	}
	
	@Override
	public String getHelpMessage() {
		return "Removes the specified alias";
	}
	
	@Override
	public int getDefaultPermissionLevel() {
		return ICommand.ADMINISTRATOR;
	}

	@Override
	public boolean isConsoleExecutionAllowed() {
		return true;
	}
	
	@Override
	public Optional<String> executeCommand(String parameters, User executor, Channel channel, Message commandMessage) throws CommandSyntaxException {
		if (parameters == null || parameters.isEmpty())
			throw new CommandSyntaxException("No alias provided!");
		
		try {
			CommandRegistry.removeAlias(parameters);
		} catch (SQLException e) {
			throw new CommandSyntaxException("There was an error removing the alias, does it exist?");
		}
		return Optional.of("Alias ```"+parameters+"``` successfully removed");
	}
}
