package com.austinv11.DiscordBot.api;

import com.austinv11.DiscordBot.DiscordBot;
import com.austinv11.DiscordBot.api.commands.CommandSyntaxException;
import com.austinv11.DiscordBot.api.commands.ICommand;
import com.austinv11.DiscordBot.handler.Logger;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;
import sx.blah.discord.handle.obj.Channel;
import sx.blah.discord.handle.obj.Message;
import sx.blah.discord.handle.obj.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

/**
 * Register commands here
 */
public class CommandRegistry {
	
	private static List<ICommand> commands = new ArrayList<>();
	private static List<String> commandPermsCached = new ArrayList<>();
	
	/**
	 * Registers a command
	 * @param command The command to register
	 */
	public static void registerCommand(ICommand command) {
		if (commandPermsCached.isEmpty()) {
			try {
				ResultSet set = DiscordBot.db.openSelect("COMMANDS");
				while (set.next()) {
					commandPermsCached.add(set.getString("COMMAND"));
				}
				DiscordBot.db.closeSelect();
			} catch (SQLException e) {
				Logger.log(e);
			}
		}
		if (!commandPermsCached.contains(command.getCommand())) {
			try {
				DiscordBot.db.insert("COMMANDS", new String[]{"COMMAND", "PERMISSION_LEVEL"}, new String[]{"'"+command.getCommand()+"'",
						String.valueOf(command.getDefaultPermissionLevel())});
				commandPermsCached.add(command.getCommand());
			} catch (SQLException e) {
				Logger.log(e);
			}
		}
		commands.add(command);
	}
	
	/**
	 * Registers an alias for a command sequence
	 * @param alias The alias
	 * @param commandSequence The string message to emulate
	 */
	public static void registerAlias(String alias, String commandSequence) throws NullPointerException {
		try {
			boolean exists = false;
			boolean needsUpdate = false;
			ResultSet set = DiscordBot.db.openSelect("ALIASES");
			while (set.next()) {
				if (set.getString("NAME").equals(alias)) {
					exists = true;
					needsUpdate = !set.getString("COMMAND").equals(commandSequence);
					break;
				}
			}
			DiscordBot.db.closeSelect();
			if (exists && needsUpdate) {
				HashMap<String, String> key = new HashMap<>();
				key.put("COMMAND", commandSequence);
				DiscordBot.db.update("ALIASES", "NAME", alias, key);
			} else if (!exists) {
				DiscordBot.db.insert("ALIASES", new String[]{"NAME", "COMMAND"}, new String[]{"'"+alias+"'", "'"+commandSequence+"'"});
			}
			commands.add(new GhostCommand(alias, commandSequence));
		} catch (SQLException e) {
			Logger.log(e);
		}
	}
	
	/**
	 * Gets all the currently registered commands
	 * @return The commands
	 */
	public static List<ICommand> getAllCommands() {
		return new ArrayList<>(commands);
	}
	
	/**
	 * Gets the command for a given name
	 * @param sequence The sequence of words that can invoke a command
	 * @return The command (or null if not found)
	 */
	public static ICommand getCommandForMessage(String sequence) {
		for (ICommand command : commands) {
			if (DiscordBot.doesCommandMatch(command, sequence) != null) {
				return command;
			}
		}
		return null;
	}
	
	public static class GhostCommand implements ICommand {
		
		String alias;
		String toExecute;
		ICommand command;
		
		public GhostCommand(String alias, String toExecute) throws NullPointerException {
			this.alias = alias;
			this.toExecute = toExecute;
			this.command = getCommandForMessage(toExecute);
			if (command == null)
				throw new NullPointerException("No appropriate command found!");
		}
		
		@Override
		public String getCommand() {
			return alias;
		}
		
		@Override
		public String[] getAliases() {
			return new String[0];
		}
		
		@Override
		public boolean removesCommandMessage() {
			return command.removesCommandMessage();
		}
		
		@Override
		public String getUsage() {
			return getCommand();
		}
		
		@Override
		public String getHelpMessage() {
			return "This command represents an alias for ```"+toExecute+"```";
		}
		
		@Override
		public int getDefaultPermissionLevel() {
			return command.getDefaultPermissionLevel();
		}
		
		@Override
		public int getPermissionLevel() {
			return command.getPermissionLevel();
		}
		
		@Override
		public Optional<String> executeCommand(String parameters, User executor, Channel channel, Message commandMessage) throws CommandSyntaxException {
			DiscordBot.instance.getDispatcher().dispatch(new MessageReceivedEvent(new Message(commandMessage.getID(), 
					DiscordBot.CONFIG.commandDiscriminator+toExecute, executor, channel, commandMessage.getTimestamp())));
			
			return Optional.empty();
		}
	}
}
