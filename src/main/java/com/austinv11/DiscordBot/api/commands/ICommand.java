package com.austinv11.DiscordBot.api.commands;

import com.austinv11.DiscordBot.DiscordBot;
import com.austinv11.DiscordBot.handler.Logger;
import sx.blah.discord.handle.obj.Channel;
import sx.blah.discord.handle.obj.Message;
import sx.blah.discord.handle.obj.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

/**
 * Interface representing a command
 */
public interface ICommand {
	
	/**
	 * Literally anyone will be able to use the command
	 * This is also the permission level for those banned from using commands
	 */
	public static int ANYONE = -1;
	/**
	 * The default permission level
	 */
	public static int DEFAULT = 0;
	/**
	 * A breakpoint for administrators, but anything above 0 will require special permissions
	 */
	public static int ADMINISTRATOR = 10;
	/**
	 * Only the owner will be able to use the command
	 */
	public static int OWNER = Integer.MAX_VALUE;
	
	/**
	 * Gets the name of the command
	 * @return The name of the command
	 */
	public String getCommand();
	
	/**
	 * Gets any aliases for the command
	 * @return The aliases
	 */
	public String[] getAliases();
	
	/**
	 * Returns whether the message executing the command should be removed
	 * @return If true, the message sent to execute the command will be deleted
	 */
	public boolean removesCommandMessage();
	
	/**
	 * Gets the usage message for the command
	 * @return The usage message. Ex. <code>eval [language] [code snippet]</code>
	 */
	public String getUsage();
	
	/**
	 * Gets the generic help message explaining the command
	 * @return The help message. Ex, <code>The eval command executes the provided code in a sandbox and displays the results</code>
	 */
	public String getHelpMessage();
	
	/**
	 * Gets the default permission level required for this command
	 * @return Any integer between -1 and {@link Integer#MAX_VALUE}
	 */
	public int getDefaultPermissionLevel();
	
	/**
	 * Called to get the definitive permission level, permissions are meant to be able to be remapped so only override 
	 * this if you know what you are doing!
	 * @return The actual permissions level
	 */
	public default int getPermissionLevel() {
		try {
			ResultSet set = DiscordBot.db.openSelect("COMMANDS");
			while (set.next()) {
				if (set.getString("COMMAND").equals(getCommand())) {
					return set.getInt("PERMISSION_LEVEL");
				}
			}
			DiscordBot.db.closeSelect();
		} catch (SQLException e) {
			Logger.log(e);
		}
		return getDefaultPermissionLevel();
	}
	
	/**
	 * Called to execute the command
	 * @param parameters The parameters for the command
	 * @param executor The user who executed the command
	 * @param channel The channel the command was executed on
	 * @param commandMessage The message which caused the command to be executed
	 * @return The response for the command. This is optional!
	 * @throws CommandSyntaxException Throw this if the command parameters were invalid, the message provided will be used
	 * 		   as the error message
	 */
	public Optional<String> executeCommand(String parameters, User executor, Channel channel, Message commandMessage) throws CommandSyntaxException;
}
