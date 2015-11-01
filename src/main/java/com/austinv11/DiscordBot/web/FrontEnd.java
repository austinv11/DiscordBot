package com.austinv11.DiscordBot.web;

import com.austinv11.DiscordBot.DiscordBot;
import com.google.gson.Gson;
import fi.iki.elonen.NanoHTTPD;
import sx.blah.discord.handle.obj.Message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FrontEnd extends NanoHTTPD {
	
	private final String secretKey;
	public static volatile List<Message> console = new ArrayList<>();
	
	public FrontEnd(String secretKey) {
		super(DiscordBot.CONFIG.hostName != null && !DiscordBot.CONFIG.hostName.equals("null") ? DiscordBot.CONFIG.hostName : null, DiscordBot.CONFIG.port);
		this.secretKey = secretKey;
	}
	
	@Override
	public Response serve(IHTTPSession session) {
		if (session.getUri().endsWith("element/console")) {
			return newFixedLengthResponse(concatList(console));
		} else if (session.getUri().endsWith("element/guilds")) {
			return newFixedLengthResponse(concatList(DiscordBot.instance.getGuildList()));
		} else if (session.getUri().contains("/js/")) {
			return newFixedLengthResponse(getTextFromClasspath("js/"+session.getUri().split("/js/")[1]));
		} else if (session.getUri().contains("/css/")) {
			return newFixedLengthResponse(getTextFromClasspath("css/"+session.getUri().split("/css/")[1]));
		} else {
			Map<String, String> parms = session.getParms();
			if (parms.get("key") == null) {
				return newFixedLengthResponse(getTextFromClasspath("html/start.html"));
			} else if (parms.get("key").equals(secretKey)) {
				return newFixedLengthResponse(getTextFromClasspath("html/"+session.getUri().split("/html/")[1]));
			} else {
				return newFixedLengthResponse(getTextFromClasspath("html/access_denied.html"));
			}
		}
	}
	
	private String concatList(List<?> list) {
		Object[] messages = new Object[list.size()];
		for (int i = 0; i < list.size(); i++)
			messages[i] = list.get(i);
		return new Gson().toJson(messages);
	}
	
	private String getTextFromClasspath(String filepath) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream(filepath)));
		String fileText = "";
		String line;
		try {
			while ((line = reader.readLine()) != null)
				fileText += line+"\n";
			reader.close();
			return fileText;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "<li>404 Not Found</li>";
	}
}
