package com.austinv11.DiscordBot.api.plugins.api.util;

import com.austinv11.DiscordBot.api.plugins.api.events.InterScriptMessage;

import java.util.HashMap;

/**
 * ISM (stands for: Inter-Script-Messaging) allows for scripts to transmit data to other scripts. This is required because
 * each script is loaded in an independent environment.
 */
public class ISM {
	
	private static HashMap<String, Object> globalVariables = new HashMap<>();
	
	/**
	 * Sends an inter-script message, posting the {@link InterScriptMessage} event
	 * @param target The path to the script to message
	 * @param message The message
	 */
	public void sendMessage(String target, Object message) {
		new InterScriptMessage(target, message).propagate();
	}
	
	/**
	 * Stores an object which can be accessed by any script
	 * @param key The key to store the object under
	 * @param object The object to store
	 */
	public void putVariable(String key, Object object) {
		globalVariables.put(key, object);
	}
	
	/**
	 * Retrieves an object which can be accessed by any script
	 * @param key The key the object is stored under
	 * @return The object stored
	 */
	public Object getVariable(String key) {
		return globalVariables.get(key);
	}
}
