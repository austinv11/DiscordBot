package com.austinv11.DiscordBot;

import com.austinv11.DiscordBot.api.CommandRegistry;
import com.austinv11.DiscordBot.api.commands.ICommand;
import com.austinv11.DiscordBot.commands.*;
import com.austinv11.DiscordBot.handler.BaseHandler;
import com.austinv11.DiscordBot.reference.Config;
import com.austinv11.DiscordBot.reference.Database;
import com.austinv11.DiscordBot.web.FrontEnd;
import org.apache.commons.lang3.StringEscapeUtils;
import org.json.simple.parser.ParseException;
import sx.blah.discord.DiscordClient;
import sx.blah.discord.handle.IListener;
import sx.blah.discord.handle.impl.events.*;
import sx.blah.discord.handle.obj.*;

import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;
import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class DiscordBot {
	
	public static long startTime;
	public static DiscordClient instance = DiscordClient.get();
	public static ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
	public static String ownerId;
	public static Database db;
	public static FrontEnd server;
	private static String[] credentials;
	public static HashMap<String, HashMap<String, Message>> messageCache = new HashMap<>(); //TODO: Optimize
	
	//Makes sure to escape all special characters
	public static Message sendMessage(String content, String channelID, String... mentions) throws IOException, ParseException {
		return instance.sendMessage(StringEscapeUtils.escapeJson(content), channelID, mentions);
	}
	
	public static void ready() {
		User user = instance.getOurUser();
		System.out.println("Logged in as "+user.getName()+" with user id "+user.getID()+", this user is "+user.getPresence());
		System.out.println("This user's avatar ("+user.getAvatar()+") is located at the url "+user.getAvatarURL());
		try {
			if (!credentials[4].equals("null")) {
				if (credentials[4].contains("https://discord.gg/")
						|| credentials[2].contains("http://discord.gg/")) {
					String invite = credentials[4].split(".gg/")[1].split(" ")[0];
					System.out.println("Received invite code "+invite);
					Invite invite1 = new Invite(invite);
					Invite.InviteResponse response = invite1.accept();
					sendMessage(String.format("Hello, %s! I am a bot and I was invited to the %s channel",
							response.getGuildName(), response.getChannelName()), response.getChannelID());
					System.out.println("Accepted initial invitation");
				} else {
					System.out.println("Invite url "+credentials[4]+" is invalid!");
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
	
	private static void registerListeners() { //TODO Remember to update
		//Holy anonymous classes batman
		instance.getDispatcher().registerListener(new IListener<GuildCreateEvent>() {
			@Override
			public void receive(GuildCreateEvent event) {
				BaseHandler.receive(event);
			}
		});
		instance.getDispatcher().registerListener(new IListener<InviteReceivedEvent>() {
			@Override
			public void receive(InviteReceivedEvent event) {
				BaseHandler.receive(event);
			}
		});
		instance.getDispatcher().registerListener(new IListener<MentionEvent>() {
			@Override
			public void receive(MentionEvent event) {
				BaseHandler.receive(event);
			}
		});
		instance.getDispatcher().registerListener(new IListener<MessageDeleteEvent>() {
			@Override
			public void receive(MessageDeleteEvent event) {
				BaseHandler.receive(event);
			}
		});
		instance.getDispatcher().registerListener(new IListener<MessageReceivedEvent>() {
			@Override
			public void receive(MessageReceivedEvent event) {
				BaseHandler.receive(event);
			}
		});
		instance.getDispatcher().registerListener(new IListener<MessageSendEvent>() {
			@Override
			public void receive(MessageSendEvent event) {
				BaseHandler.receive(event);
			}
		});
		instance.getDispatcher().registerListener(new IListener<ReadyEvent>() {
			@Override
			public void receive(ReadyEvent event) {
				BaseHandler.receive(event);
			}
		});
	}
	
	public static void main(String[] args) {
		try {
			startTime = System.currentTimeMillis();
			credentials = getCredentials();
			if (Config.runServerFrontEnd) {
				(server = new FrontEnd(credentials[3])).start();
			}
			registerListeners();
			instance.login(credentials[0], credentials[1]);
			boolean needsTable = !new File(Config.databaseFile).exists();
			db = new Database(Config.databaseFile);
			Runtime.getRuntime().addShutdownHook(new Thread() {
				@Override
				public void run() {
					synchronized (this) {
						if (server != null)
							server.stop();
						try {
							if (db.isConnected())
								db.disconnect();
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}
				}
			});
			if (needsTable) {
				db.createTable("COMMANDS", new Database.Key("COMMAND", "TEXT", "PRIMARY KEY NOT NULL"), new Database.Key("PERMISSION_LEVEL", "INT", "NOT NULL"));
				db.createTable("USERS", new Database.Key("ID", "TEXT", "PRIMARY KEY NOT NULL"), new Database.Key("PERMISSION_LEVEL", "INT", "NOT NULL"));
				db.createTable("PERMISSION_RANKS", new Database.Key("RANK", "TEXT", "PRIMARY KEY NOT NULL"), new Database.Key("PERMISSION_LEVEL", "INT", "NOT NULL"));
				
				db.insert("PERMISSION_RANKS", new String[]{"RANK", "PERMISSION_LEVEL"}, new String[]{"'Owner'", String.valueOf(ICommand.OWNER)});
				db.insert("PERMISSION_RANKS", new String[]{"RANK", "PERMISSION_LEVEL"}, new String[]{"'Anyone'", String.valueOf(ICommand.ANYONE)});
				db.insert("PERMISSION_RANKS", new String[]{"RANK", "PERMISSION_LEVEL"}, new String[]{"'Default'", String.valueOf(ICommand.DEFAULT)});
				db.insert("PERMISSION_RANKS", new String[]{"RANK", "PERMISSION_LEVEL"}, new String[]{"'Administrator'", String.valueOf(ICommand.ADMINISTRATOR)});
				if (!credentials[2].equals("null")) {
					db.insert("USERS", new String[]{"ID", "PERMISSION_LEVEL"}, new String[]{"'"+credentials[2]+"'", String.valueOf(ICommand.OWNER)});
				}
			}
			CommandRegistry.registerCommand(new HelpCommand());
			CommandRegistry.registerCommand(new EvaluateCommand());
			CommandRegistry.registerCommand(new UptimeCommand());
			CommandRegistry.registerCommand(new NameCommand());
			CommandRegistry.registerCommand(new RestartCommand());
			CommandRegistry.registerCommand(new WhoisCommand());
			CommandRegistry.registerCommand(new PermissionsCommand());
			CommandRegistry.registerCommand(new PurgeCommand());
			CommandRegistry.registerCommand(new ShrugCommand());
			ownerId = credentials[2];
			for (ScriptEngineFactory factory : scriptEngineManager.getEngineFactories()) {
				System.out.println("Loaded script engine '"+factory.getEngineName()+"' v"+factory.getEngineVersion()+
						" for language: "+factory.getLanguageName()+" v"+factory.getLanguageVersion());
			}
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
				writer.println("owner_id=null");
				writer.println("secret_key="+UUID.randomUUID());
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
		String[] credentials = new String[5];
		FileReader reader = new FileReader(file);
		BufferedReader bufferedReader = new BufferedReader(reader);
		bufferedReader.readLine();
		credentials[0] = bufferedReader.readLine().replaceFirst("email=", "");
		credentials[1] = bufferedReader.readLine().replaceFirst("password=", "");
		credentials[2] = bufferedReader.readLine().replaceFirst("owner_id=", "");
		credentials[3] = bufferedReader.readLine().replaceFirst("secret_key=", "");
		credentials[4] = bufferedReader.readLine().replaceFirst("server_invite=", "");
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
		if (server != null)
			server.stop();
		try {
			if (db.isConnected())
				db.disconnect();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		main(new String[0]);
	}
	
	public static int getUserPermissionLevel(User user) {
		try {
			ResultSet set = db.openSelect("USERS");
			while (set.next()) {
				if (user.getID().equals(set.getString("ID")))
					return set.getInt("PERMISSION_LEVEL");
			}
			db.closeSelect();
			
			db.insert("USERS", new String[]{"ID", "PERMISSION_LEVEL"}, new String[]{"'"+user.getID()+"'", String.valueOf(ICommand.DEFAULT)});
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ICommand.DEFAULT;
	}
	
	public static String getRankForLevel(int level) {
		try {
			ResultSet set = db.openSelect("PERMISSION_RANKS");
			while (set.next()) {
				if (set.getInt("PERMISSION_LEVEL") == level) {
					return set.getString("RANK");
				}
			}
			db.closeSelect();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return String.valueOf(level);
	}
	
	public static int getLevelForRank(String rank) {
		try {
			ResultSet set = db.openSelect("PERMISSION_RANKS");
			while (set.next()) {
				if (set.getString("RANK").equals(rank)) {
					return set.getInt("PERMISSION_LEVEL");
				}
			}
			db.closeSelect();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			int val = Integer.valueOf(rank);
			return val;
		} catch (NumberFormatException e) {}
		return ICommand.DEFAULT;
	}
}
