package com.austinv11.DiscordBot;

import com.austinv11.DiscordBot.api.EventBus;
import com.austinv11.DiscordBot.api.commands.CommandSyntaxException;
import com.austinv11.DiscordBot.api.commands.ICommand;
import com.austinv11.DiscordBot.api.events.MessageEvent;
import com.austinv11.DiscordBot.api.events.PresenceChangeEvent;
import com.austinv11.DiscordBot.api.events.StartTypingEvent;
import com.austinv11.DiscordBot.commands.*;
import com.austinv11.DiscordBot.handler.BaseHandler;
import com.austinv11.DiscordBot.reference.Config;
import org.apache.commons.lang3.StringEscapeUtils;
import org.java_websocket.handshake.ServerHandshake;
import org.json.simple.parser.ParseException;
import sx.blah.discord.DiscordClient;
import sx.blah.discord.obj.*;

import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;
import java.io.*;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class DiscordBot extends DiscordClient {
	
	public static long startTime;
	public static DiscordBot instance;
	public static ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
	private static String[] credentials;
	private static HashMap<String, HashMap<String, Message>> messageCache = new HashMap<>(); //TODO: Optimize
	private static HashMap<String, Long> timeSinceLastMessage = new HashMap<>();
	private static HashMap<String, Integer> messageCounter = new HashMap<>();
	private static HashMap<String, Long> cooldown = new HashMap<>();
	
	public DiscordBot(String email, String password) throws URISyntaxException, IOException, ParseException {
		super(email, password);
	}
	
	@Override
	public void connect() {
		super.connect();
	}
	
	@Override
	public void onOpen(ServerHandshake serverHandshake) {
		super.onOpen(serverHandshake);
	}
	
	@Override
	public void onMessage(String message) {
		super.onMessage(message);
	}
	
	@Override
	public void onClose(int i, String s, boolean b) {
		super.onClose(i, s, b);
	}
	
	@Override
	public void onError(Exception e) {
		super.onError(e);
	}
	
	@Override
	public void onMessageReceive(Message message) {
		super.onMessageReceive(message);
		System.out.println("["+message.getTimestamp().toString()+"]"+message.getAuthor().getName()+"("+
				getChannelByID(message.getChannelID()).getName()+")"+": "+message.getContent());
		long currentTime = System.currentTimeMillis();
		if (cooldown.containsKey(message.getAuthor().getID())) {
			if (currentTime - cooldown.get(message.getAuthor().getID()) >= (long) (Config.cooldownTime*1000)) {
				cooldown.remove(message.getAuthor().getID());
			} else {
				try {
					deleteMessage(message.getMessageID(), message.getChannelID());
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
			if (messageCount > Config.maxUserMessagesPerSecond+1) {
				messageCounter.put(message.getAuthor().getID(), 0);
				cooldown.put(message.getAuthor().getID(), currentTime);
				try {
					sendMessage("User @"+message.getAuthor().getName()+" has exceeded the maximum of "+Config.maxUserMessagesPerSecond+
							" messages per second, he has been muted for "+Config.cooldownTime+" seconds", message.getChannelID(), 
							message.getAuthor().getID());
				} catch (IOException | ParseException e) {
					e.printStackTrace();
				}
				return;
			}
		}
		messageCounter.put(message.getAuthor().getID(), messageCount);
		if (!message.getAuthor().getID().equals(getOurUser().getID())) {
			if (message.getContent().startsWith(String.valueOf(Config.commandDiscriminator))) {
				try {
					for (ICommand command : EventBus.getAllCommands()) {
						if (doesCommandMatch(command, message.getContent())) {
							try {
								String params = message.getContent().contains(" ") ? message.getContent().replaceFirst(
										message.getContent().split(" ")[0]+" ", "") : "";
								Optional<String> result = command.executeCommand(params, message.getAuthor(),
										getChannelByID(message.getChannelID()), message);
								if (result != null && result.isPresent()) {
									sendMessage(result.get(), message.getChannelID());
								}
								if (command.removesCommandMessage()) {
									deleteMessage(message.getMessageID(), message.getChannelID());
								}
							} catch (CommandSyntaxException e) {
								sendMessage("Error: "+e.errorMessage+"\nNeed help? Read the help page" +
										" for this command by doing '"+Config.commandDiscriminator+"help "+command.getCommand()+"'",
										message.getChannelID());
							}
							return;
						}
					}
					sendMessage("No matching commands found! Run '"+Config.commandDiscriminator+
							"help' to list all available commands", message.getChannelID());
				} catch (ParseException | IOException e) {
					e.printStackTrace();
				}
			} else {
				EventBus.postEvent(new MessageEvent.MessageReceivedEvent(message));
				messageCache.get(message.getChannelID()).put(message.getMessageID(), new Message(message.getMessageID(), message.getContent(),
						message.getAuthor(), message.getChannelID(), message.getMentionedIDs(), message.getTimestamp()));
			}
		}
	}
	
	@Override
	public void onMessageSend(Message message) {
		super.onMessageSend(message);
		System.out.println("["+message.getTimestamp().toString()+"]"+message.getAuthor().getName()+"("+
				getChannelByID(message.getChannelID()).getName()+")"+": "+message.getContent());
		EventBus.postEvent(new MessageEvent.MessageSentEvent(message));
		messageCache.get(message.getChannelID()).put(message.getMessageID(), new Message(message.getMessageID(), message.getContent(),
				message.getAuthor(), message.getChannelID(), message.getMentionedIDs(), message.getTimestamp()));
	}
	
	@Override
	public void onMentioned(Message message) {
		super.onMentioned(message);
		EventBus.postEvent(new MessageEvent.MentionedEvent(message));
	}
	
	@Override
	public void onStartTyping(String userID, String channelID) {
		super.onStartTyping(userID, channelID);
		EventBus.postEvent(new StartTypingEvent(getUserByID(userID), getChannelByID(channelID)));
	}
	
	@Override
	public void onPresenceChange(User user, String presence) {
		super.onPresenceChange(user, presence);
		EventBus.postEvent(new PresenceChangeEvent(user, presence));
	}
	
	@Override
	public void onMessageUpdate(Message message) {
		super.onMessageUpdate(message);//TODO get old message
		Message oldMessage = messageCache.get(message.getChannelID()).get(message.getMessageID());
		EventBus.postEvent(new MessageEvent.MessageUpdateEvent(oldMessage, message));
		messageCache.get(message.getChannelID()).put(message.getMessageID(), new Message(message.getMessageID(), message.getContent(), 
				message.getAuthor(), message.getChannelID(), message.getMentionedIDs(), message.getTimestamp()));
	}
	
	@Override
	public void onMessageDelete(String messageID, String channelID) { //TODO get old message
		super.onMessageDelete(messageID, channelID);
		Message oldMessage = messageCache.get(channelID).get(messageID);
		EventBus.postEvent(new MessageEvent.MessageDeleteEvent(oldMessage));
		messageCache.get(channelID).remove(messageID);
	}
	
	@Override
	public Message sendMessage(String content, String channelID, String... mentions) throws IOException, ParseException {
		return super.sendMessage(StringEscapeUtils.escapeJson(content), channelID, mentions);
	}
	
	public static void main(String[] args) {
		try {
			startTime = System.currentTimeMillis();
			EventBus.registerHandler(BaseHandler.class);
			EventBus.registerCommand(new HelpCommand());
			EventBus.registerCommand(new EvaluateCommand());
			EventBus.registerCommand(new UptimeCommand());
			EventBus.registerCommand(new NameCommand());
			EventBus.registerCommand(new MeCommand());
			EventBus.registerCommand(new RestartCommand());
			credentials = getCredentials();
			instance = new DiscordBot(credentials[0], credentials[1]);
			for (ScriptEngineFactory factory : scriptEngineManager.getEngineFactories()) {
				System.out.println("Loaded script engine '"+factory.getEngineName()+"' v"+factory.getEngineVersion()+
						" for language: "+factory.getLanguageName()+" v"+factory.getLanguageVersion());
			}
			new Thread() {
				@Override
				public void run() {
					try {
						synchronized (this) {
							this.wait(2000L); //FIXME damn race conditions
							User user = instance.getOurUser();
							System.out.println("Logged in as "+user.getName()+" with user id "+user.getID()+", this user is "+user.getPresence());
							System.out.println("This user's avatar ("+user.getAvatar()+") is located at the url "+user.getAvatarURL());
							try {
								if (!credentials[2].equals("null")) {
									if (credentials[2].contains("https://discord.gg/")
											|| credentials[2].contains("http://discord.gg/")) {
										String invite = credentials[2].split(".gg/")[1].split(" ")[0];
										System.out.println("Received invite code "+invite);
										Invite invite1 = instance.acceptInvite(invite);
										if (null != invite1) {
											instance.sendMessage(String.format("Hello, %s! I am a bot and I was invited to the %s channel by @%s.",
													invite1.getGuildName(), invite1.getChannelName(), invite1.getInviterUsername()), invite1.getChannelID(), invite1.getInviterID());
										}
										System.out.println("Accepted initial invitation");
									} else {
										System.out.println("Invite url "+credentials[2]+" is invalid!");
									}
								}
							} catch (NullPointerException exception) {
								//TODO: remove, "inviter" in the invite object is always null for some reason
							} catch (Exception e) {
								e.printStackTrace();
							}
							List<Guild> guilds = instance.getGuildList();
							System.out.println("Guilds connected to:");
							for (Guild guild : guilds) {
								System.out.println("*'"+guild.getName()+"' with id "+guild.getID()+" with channels:");
								for (Channel channel : guild.getChannels()) {
									if (!messageCache.containsKey(channel.getChannelID())) {
										messageCache.put(channel.getChannelID(), formMessageCache(channel.getMessages()));
									}
									System.out.println("\t*'"+channel.getName()+"' with id "+channel.getChannelID());
								}
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}.start();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("There was an error initializing the bot, rebuilding the credentials.txt");
			File file = new File("./credentials.txt");
			try {
				file.createNewFile();
				PrintStream writer = new PrintStream(file);
				writer.println("DO NOT USE YOUR PERSONAL ACCOUNT! THIS ISN'T SECURE");
				writer.println("email=");
				writer.println("password=");
				writer.println("server_invite=null");
				writer.flush();
				writer.close();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}
	
	//NOTE: DO NOT USE THIS WITH YOUR PERSONAL ACCOUNT! THIS ISN'T SECURE.
	private static String[] getCredentials() throws Exception {
		File file = new File("./credentials.txt");
		if (!file.exists()) {
			throw new FileNotFoundException();
		}
		String[] credentials = new String[3];
		FileReader reader = new FileReader(file);
		BufferedReader bufferedReader = new BufferedReader(reader);
		bufferedReader.readLine();
		credentials[0] = bufferedReader.readLine().replaceFirst("email=", "");
		credentials[1] = bufferedReader.readLine().replaceFirst("password=", "");
		credentials[2] = bufferedReader.readLine().replaceFirst("server_invite=", "");
		bufferedReader.close();
		return credentials;
	}
	
	private static HashMap<String, Message> formMessageCache(List<Message> messageList) {
		HashMap<String, Message> cache = new HashMap<>();
		for (Message message : messageList)
			cache.put(message.getMessageID(), new Message(message.getMessageID(), message.getContent(), message.getAuthor(), 
					message.getChannelID(), message.getMentionedIDs(), message.getTimestamp()));//Instantiation makes sure the base api doesn't break anything
		return cache;
	}
	
	public static boolean doesCommandMatch(ICommand command, String message) {
		if (message.startsWith(String.valueOf(Config.commandDiscriminator))) {
			message = message.replaceFirst(String.valueOf(Config.commandDiscriminator), "");
			String commandName = message.contains(" ") ? message.split(" ")[0] : message;
			if (command.getCommand().equals(commandName))
				return true;
			for (String alias : command.getAliases())
				if (message.equals(alias))
					return true;
		}
		return false;
	}
	
	public static void restart() {
		System.out.println("Restarting the bot...");
		instance.close();
		main(new String[0]);
	}
}
