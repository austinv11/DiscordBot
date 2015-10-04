package com.austinv11.DiscordBot.commands;

import com.austinv11.DiscordBot.DiscordBot;
import com.austinv11.DiscordBot.api.EventBus;
import com.austinv11.DiscordBot.api.commands.CommandSyntaxException;
import com.austinv11.DiscordBot.api.commands.ICommand;
import com.austinv11.DiscordBot.reference.Config;
import sx.blah.discord.obj.Channel;
import sx.blah.discord.obj.Message;
import sx.blah.discord.obj.User;

import java.util.List;
import java.util.Optional;
import java.util.TreeSet;

public class HelpCommand implements ICommand {
	
	@Override
	public String getCommand() {
		return "help";
	}
	
	@Override
	public String[] getAliases() {
		return new String[]{"h", "?"};
	}
	
	@Override
	public boolean removesCommandMessage() {
		return true;
	}
	
	@Override
	public String getUsage() {
		return getCommand()+" [optional:command]";
	}
	
	@Override
	public String getHelpMessage() {
		return "The help command will list all commands available if no argument is passed or it will provide specific command help if a valid command is passed as an argument";
	}
	
	@Override
	public int getDefaultPermissionLevel() {
		return ICommand.ANYONE;
	}
	
	@Override
	public Optional<String> executeCommand(String parameters, User executor, Channel channel, Message commandMessage) throws CommandSyntaxException {
		String result = "";
		if (parameters == null || parameters.isEmpty()) {
			List<ICommand> commands = EventBus.getAllCommands();
			result += commands.size()+" commands found:\n";
			TreeSet<String> commandNames = new TreeSet<>();
			for (ICommand command : commands)
				commandNames.add(command.getCommand());
			for (String commandName : commandNames)
				result += "*"+commandName+"\n";
			result += "For help about a specific command run the command '"+Config.commandDiscriminator+"help [command]'";
		} else {
			List<ICommand> commands = EventBus.getAllCommands();
			for (ICommand command : commands) {
				if (DiscordBot.doesCommandMatch(command, Config.commandDiscriminator+parameters)) {
					result += "Help page for command '"+command.getCommand()+"':\n";
					result += "Permission level: "+command.getPermissionLevel();
					result += command.getHelpMessage()+"\n";
					result += "Aliases: ";
					for (String alias : command.getAliases())
						result += alias+", ";
					result = result.substring(0, result.length()-2)+"\n";
					result += "Usage: "+Config.commandDiscriminator+command.getUsage();
					break;
				}
			}
			if (result.isEmpty()) {
				result += "No commands found for the query '"+parameters+"', run '"+Config.commandDiscriminator+"help' to list all available commands";
			}
		}
		return Optional.ofNullable(result.isEmpty() ? null : result);
	}
}
