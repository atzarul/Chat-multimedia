package com.is.chatmultimedia.models;

public abstract class ServerMessage {
	
	public enum ServerMessageType {
		REGISTER, LOGIN, LOGOUT, CONVERSATION, CLOSE_CONNECTION
	}
	
	private ServerMessageType messageType;
	
	public ServerMessage(ServerMessageType messageType) {
		this.messageType = messageType;
	}
	
	public ServerMessageType getMessageType() {
		return this.messageType;
	}

}
