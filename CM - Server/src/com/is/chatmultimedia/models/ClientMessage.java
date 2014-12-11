package com.is.chatmultimedia.models;

import java.io.Serializable;

public abstract class ClientMessage implements Serializable {
	
	private ClientMessageType messageType;
	private static final long serialVersionUID = 1;
	
	public enum ClientMessageType {
		REGISTER_RESPONSE, LOGIN_RESPONSE, CONVERSATION
	}
	
	public ClientMessage(ClientMessageType messageType) {
		this.messageType = messageType;
	}
	
	public ClientMessageType getMessageType() {
		return this.messageType;
	}

}
