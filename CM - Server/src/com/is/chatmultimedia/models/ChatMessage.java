package com.is.chatmultimedia.models;

public class ChatMessage {

	public enum MessageType {
		LOGIN, LOGOUT, MESSAGE
	}

	private MessageType messageType;
	private String message;
	private String targetUser;

	public ChatMessage(MessageType messageType, String message,
			String targetUser) {
		this.messageType = messageType;
		this.message = message;
		this.targetUser = targetUser;
	}

	public MessageType getMessageType() {
		return this.messageType;
	}

	public String getMessage() {
		return this.message;
	}

	public String getTargetUser() {
		return this.targetUser;
	}

}
