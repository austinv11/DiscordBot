package com.austinv11.DiscordBot.handler;

import com.austinv11.DiscordBot.DiscordBot;
import com.austinv11.DiscordBot.api.CommandRegistry;
import com.austinv11.DiscordBot.api.commands.CommandSyntaxException;
import com.austinv11.DiscordBot.api.commands.ICommand;

import java.util.Optional;
import java.util.Scanner;

public class ConsoleInputHandler implements Runnable {
	public Thread thread;
	public Scanner scanner = new Scanner(System.in);

	public ConsoleInputHandler() {
		thread = new Thread(this);
	}

	@Override
	public void run() {
		while (thread.isAlive()) {
			try {
				String input = scanner.nextLine();
				Logger.log("Console Input: " + input);
				ICommand command = CommandRegistry.getCommandForMessage(input);
				if (command != null) {
					if (command.isConsoleExecutionAllowed()) {
						try {
							Optional<String> result = command.executeCommand(input.contains(" ") ? input.replaceFirst(input.split(" ")[0]+" ", "") : "", DiscordBot.instance.getOurUser(), null, null);
							if (result.get() == null) {
								Logger.log("null");
							} else {
								Logger.log(result.get());
							}
						} catch (CommandSyntaxException e) {
							e.printStackTrace();
						}
					}
				} else {
					Logger.log("Command not recognized. Type 'help' for a list of commands.");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}