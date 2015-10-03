package com.austinv11.DiscordBot.commands;

import com.austinv11.DiscordBot.DiscordBot;
import com.austinv11.DiscordBot.api.commands.CommandSyntaxException;
import com.austinv11.DiscordBot.api.commands.ICommand;
import sx.blah.discord.obj.Channel;
import sx.blah.discord.obj.Message;
import sx.blah.discord.obj.User;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Optional;

public class UptimeCommand implements ICommand {
	
	@Override
	public String getCommand() {
		return "uptime";
	}
	
	@Override
	public String[] getAliases() {
		return new String[]{"up"};
	}
	
	@Override
	public boolean removesCommandMessage() {
		return true;
	}
	
	@Override
	public String getUsage() {
		return getCommand();
	}
	
	@Override
	public String getHelpMessage() {
		return "Gets the amount of time this bot has been online";
	}
	
	@Override
	public Optional<String> executeCommand(String parameters, User executor, Channel channel, Message commandMessage) throws CommandSyntaxException {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(DiscordBot.startTime);
		String result = "This bot has been active since "+DateFormat.getInstance().format(calendar.getTime())+"\n";
		
		long secondsInMilli = 1000;
		long minutesInMilli = secondsInMilli * 60;
		long hoursInMilli = minutesInMilli * 60;
		long daysInMilli = hoursInMilli * 24;
		long difference = System.currentTimeMillis()-DiscordBot.startTime;
		
		long days = difference / daysInMilli;
		difference = difference % daysInMilli;
		
		long hours = difference / hoursInMilli;
		difference = difference % hoursInMilli;
		
		long minutes = difference / minutesInMilli;
		difference = difference % minutesInMilli;
		
		long seconds = difference / secondsInMilli;
		
		result += "Uptime: "+days+" days, "+hours+" hours, "+minutes+" minutes, "+seconds+" seconds";
		
		return Optional.of(result);
	}
}
