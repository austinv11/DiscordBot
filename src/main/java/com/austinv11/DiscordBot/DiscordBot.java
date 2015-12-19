package com.austinv11.DiscordBot;

import com.austinv11.DiscordBot.api.CommandRegistry;
import com.austinv11.DiscordBot.api.commands.ICommand;
import com.austinv11.DiscordBot.api.plugins.Plugin;
import com.austinv11.DiscordBot.api.plugins.api.bot.Bot;
import com.austinv11.DiscordBot.api.plugins.api.bot.PermissionsLevel;
import com.austinv11.DiscordBot.api.plugins.api.discord.UserPresences;
import com.austinv11.DiscordBot.api.plugins.api.events.InitEvent;
import com.austinv11.DiscordBot.api.plugins.api.io.FileIO;
import com.austinv11.DiscordBot.api.plugins.api.io.IOMode;
import com.austinv11.DiscordBot.api.plugins.api.util.ISM;
import com.austinv11.DiscordBot.api.plugins.api.util.Log;
import com.austinv11.DiscordBot.api.plugins.api.util.Time;
import com.austinv11.DiscordBot.api.plugins.api.util.Timer;
import com.austinv11.DiscordBot.commands.*;
import com.austinv11.DiscordBot.handler.BaseHandler;
import com.austinv11.DiscordBot.handler.Logger;
import com.austinv11.DiscordBot.reference.Config;
import com.austinv11.DiscordBot.reference.Database;
import com.austinv11.DiscordBot.web.FrontEnd;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import sx.blah.discord.DiscordClient;
import sx.blah.discord.handle.impl.AnnotatedEventDispatcher;
import sx.blah.discord.handle.obj.*;
import sx.blah.discord.util.MessageBuilder;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;
import java.io.*;
import java.net.URISyntaxException;
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
	public static Config CONFIG = new Config();
	public static Plugin[] plugins;
	public static final int RESTART_EXIT_CODE = 1;
	public static final int INTERNAL_EXIT_CODE = 2;
	
	public static void ready() {
		User user = instance.getOurUser();
		Logger.log("Logged in as "+user.getName()+" with user id "+user.getID()+", this user is "+user.getPresence());
		Logger.log("This user's avatar ("+user.getAvatar()+") is located at the url "+user.getAvatarURL());
		if (!credentials[4].equals("null")) {
			if (credentials[4].contains("https://discord.gg/") && acceptInvite(credentials[4])) {
				Logger.log("Accepted initial invitation");
			} else {
				Logger.log("Invite url "+credentials[4]+" is invalid!");
			}
		}
		List<Guild> guilds = instance.getGuilds();
		Logger.log("Guilds connected to:");
		for (Guild guild : guilds) {
			Logger.log("*'"+guild.getName()+"' with id "+guild.getID()+" with channels:");
			for (Channel channel : guild.getChannels()) {
				if (!messageCache.containsKey(channel.getID())) {
					messageCache.put(channel.getID(), formMessageCache(channel.getMessages()));
				}
				Logger.log("\t*'"+channel.getName()+"' with id "+channel.getID());
			}
		}
	}
	
	public static boolean acceptInvite(String invite) {
		try {
			String inviteCode = invite.split(".gg/")[1].split(" ")[0];
			Logger.log("Received invite code "+inviteCode);
			Invite invite1 = new Invite(inviteCode);
			Invite.InviteResponse response = invite1.details();
			invite1.accept();
			new MessageBuilder().appendContent(String.format("Hello, %s! I am a bot and I was invited to join you guys at the %s channel",
					response.getGuildName(), response.getChannelName())).withChannel(response.getChannelID());
			return true;
		} catch (Exception e) {
			Logger.log(e);
			return false;
		}
	}
	
	public static void main(String[] args) {
		try {
			startTime = System.currentTimeMillis();
			
			File config = new File("./config.json");
			Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
			if (config.exists()) {
				FileReader reader = new FileReader(config);
				CONFIG = gson.fromJson(reader, Config.class);
			} else {
				config.createNewFile();
			}
			String configJson = gson.toJson(CONFIG);
			PrintStream writer = new PrintStream(config);
			writer.println(configJson);
			writer.flush();
			writer.close();
			
			credentials = getCredentials();
			
			if (CONFIG.runServerFrontEnd) {
				(server = new FrontEnd(credentials[3])).start();
			}
			
			instance.setDispatcher(new AnnotatedEventDispatcher());
			instance.getDispatcher().registerListener(new BaseHandler());
			
			Runtime.getRuntime().addShutdownHook(new Thread() {
				@Override
				public void run() {
					synchronized (this) {
						closeBot();
					}
				}
			});
			
			instance.login(credentials[0], credentials[1]);
			
			boolean needsTable = !new File(CONFIG.databaseFile).exists();
			db = new Database(CONFIG.databaseFile);
			if (needsTable) {
				db.createTable("COMMANDS", new Database.Key("COMMAND", "TEXT", "PRIMARY KEY NOT NULL"), new Database.Key("PERMISSION_LEVEL", "INT", "NOT NULL"));
				db.createTable("USERS", new Database.Key("ID", "TEXT", "PRIMARY KEY NOT NULL"), new Database.Key("PERMISSION_LEVEL", "INT", "NOT NULL"));
				db.createTable("PERMISSION_RANKS", new Database.Key("RANK", "TEXT", "PRIMARY KEY NOT NULL"), new Database.Key("PERMISSION_LEVEL", "INT", "NOT NULL"));
				db.createTable("ALIASES", new Database.Key("NAME", "TEXT", "PRIMARY KEY NOT NULL"), new Database.Key("COMMAND", "TEXT", "NOT NULL"));
				
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
			CommandRegistry.registerCommand(new NLPCommand());
			CommandRegistry.registerCommand(new PingCommand());
			CommandRegistry.registerCommand(new PluginsCommand());
			CommandRegistry.registerCommand(new ShutdownCommand());
			CommandRegistry.registerCommand(new AliasCommand());
			
			ownerId = credentials[2];
			
			addGlobalScriptBinding("Config", CONFIG);
			addGlobalScriptBinding(IOMode.class);
			addGlobalScriptBinding(FileIO.class);
			addGlobalScriptBinding(PermissionsLevel.class);
			addGlobalScriptBinding(Timer.class);
			addGlobalScriptBinding(ISM.class);
			addGlobalScriptBinding(UserPresences.class);
			addGlobalScriptBinding(Time.class);
			addGlobalScriptBinding(Log.class);
			addGlobalScriptBinding(Bot.class);
			for (ScriptEngineFactory factory : scriptEngineManager.getEngineFactories()) {
				Logger.log("Loaded script engine '"+factory.getEngineName()+"' v"+factory.getEngineVersion()+
						" for language: "+factory.getLanguageName()+" v"+factory.getLanguageVersion());
			}
			
			Logger.log("Loading plugins...");
			File pluginDataDir = new File(FileIO.PLUGIN_DATA_DIR);
			if (!pluginDataDir.exists())
				pluginDataDir.mkdir();
			if (!pluginDataDir.isDirectory())
				throw new IOError(new Throwable(FileIO.PLUGIN_DATA_DIR+" is not a directory!"));
			
			File pluginDir = new File("./plugins");
			if (!pluginDir.exists())
				pluginDir.mkdir();
			if (!pluginDir.isDirectory())
				throw new IOError(new Throwable("./plugins is not a directory!"));
			File[] pluginFiles = pluginDir.listFiles(pathname->{
				return pathname.getName().endsWith(".zip");
			});
			if (pluginFiles != null && pluginFiles.length > 0) {
				plugins = new Plugin[pluginFiles.length];
				for (int i = 0; i < pluginFiles.length; i++) {
					plugins[i] = new Plugin(pluginFiles[i]);
					Logger.log("Plugin '"+plugins[i].manifest.plugin_id+"' v"+plugins[i].manifest.version+" loaded ("+
							(i+1)+"/"+pluginFiles.length+")");
				}
			}
			for (Plugin plugin : plugins) {
				new InitEvent(plugin.manifest).propagate();
			}
			
			try {
				ResultSet set = db.openSelect("ALIASES");
				HashMap<String, String> aliases = new HashMap<>();
				while (set.next()) {
					aliases.put(set.getString("NAME"), set.getString("COMMAND"));
				}
				db.closeSelect();
				for (String key : aliases.keySet()) {
					CommandRegistry.registerAlias(key, aliases.get(key));
				}
			} catch (Exception e) {
				Logger.log(e);
			}
			
		} catch (Exception e) {
			Logger.log(e);
			Logger.log(Logger.Level.FATAL, "There was an error initializing the bot, rebuilding the credentials.txt");
			File file = new File("./credentials.txt");
			if (file.exists()) {
				file.renameTo(new File("./credentials-backup.txt"));
			}
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
				Logger.log(e1);
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
			cache.put(message.getID(), new Message(message.getID(), message.getContent(), message.getAuthor(), 
					message.getChannel(), message.getTimestamp()));//Instantiation makes sure the base api doesn't break anything
		return cache;
	}
	
	public static String doesCommandMatch(ICommand command, String message) {
		if (message.startsWith(String.valueOf(CONFIG.commandDiscriminator))) {
			message = message.replaceFirst(String.valueOf(CONFIG.commandDiscriminator), "");
		}
		String commandName = message.contains(" ") ? message.split(" ")[0] : message;
		if (command.getCommand().equals(commandName))
			return commandName;
		for (String alias : command.getAliases())
			if (commandName.equals(alias))
				return alias;
		return null;
	}
	
	public static void restart() {
		Logger.log("Restarting the bot...");
		try {
			Logger.log(DiscordBot.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
			if (System.getProperty("os.name").toLowerCase().contains("win")) { //TODO: Ensure this works
				Runtime.getRuntime().exec("java -jar "+
						DiscordBot.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
			} else {
				Runtime.getRuntime().exec("java -jar "+
						DiscordBot.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
			}
		} catch (IOException | URISyntaxException e) {
			Logger.log(e);
		}
		Runtime.getRuntime().exit(RESTART_EXIT_CODE);
	}
	
	public static void closeBot() {
		Logger.log("Closing the bot...");
		if (server != null)
			server.stop();
		try {
			if (db.isConnected())
				db.disconnect();
		} catch (SQLException e) {
			Logger.log(e);
		}
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
			Logger.log(e);
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
			Logger.log(e);
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
			Logger.log(e);
		}
		try {
			int val = Integer.valueOf(rank);
			return val;
		} catch (NumberFormatException e) {}
		return ICommand.DEFAULT;
	}
	
	public static ScriptEngine getScriptEngineForLang(String lang) {
		ScriptEngine engine = scriptEngineManager.getEngineByExtension(lang);
		engine = engine == null ? scriptEngineManager.getEngineByName(lang) : engine;
		return engine;
	}
	
	public static void addGlobalScriptBinding(Class clazz) throws IllegalAccessException, InstantiationException {
		addGlobalScriptBinding(clazz.getSimpleName(), clazz.newInstance());
	}
	
	public static void addGlobalScriptBinding(String name, Object object) {
		scriptEngineManager.put(name, object);
	}
}
