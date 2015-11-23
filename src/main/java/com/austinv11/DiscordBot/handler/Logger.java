package com.austinv11.DiscordBot.handler;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * This logs actions to the appropriate place
 */
public class Logger {
	
	public static File logFile;
	static  {
		logFile = new File("./DiscordBotLog-"+LocalDate.now().toString()+".log");
		if (logFile.exists()) {
			logFile.delete();
		}
	}
	
	public static void log(Level level, Object message, LocalDateTime timeStamp, boolean logToFile) {
		String messageString;
		if (message instanceof Exception) {
			StringWriter writer = new StringWriter();
			((Exception) message).printStackTrace(new PrintWriter(writer));
			messageString = writer.toString();
			try {
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			messageString = String.valueOf(message);
		}
		
		String formattedString = String.format("[%s][%s] %s", level.name(), timeStamp.toString(), messageString);
		if (logToFile) {
			try {
				PrintWriter writer = new PrintWriter(new FileWriter(logFile, true));
				writer.println(formattedString);
				writer.flush();
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		System.out.println(formattedString);
	}
	
	public static void log(Level level, Object message, LocalDateTime timeStamp) {
		log(level, message, timeStamp, true);
	}
	
	public static void log(Level level, Object message) {
		log(level, message, LocalDateTime.now());
	}
	
	public static void log(Object message, LocalDateTime timeStamp) {
		log(Level.INFO, message, timeStamp);
	}
	
	public static void log(Object message) {
		log(Level.INFO, message);
	}
	
	public static void log(Exception exception) {
		log(Level.WARNING, exception);
	}
	
	public enum Level {
		DEBUG, INFO, WARNING, SEVERE, FATAL
	}
}
