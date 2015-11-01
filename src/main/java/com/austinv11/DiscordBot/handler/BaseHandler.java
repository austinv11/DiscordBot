package com.austinv11.DiscordBot.handler;

import com.austinv11.DiscordBot.DiscordBot;
import com.austinv11.DiscordBot.api.CommandRegistry;
import com.austinv11.DiscordBot.api.commands.CommandSyntaxException;
import com.austinv11.DiscordBot.api.commands.ICommand;
import com.austinv11.DiscordBot.web.FrontEnd;
import org.json.simple.parser.ParseException;
import sx.blah.discord.handle.IEvent;
import sx.blah.discord.handle.impl.events.*;
import sx.blah.discord.handle.obj.Channel;
import sx.blah.discord.handle.obj.Invite;
import sx.blah.discord.handle.obj.Message;

import java.io.IOException;
import java.util.HashMap;
import java.util.Optional;

public class BaseHandler {
	
	private static HashMap<String, Long> timeSinceLastMessage = new HashMap<>();
	private static HashMap<String, Integer> messageCounter = new HashMap<>();
	private static HashMap<String, Long> cooldown = new HashMap<>();
	
	public static void guildCreateEvent(GuildCreateEvent event) {
		
	}
	
	public static void inviteReceivedEvent(InviteReceivedEvent event) {
		
	}
	
	public static void mentionEvent(MentionEvent event) {
		try {
			try {
				if (event.getMessage().getContent().contains("https://discord.gg/")
						|| event.getMessage().getContent().contains("http://discord.gg/")) {
					String invite = event.getMessage().getContent().split(".gg/")[1].split(" ")[0];
					System.out.println("Received invite code "+invite);
					Invite invite1 = new Invite(invite);
					Invite.InviteResponse response = invite1.accept();
					DiscordBot.sendMessage(String.format("Hello, %s! I am a bot and I was invited to the %s channel",
							response.getGuildName(), response.getChannelName()), response.getChannelID());
					}
			} catch (NullPointerException exception) {}//TODO: remove, "inviter" in the invite object is always null for some reason
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void messageDeleteEvent(MessageDeleteEvent event) {
		//Remove deleted message from cache
		DiscordBot.messageCache.get(event.getMessage().getChannelID()).remove(event.getMessage().getMessageID());
	}
	
	private static void checkSpamFilter(Message message) {
		if (DiscordBot.CONFIG.enableSpamFilter) {
			long currentTime = System.currentTimeMillis();
			if (cooldown.containsKey(message.getAuthor().getID())) {
				if (currentTime-cooldown.get(message.getAuthor().getID()) >= (long) (DiscordBot.CONFIG.cooldownTime*1000)) {
					cooldown.remove(message.getAuthor().getID());
				} else {
					try {
						DiscordBot.instance.deleteMessage(message.getMessageID(), message.getChannelID());
					} catch (IOException e) {
						e.printStackTrace();
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
					try {
						DiscordBot.sendMessage("User @"+message.getAuthor().getName()+" has exceeded the maximum of "+DiscordBot.CONFIG.maxUserMessagesPerSecond+
										" messages per second, he has been muted for "+DiscordBot.CONFIG.cooldownTime+" seconds", message.getChannelID(),
								message.getAuthor().getID());
					} catch (IOException | ParseException e) {
						e.printStackTrace();
					}
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
									DiscordBot.sendMessage(result.get(), message.getChannelID());
								}
							} catch (CommandSyntaxException e) {
								DiscordBot.sendMessage("Error: "+e.errorMessage+"\nNeed help? Read the help page"+
												" for this command by doing '"+DiscordBot.CONFIG.commandDiscriminator+"help "+command.getCommand()+"'",
										message.getChannelID());
							}
							if (command.removesCommandMessage()) {
								DiscordBot.instance.deleteMessage(message.getMessageID(), message.getChannelID());
							}
						} else {
							DiscordBot.sendMessage("Error: You don't have permission to use this command!", message.getChannelID());
						}
						return true;
					}
				}
				DiscordBot.sendMessage("No matching commands found! Run '"+DiscordBot.CONFIG.commandDiscriminator+
						"help' to list all available commands", message.getChannelID());
				return true;
			} catch (ParseException | IOException e) {
				e.printStackTrace();
			}
		}
		return false; //Command not detected
	}
	
	public static void messageReceivedEvent(MessageReceivedEvent event) {
		Message message = event.getMessage();
		Channel channel = DiscordBot.instance.getChannelByID(message.getChannelID());
		if (channel == null)
			return;
		
		//Logging messages
		System.out.println("["+message.getTimestamp().toString()+"]"+message.getAuthor().getName()+"("+
				channel.getName()+")"+": "+message.getContent());
		FrontEnd.console.add(message);
		
		checkSpamFilter(message);
		
		//Make sure the user isn't the bot
		if (!message.getAuthor().getID().equals(DiscordBot.instance.getOurUser().getID())) {
			if (!checkForCommand(channel, message)){
				//Not a command, cache it and check for table disrespect
				DiscordBot.messageCache.get(message.getChannelID()).put(message.getMessageID(), new Message(message.getMessageID(), message.getContent(),
						message.getAuthor(), message.getChannelID(), message.getMentionedIDs(), message.getTimestamp()));
				
				if (DiscordBot.CONFIG.enableRespectTables) {
					if (message.getContent().contains("(╯°□°）╯︵ ┻━┻")) {
						try {
							//Please respect tables
							DiscordBot.sendMessage("┬─┬ノ(ಠ_ಠノ)\nPlease respect tables", message.getChannelID());
						} catch (IOException | ParseException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
	}
	
	public static void messageSendEvent(MessageSendEvent event) {
		Message message = event.getMessage();
		Channel channel = DiscordBot.instance.getChannelByID(message.getChannelID());
		if (channel == null)
			return;
		
		//Logging messages
		System.out.println("["+message.getTimestamp().toString()+"]"+message.getAuthor().getName()+"("+
				channel.getName()+")"+": "+message.getContent());
		FrontEnd.console.add(message);
		
		//Caching it
		DiscordBot.messageCache.get(message.getChannelID()).put(message.getMessageID(), new Message(message.getMessageID(), message.getContent(),
				message.getAuthor(), message.getChannelID(), message.getMentionedIDs(), message.getTimestamp()));
	}
	
	public static void readyEvent(ReadyEvent event) {
		//Init
		DiscordBot.ready();
	}
	
	public static void receive(IEvent event) { //TODO: Remember to update
		if (event instanceof GuildCreateEvent) {
			guildCreateEvent((GuildCreateEvent) event);
		} else if (event instanceof InviteReceivedEvent) {
			inviteReceivedEvent((InviteReceivedEvent) event);
		} else if (event instanceof MentionEvent) {
			mentionEvent((MentionEvent) event);
		} else if (event instanceof MessageDeleteEvent) {
			messageDeleteEvent((MessageDeleteEvent) event);
		} else if (event instanceof MessageReceivedEvent) {
			messageReceivedEvent((MessageReceivedEvent) event);
		} else if (event instanceof MessageSendEvent) {
			messageSendEvent((MessageSendEvent) event);
		} else if (event instanceof ReadyEvent) {
			readyEvent((ReadyEvent) event);
		}
	}
}
