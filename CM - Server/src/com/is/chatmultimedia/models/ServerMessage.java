package com.is.chatmultimedia.models;

import java.io.Serializable;

public abstract class ServerMessage implements Serializable {
	
	private static final long serialVersionUID = 1;
	
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
