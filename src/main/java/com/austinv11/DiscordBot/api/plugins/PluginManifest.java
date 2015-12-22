package com.austinv11.DiscordBot.api.plugins;

/**
 * Java representation of the PLUGIN_MANIFEST.json
 */
public class PluginManifest {
	
	/**
	 * The name of the plugin
	 */
	public String plugin_id;
	/**
	 * The author(s) of the plugin
	 */
	public String author;
	/**
	 * The version of the plugin
	 */
	public String version;
	/**
	 * A short description of what the plugin does
	 */
	public String description;
	/**
	 * The commands (if any) added by the plugin
	 */
	public Command[] commands;
	/**
	 * The event handlers (if any) to be sent events
	 */
	public EventHandler[] event_handlers;
	
	public static class Command {
		
		/**
		 * The name of the command used to invoke it
		 */
		public String name;
		/**
		 * The aliases which could be used to invoke the command (if any)
		 */
		public String[] aliases;
		/**
		 * Whether the message invoking the command should be removed
		 */
		public boolean removes_command_message;
		/**
		 * Demonstrates correct message syntax
		 */
		public String usage;
		/**
		 * A short message describing what the command does
		 */
		public String help_message;
		/**
		 * A <b>NUMBER</b> representing the permission level required to use the command<br>
		 * NOTE: The variable should be a string but it must also be able to be parsed into an integer, the string is
		 * used to allow for it to support inline programming and to add the ability for rank names
		 */
		public String permission_level;
		/**
		 * The script to be called when the command is used
		 */
		public String executor;
		/**
		 * Whether this command can be executed by console
		 */
		public boolean executable_by_console;
	}
	
	public static class EventHandler {
		
		/**
		 * The script to be called when the event occurs
		 */
		public String script;
		/**
		 * The targeted event for this handler
		 */
		public String event_filter;
	}
}
