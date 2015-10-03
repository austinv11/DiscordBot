package com.austinv11.DiscordBot.api.commands;

import sx.blah.discord.obj.Channel;
import sx.blah.discord.obj.Message;
import sx.blah.discord.obj.User;

import java.util.Optional;

/**
 * Interface representing a command
 */
public interface ICommand {
	
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
