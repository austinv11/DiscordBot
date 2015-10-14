package com.austinv11.DiscordBot.api;

import com.austinv11.DiscordBot.DiscordBot;
import com.austinv11.DiscordBot.api.commands.ICommand;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
				e.printStackTrace();
			}
		}
		if (!commandPermsCached.contains(command.getCommand())) {
			try {
				DiscordBot.db.insert("COMMANDS", new String[]{"COMMAND", "PERMISSION_LEVEL"}, new String[]{"'"+command.getCommand()+"'",
						String.valueOf(command.getDefaultPermissionLevel())});
				commandPermsCached.add(command.getCommand());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		commands.add(command);
	}
	
	/**
	 * Gets all the currently registered commands
	 * @return The commands
	 */
	public static List<ICommand> getAllCommands() {
		return new ArrayList<>(commands);
	}
}
