package com.austinv11.DiscordBot.web;

import com.austinv11.DiscordBot.reference.Config;
import fi.iki.elonen.NanoHTTPD;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FrontEnd extends NanoHTTPD {
	
	private final String secretKey;
	public static volatile List<String> console = new ArrayList<>();
	
	public FrontEnd(String secretKey) {
		super(Config.hostName != null && !Config.hostName.equals("null") ? Config.hostName : null, Config.port);
		this.secretKey = secretKey;
	}
	
	@Override
	public Response serve(IHTTPSession session) {
		if (session.getUri().endsWith("element/console")) {
			return newFixedLengthResponse(concatList(console));
		} else if (session.getUri().contains("/js/")) {
			return newFixedLengthResponse(getTextFromClasspath("js/"+session.getUri().split("/js/")[1]));
		} else {
			Map<String, String> parms = session.getParms();
			if (parms.get("key") == null) {
				String msg = "<html><body>\n";
				msg += "<form action = '/html/main.html?' method='get'>\n"+"  <p>Secret Key: <input type='text' name='key'></p>\n"+"</form>\n";
				msg += "</body></html>\n";
				return newFixedLengthResponse(msg);
			} else if (parms.get("key").equals(secretKey)) {
				return newFixedLengthResponse(getTextFromClasspath("html/"+session.getUri().split("/html/")[1]));
			} else {
				return newFixedLengthResponse("<h1>Security Exception: Access Denied!</h1>");
			}
		}
	}
	
	private String concatList(List<String> list) {
		String string = "";
		for (String s : list)
			string += "<p>"+s+"</p>";
		return string;
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
