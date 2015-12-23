package com.austinv11.DiscordBot.commands;

import com.austinv11.DiscordBot.DiscordBot;
import com.austinv11.DiscordBot.api.commands.CommandSyntaxException;
import com.austinv11.DiscordBot.api.commands.ICommand;
import sx.blah.discord.handle.obj.Channel;
import sx.blah.discord.handle.obj.Guild;
import sx.blah.discord.handle.obj.Message;
import sx.blah.discord.handle.obj.User;
import sx.blah.discord.util.MessageBuilder;

import java.util.Optional;

public class SayCommand implements ICommand {

	@Override
	public String getCommand() {
		return "say";
	}

	@Override
	public String[] getAliases() {
		return new String[0];
	}

	@Override
	public boolean removesCommandMessage() {
		return true;
	}

	@Override
	public String getUsage() {
		return getCommand()+" [message]";
	}

	@Override
	public String getHelpMessage() {
		return "Makes the bot send a message";
	}

	@Override
	public int getDefaultPermissionLevel() {
		return ICommand.ANYONE;
	}

	@Override
	public boolean isConsoleExecutionAllowed() {
		return true;
	}

	@Override
	public Optional<String> executeCommand(String parameters, User executor, Channel channel, Message commandMessage) throws CommandSyntaxException {
		// There are no parameters, there is no message
		if (parameters == null) {
			return Optional.empty();
		}

		String[] eachPar = parameters.split(" ");
		// The channel is null, meaning this command was executed from console. We need to find the correct channel
		if (channel == null) {
			// If the command was executed from console and there is no channel parameter, the command cannot be properly executed
			if (eachPar.length <= 1) {
				return Optional.empty();
			}
			String channelName = eachPar[0];
			// Find the channel this message needs to be sent to
			for (Guild guild : DiscordBot.instance.getGuilds()) {
				for (Channel channel1 : guild.getChannels()) {
					if (channel1.getName().equalsIgnoreCase(channelName)) {
						channel = channel1;
					}
				}
			}

			// No matching channel was found
			if (channel == null) {
				return Optional.of("Channel not found.");
			}

			new MessageBuilder().withContent(parameters.substring(parameters.indexOf(' '))).withChannel(channel).build();
			return Optional.empty();
		} else {
			return Optional.of(parameters);
		}
	}
}
