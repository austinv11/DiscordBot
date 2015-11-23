package com.austinv11.DiscordBot.handler;

import com.austinv11.DiscordBot.DiscordBot;
import com.austinv11.DiscordBot.api.CommandRegistry;
import com.austinv11.DiscordBot.api.commands.CommandSyntaxException;
import com.austinv11.DiscordBot.api.commands.ICommand;
import com.austinv11.DiscordBot.web.FrontEnd;
import sx.blah.discord.handle.impl.EventSubscriber;
import sx.blah.discord.handle.impl.events.*;
import sx.blah.discord.handle.obj.Channel;
import sx.blah.discord.handle.obj.Message;
import sx.blah.discord.util.MessageBuilder;

import java.io.IOException;
import java.util.HashMap;
import java.util.Optional;

public class BaseHandler {
	
	private static HashMap<String, Long> timeSinceLastMessage = new HashMap<>();
	private static HashMap<String, Integer> messageCounter = new HashMap<>();
	private static HashMap<String, Long> cooldown = new HashMap<>();
	
	@EventSubscriber
	public void guildCreateEvent(GuildCreateEvent event) {
		
	}
	
	@EventSubscriber
	public void inviteReceivedEvent(InviteReceivedEvent event) {
		
	}
	
	@EventSubscriber
	public void mentionEvent(MentionEvent event) {
		try {
			if (event.getMessage().getContent().contains("https://discord.gg/")
					|| event.getMessage().getContent().contains("http://discord.gg/")) {
				DiscordBot.acceptInvite(event.getMessage().getContent());
				}
		} catch (Exception e) {
			Logger.log(e);
		}
	}
	
	@EventSubscriber
	public void messageDeleteEvent(MessageDeleteEvent event) {
		//Remove deleted message from cache
		DiscordBot.messageCache.get(event.getMessage().getChannel().getID()).remove(event.getMessage().getID());
	}
	
	private static void checkSpamFilter(Message message) {
		if (DiscordBot.CONFIG.enableSpamFilter) {
			long currentTime = System.currentTimeMillis();
			if (cooldown.containsKey(message.getAuthor().getID())) {
				if (currentTime-cooldown.get(message.getAuthor().getID()) >= (long) (DiscordBot.CONFIG.cooldownTime*1000)) {
					cooldown.remove(message.getAuthor().getID());
				} else {
					try {
						DiscordBot.instance.deleteMessage(message.getID(), message.getChannel().getID());
					} catch (IOException e) {
						Logger.log(e);
					}
					return;
				}
			}
			long lastMessageTime = timeSinceLastMessage.containsKey(message.getAuthor().getID()) ? timeSinceLastMessage.get(message.getAuthor().getID()) : 0L;
			int messageCount = messageCounter.containsKey(message.getAuthor().getID()) ? messageCounter.get(message.getAuthor().getID()) : 0;
			messageCount++;
			timeSinceLastMessage.put(message.getAuthor().getID(), currentTime);
			if (currentTime-lastMessageTime < 1000) {
				if (messageCount >= DiscordBot.CONFIG.maxUserMessagesPerSecond) {
					messageCounter.put(message.getAuthor().getID(), 0);
					cooldown.put(message.getAuthor().getID(), currentTime);
					new MessageBuilder().withContent("User "+message.getAuthor().mention()+" has exceeded the maximum of "+DiscordBot.CONFIG.maxUserMessagesPerSecond+
							" messages per second, he has been muted for "+DiscordBot.CONFIG.cooldownTime+" seconds").withChannel(message.getChannel()).build();
					return;
				}
			}
			messageCounter.put(message.getAuthor().getID(), messageCount);
		}
	}
	
	private static boolean checkForCommand(Channel channel, Message message) {
		if (message.getContent().startsWith(String.valueOf(DiscordBot.CONFIG.commandDiscriminator))) {
			try {
				for (ICommand command : CommandRegistry.getAllCommands()) {
					String commandPrefix = DiscordBot.doesCommandMatch(command, message.getContent());
					if (commandPrefix != null) {
						int commandLevel = command.getPermissionLevel();
						if (DiscordBot.getUserPermissionLevel(message.getAuthor()) >= commandLevel) {
							try {
								String params = message.getContent().contains(" ") ? message.getContent().replaceFirst(
										message.getContent().split(" ")[0]+" ", "") : "";
								Optional<String> result = command.executeCommand(params, message.getAuthor(),
										channel, message);
								if (result != null && result.isPresent()) {
									new MessageBuilder().withContent(result.get()).withChannel(message.getChannel()).build();
								}
							} catch (CommandSyntaxException e) {
								new MessageBuilder().withContent("Error: "+e.errorMessage+"\nNeed help? Read the help " +
										"page for this command by doing ").appendContent(DiscordBot.CONFIG.commandDiscriminator+
										"help "+command.getCommand(), MessageBuilder.Styles.CODE).withChannel(message.getChannel()).build();
							}
							if (command.removesCommandMessage()) {
								DiscordBot.instance.deleteMessage(message.getID(), message.getChannel().getID());
							}
						} else {
							new MessageBuilder().withContent("Error: You don't have permission to use this command!")
									.withChannel(message.getChannel()).build();
						}
						return true;
					}
				}
				new MessageBuilder().withContent("No matching commands found! Run '"+DiscordBot.CONFIG.commandDiscriminator+
						"help' to list all available commands").withChannel(message.getChannel()).build();
				return true;
			} catch (IOException e) {
				Logger.log(e);
			}
		}
		return false; //Command not detected
	}
	
	@EventSubscriber
	public void messageReceivedEvent(MessageReceivedEvent event) {
		Message message = event.getMessage();
		Channel channel = message.getChannel();
		if (channel == null)
			return;
		
		//Logging messages
		Logger.log(message.getAuthor().getName()+"("+channel.getName()+")"+": "+message.getContent(), message.getTimestamp());
		FrontEnd.console.add(message);
		
		checkSpamFilter(message);
		
		//Make sure the user isn't the bot
		if (!message.getAuthor().getID().equals(DiscordBot.instance.getOurUser().getID())) {
			if (!checkForCommand(channel, message)){
				//Not a command, cache it and check for table disrespect
				DiscordBot.messageCache.get(message.getChannel().getID()).put(message.getID(), new Message(message.getID(), 
						message.getContent(), message.getAuthor(), message.getChannel(), message.getTimestamp()));
				
				if (DiscordBot.CONFIG.enableRespectTables) {
					if (message.getContent().contains("(╯°□°）╯︵ ┻━┻")) {
						//Please respect tables
						new MessageBuilder().withContent("┬─┬ノ(ಠ_ಠノ)\nPlease respect tables")
								.withChannel(message.getChannel()).build();
					}
				}
			}
		}
	}
	
	@EventSubscriber
	public void messageSendEvent(MessageSendEvent event) {
		Message message = event.getMessage();
		Channel channel = message.getChannel();
		if (channel == null)
			return;
		
		//Logging messages
		Logger.log(message.getAuthor().getName()+"("+channel.getName()+")"+": "+message.getContent(), message.getTimestamp());
		FrontEnd.console.add(message);
		
		//Caching it
		DiscordBot.messageCache.get(message.getChannel().getID()).put(message.getID(), new Message(message.getID(), message.getContent(),
				message.getAuthor(), message.getChannel(), message.getTimestamp()));
	}
	
	@EventSubscriber
	public void readyEvent(ReadyEvent event) {
		//Init
		DiscordBot.ready();
	}
	
	@EventSubscriber
	public void messageUpdateEvent(MessageUpdateEvent event) {
		
	}
	
	@EventSubscriber
	public void prescenseUpdateEvent(PresenceUpdateEvent event) {
		
	}
	
	@EventSubscriber
	public void typingEvent(TypingEvent event) {
		
	}
	
	@EventSubscriber
	public void userJoinEvent(UserJoinEvent event) {
		
	}
	
	@EventSubscriber
	public void userLeaveEvent(UserLeaveEvent event) {
		
	}
	
	@EventSubscriber
	public void channelCreateEvent(ChannelCreateEvent event) {
		
	}
	
	@EventSubscriber
	public void channelDeleteEvent(ChannelDeleteEvent event) {
		
	}
	
	@EventSubscriber
	public void guildLeaveEvent(GuildLeaveEvent event) {
		
	}
}
