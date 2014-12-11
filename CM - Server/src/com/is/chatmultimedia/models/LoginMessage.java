package com.is.chatmultimedia.models;

public class LoginMessage extends ServerMessage {
	
	private String username;
	private String password;
	private static final long serialVersionUID = 1;
	
	public LoginMessage(String username, String password) {
		super(ServerMessageType.LOGIN);
		this.username = username;
		this.password = password;
	}
	
	public String getUsername() {
		return this.username;
	}
	
	public String getPassword() {
		return this.password;
		}

}
