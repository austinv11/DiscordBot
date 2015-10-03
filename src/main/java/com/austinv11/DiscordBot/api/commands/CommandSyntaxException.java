package com.austinv11.DiscordBot.api.commands;

/**
 * Represents a command syntax error
 */
public class CommandSyntaxException extends Exception {
	
	public String errorMessage;
	
	private CommandSyntaxException() {
	}
	
	/**
	 * @param message The error message for the user
	 */
	public CommandSyntaxException(String message) {
		super(message);
		this.errorMessage = message;
	}
	
	private CommandSyntaxException(String message, Throwable cause) {
		super(message, cause);
	}
	
	private CommandSyntaxException(Throwable cause) {
		super(cause);
	}
	
	private CommandSyntaxException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
