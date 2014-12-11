package com.is.chatmultimedia.models;

public class ClientConversationMessage extends ClientMessage {
	
	private String sourceUser;
	private String targetUser;
	private String messageToSend;
	private static final long serialVersionUID = 1;
	
	public ClientConversationMessage(String sourceUser, String targetUser, String messageToSend) {
		super(ClientMessageType.CONVERSATION);
		this.sourceUser = sourceUser;
		this.targetUser = targetUser;
		this.messageToSend = messageToSend;
	}
	
	public ClientConversationMessage(ServerConversationMessage conversationMessage) {
		super(ClientMessageType.CONVERSATION);
		this.sourceUser = conversationMessage.getSourceUser();
		this.targetUser = conversationMessage.getTargetUser();
		this.messageToSend = conversationMessage.getMessageToSend();
	}

	public String getSourceUser() {
		return sourceUser;
	}

	public String getTargetUser() {
		return targetUser;
	}

	public String getMessageToSend() {
		return messageToSend;
	}

}
