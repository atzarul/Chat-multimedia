package com.is.chatmultimedia.models;

public class LogoutMessage extends ServerMessage {

	private String username;
	private static final long serialVersionUID = 1;

	public LogoutMessage(String username) {
		super(ServerMessageType.LOGOUT);
		this.username = username;
	}

	public String getUsername() {
		return this.username;
	}

}
