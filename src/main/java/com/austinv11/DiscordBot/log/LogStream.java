package com.austinv11.DiscordBot.log;

import java.io.OutputStream;
import java.io.PrintStream;

public class LogStream extends PrintStream { //TODO
	
	public LogStream(OutputStream out) {
		super(out);
	}
}
