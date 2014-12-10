package com.is.chatmultimedia.server.models;

import java.net.Socket;

public class User {

	private String username;
	private Socket connection;

	public User(String username, Socket connection) {
		this.username = username;
		this.connection = connection;
	}

	public String getUsername() {
		return this.username;
	}

	public Socket getConnection() {
		return this.connection;
	}

}
