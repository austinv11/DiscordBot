package com.austinv11.DiscordBot.api.plugins.api.discord;

import com.austinv11.DiscordBot.api.plugins.api.util.Time;
import org.json.simple.parser.ParseException;
import sx.blah.discord.handle.obj.Message;
import sx.blah.discord.util.MessageBuilder;

import java.io.IOException;

/**
 * This object represents a message. This is an intermediary between Discord4J and a script in order to prevent Discord4J
 * breaking stuff in the future.
 */
public class ScriptMessage {
	
	protected Message javaMessage;
	
	public ScriptMessage(Message message) {
		this.javaMessage = message;
	}
	
	/**
	 * Gets the string contents of the message
	 * @return The content
	 */
	public String getContent() {
		return javaMessage.getContent();
	}
	
	/**
	 * Gets the channel the message was sent to
	 * @return The channel
	 */
	public ScriptChannel getChannel() {
		return new ScriptChannel(javaMessage.getChannel());
	}
	
	/**
	 * Gets the author who wrote this message
	 * @return The author
	 */
	public ScriptUser getAuthor() {
		return new ScriptUser(javaMessage.getAuthor());
	}
	
	/**
	 * Gets the message's id
	 * @return The message id
	 */
	public String getID() {
		return javaMessage.getID();
	}
	
	/**
	 * Gets the {@link Time} the message was sent
	 * @return The timestamp
	 */
	public Time getTimestamp() {
		return new Time(javaMessage.getTimestamp());
	}
	
	/**
	 * Replies to the message, automatically @mentions the author of the message
	 * @param content The content of the message
	 * @throws IOException
	 * @throws ParseException
	 */
	public void reply(String content) throws IOException, ParseException {
		javaMessage.reply(content);
	}
	
	/**
	 * Replies to the message, automatically @mentions the author of the message
	 * @param message The message object for the message
	 */
	public void reply(ScriptMessage message) {
		new MessageBuilder().withContent(getAuthor().mention()+" "+message.getContent())
				.withChannel(javaMessage.getChannel()).build();
	}
}
