package com.austinv11.DiscordBot.commands;

import com.austinv11.DiscordBot.DiscordBot;
import com.austinv11.DiscordBot.api.CommandRegistry;
import com.austinv11.DiscordBot.api.commands.CommandSyntaxException;
import com.austinv11.DiscordBot.api.commands.ICommand;
import sx.blah.discord.handle.obj.Channel;
import sx.blah.discord.handle.obj.Guild;
import sx.blah.discord.handle.obj.Message;
import sx.blah.discord.handle.obj.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Optional;

public class PermissionsCommand implements ICommand {
	
	@Override
	public String getCommand() {
		return "permissions";
	}
	
	@Override
	public String[] getAliases() {
		return new String[]{"perms", "permission", "perm", "rank", "group"};
	}
	
	@Override
	public boolean removesCommandMessage() {
		return false;
	}
	
	@Override
	public String getUsage() {
		return getCommand()+" user/rank/command [user/rank/command] [optional:permission level]";
	}
	
	@Override
	public String getHelpMessage() {
		return "Remaps or retrieves permissions for something";
	}
	
	@Override
	public int getDefaultPermissionLevel() {
		return ICommand.DEFAULT;
	}
	
	@Override
	public Optional<String> executeCommand(String parameters, User executor, Channel channel, Message commandMessage) throws CommandSyntaxException {
		String result = "No results found for your query!";
		if (parameters != null && !parameters.isEmpty() && parameters.split(" ").length > 1) {
			String command = parameters.split(" ")[0];
			String thing = parameters.split(" ")[1];
			Integer level = parameters.split(" ").length > 2 ? DiscordBot.getLevelForRank(parameters.split(" ")[2]) : null; //if null, retrieve level, not change
			if (command.equals("user")) {
				User user = getUser(thing);
				if (user == null)
					throw new CommandSyntaxException("User "+thing+" doesn't exist!");
				if (level == null) {
					result = "User "+thing+" has the rank "+DiscordBot.getRankForLevel(DiscordBot.getUserPermissionLevel(user));
				} else {
					int executorLevel = DiscordBot.getUserPermissionLevel(executor);
					int oldLevel = DiscordBot.getUserPermissionLevel(user);
					if (oldLevel >= executorLevel) {
						result = "You don't have a high enough permissions level to perform this action";
					} else {
						int newLevel = level >= executorLevel ? executorLevel-1 : level;
						HashMap<String, String> toUpdate = new HashMap<>();
						toUpdate.put("PERMISSION_LEVEL", String.valueOf(newLevel));
						try {
							DiscordBot.db.update("USERS", "ID", "'"+user.getID()+"'", toUpdate);
							result = "User "+thing+" now has a permission level of "+newLevel;
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}
				}
			} else if (command.equals("rank")) {
				if (level == null) {
					result = "Rank "+thing+" has the permission level "+DiscordBot.getLevelForRank(thing);
				} else {
					boolean exists = false;
					try {
						ResultSet set = DiscordBot.db.openSelect("PERMISSION_RANKS");
						while (set.next()) {
							if (set.getString("RANK").equals(thing)) {
								exists = true;
								break;
							}
						}
						DiscordBot.db.closeSelect();
						int executorLevel = DiscordBot.getUserPermissionLevel(executor);
						int oldLevel = DiscordBot.getLevelForRank(thing);
						if (oldLevel >= executorLevel) {
							result = "You don't have a high enough permissions level to perform this action";
						} else {
							int newLevel = level >= executorLevel ? executorLevel-1 : level;
							try {
								if (exists) {
									HashMap<String, String> toUpdate = new HashMap<>();
									toUpdate.put("PERMISSION_LEVEL", String.valueOf(newLevel));
									DiscordBot.db.update("PERMISSION_RANKS", "RANK", "'"+thing+"'", toUpdate);
									result = "Updated rank "+thing+" to be permission level "+newLevel;
								} else {
									DiscordBot.db.insert("PERMISSION_RANKS", new String[]{"RANK", "PERMISSION_LEVEL"}, 
											new String[]{"'"+thing+"'", String.valueOf(newLevel)});
									result = "Created rank "+thing+" with a permission level of "+newLevel;
								}
							} catch (SQLException e) {
								e.printStackTrace();
							}
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			} else if (command.equals("command")) {
				ICommand commandObj = null;
				for (ICommand command1 : CommandRegistry.getAllCommands()) {
					if (DiscordBot.doesCommandMatch(command1, DiscordBot.CONFIG.commandDiscriminator+thing) != null) {
						commandObj = command1;
					}
				}
				if (commandObj != null) {
					if (level == null) {
						result = "Command "+thing+" has the permission rank "+DiscordBot.getRankForLevel(commandObj.getPermissionLevel());
					} else {
						int executorLevel = DiscordBot.getUserPermissionLevel(executor);
						int oldLevel = commandObj.getPermissionLevel();
						if (oldLevel >= executorLevel) {
							result = "You don't have a high enough permissions level to perform this action";
						} else {
							int newLevel = level >= executorLevel ? executorLevel-1 : level;
							HashMap<String, String> toUpdate = new HashMap<>();
							toUpdate.put("PERMISSION_LEVEL", String.valueOf(newLevel));
							try {
								DiscordBot.db.update("COMMANDS", "COMMAND", "'"+thing+"'", toUpdate);
								result = "Updated command "+thing+" to have a permission level of "+DiscordBot.getRankForLevel(level);
							} catch (SQLException e) {
								e.printStackTrace();
							}
						}
					}
				} else
					throw new CommandSyntaxException("Command "+thing+" couldn't be found");
			} else {
				throw new CommandSyntaxException("You can only change the permissions for a user, rank or command");
			}
		} else {
			throw new CommandSyntaxException("No arguments provided!");
		}
		return Optional.of(result);
	}
	
	private User getUser(String userToFind) {
		for (Guild guild : DiscordBot.instance.getGuilds())
			for (User user : guild.getUsers()) {
				if (user.getName().equals(userToFind)) { //Doesn't ignore case because there are too many users
					return user;
				}
			}
		return null;
	}
}
