package com.is.chatmultimedia.models;

public class LoginResponseMessage extends ClientMessage {

	private boolean successful;
	private String message;
	private static final long serialVersionUID = 1;

	public LoginResponseMessage(boolean successful, String message) {
		super(ClientMessageType.LOGIN_RESPONSE);
		this.successful = successful;
		this.message = message;
	}

	public boolean isSuccessful() {
		return this.successful;
	}

	public String getMessage() {
		return this.message;
	}

}
