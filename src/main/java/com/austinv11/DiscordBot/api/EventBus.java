package com.austinv11.DiscordBot.api;

import com.austinv11.DiscordBot.DiscordBot;
import com.austinv11.DiscordBot.api.commands.ICommand;
import com.austinv11.DiscordBot.api.events.DiscordEvent;
import com.austinv11.DiscordBot.api.events.Subscribe;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * The discord event bus, post events and subscribe to events as well as register commands here
 */
public class EventBus {
	
	private static HashMap<Class, List<Method>> staticTargets = new HashMap<>();
	private static HashMap<Class, List<Object[]>> dynamicTargets = new HashMap<>();
	private static List<ICommand> commands = new ArrayList<>();
	private static List<String> commandPermsCached = new ArrayList<>();
	
	/**
	 * Posts an event an propagates it to all subscribed methods
	 * @param event The event to post
	 */
	public static void postEvent(Object event) {
		if (!event.getClass().isAnnotationPresent(DiscordEvent.class))
			throw new SecurityException("Discord event posted without the a proper annotation!");
		for (Class eventClass : staticTargets.keySet()) {
			if (event.getClass().isAssignableFrom(eventClass) || event.getClass().equals(eventClass)) {
				for (Method method : staticTargets.get(eventClass)) {
					try {
						method.invoke(null, event);
					} catch (IllegalAccessException | InvocationTargetException e) {
						e.printStackTrace();
					}
				}
			}
		}
		for (Class eventClass : dynamicTargets.keySet()) {
			if (event.getClass().isAssignableFrom(eventClass) || event.getClass().equals(eventClass)) {
				for (Object[] objectArray : dynamicTargets.get(eventClass)) {
					try {
						((Method)objectArray[0]).invoke(objectArray[1], event);
					} catch (IllegalAccessException | InvocationTargetException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	/**
	 * Registers an event handler
	 * @param handler The handler object
	 */
	public static void registerHandler(Object handler) {
		List<Method> methods = new ArrayList<>();
		methods.addAll(Arrays.asList(handler.getClass().getDeclaredMethods()));
		for (Method method : methods) {
			if (method.isAnnotationPresent(Subscribe.class)) {
				if (method.getParameterCount() != 1 || !method.getParameterTypes()[0].isAnnotationPresent(DiscordEvent.class)) {
					throw new IllegalArgumentException("Subscribed method has incorrect parameters");
				}
				method.setAccessible(true);
				List<Object[]> handlers = dynamicTargets.containsKey(method.getParameterTypes()[0]) ?
						dynamicTargets.get(method.getParameterTypes()[0]) : new ArrayList<>();
				handlers.add(new Object[]{method, handler});
				dynamicTargets.put(method.getParameterTypes()[0], handlers);
			}
		}
	}
	
	/**
	 * Registers an event handler
	 * @param handler The handler class
	 */
	public static void registerHandler(Class handler) {
		List<Method> methods = new ArrayList<>();
		methods.addAll(Arrays.asList(handler.getDeclaredMethods()));
		for (Method method : methods) {
			if (method.isAnnotationPresent(Subscribe.class)) {
				if (method.getParameterCount() != 1 || !method.getParameterTypes()[0].isAnnotationPresent(DiscordEvent.class)) {
					throw new IllegalArgumentException("Subscribed method has incorrect parameters");
				}
				method.setAccessible(true);
				List<Method> handlers = staticTargets.containsKey(method.getParameterTypes()[0]) ? 
						staticTargets.get(method.getParameterTypes()[0]) : new ArrayList<>();
				handlers.add(method);
				staticTargets.put(method.getParameterTypes()[0], handlers);
			}
		}
	}
	
	/**
	 * Registers a command
	 * @param command The command to register
	 */
	public static void registerCommand(ICommand command) {
		if (commandPermsCached.isEmpty()) {
			try {
				ResultSet set = DiscordBot.db.openSelect("COMMANDS");
				while (set.next()) {
					commandPermsCached.add(set.getString("COMMAND"));
				}
				DiscordBot.db.closeSelect();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (!commandPermsCached.contains(command.getCommand())) {
			try {
				DiscordBot.db.insert("COMMANDS", new String[]{"COMMAND", "PERMISSION_LEVEL"}, new String[]{"'"+command.getCommand()+"'",
						String.valueOf(command.getDefaultPermissionLevel())});
				commandPermsCached.add(command.getCommand());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		commands.add(command);
	}
	
	/**
	 * Gets all the currently registered commands
	 * @return The commands
	 */
	public static List<ICommand> getAllCommands() {
		return new ArrayList<>(commands);
	}
}
