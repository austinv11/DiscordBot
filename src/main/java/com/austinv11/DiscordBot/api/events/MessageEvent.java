package com.austinv11.DiscordBot.api.events;

import sx.blah.discord.obj.Message;

/**
 * Abstracted message event class
 * Represents any event that has to do with a message
 */
@DiscordEvent
public class MessageEvent {
	
	/**
	 * The message involved
	 */
	public Message message;
	
	public MessageEvent(Message message) {
		this.message = message;
	}
	
	/**
	 * Posted when a message is received
	 */
	@DiscordEvent
	public static class MessageReceivedEvent extends MessageEvent {
		
		public MessageReceivedEvent(Message message) {
			super(message);
		}
	}
	
	/**
	 * Posted when a message is sent by the bot
	 */
	@DiscordEvent
	public static class MessageSentEvent extends MessageEvent {
		
		public MessageSentEvent(Message message) {
			super(message);
		}
	}
	
	/**
	 * Posted when this bot is mentioned
	 */
	@DiscordEvent
	public static class MentionedEvent extends MessageEvent {
		
		public MentionedEvent(Message message) {
			super(message);
		}
	}
	
	/**
	 * Posted when a message is edited
	 */
	@DiscordEvent
	public static class MessageUpdateEvent extends MessageEvent {
		
		/**
		 * The original message
		 */
		public Message oldMessage;
		/**
		 * The new message
		 */
		public Message newMessage;
		
		protected MessageUpdateEvent(Message message) {
			super(message);
		}
		
		public MessageUpdateEvent(Message oldMessage, Message newMessage) {
			this(newMessage);
			this.oldMessage = oldMessage;
			this.newMessage = newMessage;
		}
	}
	
	/**
	 * Posted when a message is deleted
	 */
	@DiscordEvent
	public static class MessageDeleteEvent extends MessageEvent {
		
		public MessageDeleteEvent(Message message) {
			super(message);
		}
	}
}
