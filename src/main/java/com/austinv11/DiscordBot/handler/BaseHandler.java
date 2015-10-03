package com.austinv11.DiscordBot.handler;

import com.austinv11.DiscordBot.DiscordBot;
import com.austinv11.DiscordBot.api.events.MessageEvent;
import com.austinv11.DiscordBot.api.events.Subscribe;
import sx.blah.discord.obj.Invite;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BaseHandler {
	
	@Subscribe
	public static void onMentioned(MessageEvent.MentionedEvent event) {
		try {
			try {
				if (event.message.getContent().contains("https://discord.gg/")
						|| event.message.getContent().contains("http://discord.gg/")) {
					String invite = event.message.getContent().split(".gg/")[1].split(" ")[0];
					System.out.println("Received invite code "+invite);
					Invite invite1 = DiscordBot.instance.acceptInvite(invite);
					if (null != invite1) {
						DiscordBot.instance.sendMessage(String.format("Hello, %s! I was invited to the %s channel by @%s.",
								invite1.getGuildName(), invite1.getChannelName(), invite1.getInviterUsername()), invite1.getChannelID(), invite1.getInviterID());
					}
				}
			} catch (NullPointerException exception) {}//TODO: remove, "inviter" in the invite object is always null for some reason
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Subscribe
	public static void onMessageReceived(MessageEvent.MessageReceivedEvent event) {
		try {
			if (event.message.getContent().contains(":p")) {
				List<String> mentioned = new ArrayList<>();
				mentioned.add(event.message.getAuthor().getID());
				mentioned.addAll(Arrays.asList(event.message.getMentionedIDs()));
				String[] mentionedArray = new String[mentioned.size()];
				for (int i = 0; i < mentioned.size(); i++)
					mentionedArray[i] = mentioned.get(i);
				DiscordBot.instance.sendMessage("What @"+event.message.getAuthor().getName()+" meant to say was: '"+event.message.getContent().replace(":p", ":P")+"'", event.message.getChannelID(), mentionedArray);
				DiscordBot.instance.deleteMessage(event.message.getMessageID(), event.message.getChannelID());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
