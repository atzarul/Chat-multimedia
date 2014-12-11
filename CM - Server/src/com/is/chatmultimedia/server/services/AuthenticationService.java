package com.is.chatmultimedia.server.services;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

import com.is.chatmultimedia.models.ClientMessage;
import com.is.chatmultimedia.models.LoginMessage;
import com.is.chatmultimedia.models.LoginResponseMessage;
import com.is.chatmultimedia.models.ServerMessage;
import com.is.chatmultimedia.server.models.User;

public class AuthenticationService {

	private UserManager userManager;
	private static AuthenticationService instance;

	private AuthenticationService() {
		userManager = UserManager.getInstance();
	}

	public static AuthenticationService getInstance() {
		if (instance == null) {
			instance = new AuthenticationService();
		}
		return instance;
	}

	public boolean serverRequest(ServerMessage message, Socket sourceConnection) {
		switch (message.getMessageType()) {
		case LOGIN:
			login((LoginMessage) message, sourceConnection);
			return true;
		case LOGOUT:
			return false;
		default:
			return false;
		}
	}

	// TEMPORARY CODE
	private boolean login(LoginMessage loginMessage, Socket userConnection) {
		userManager
				.addUser(new User(loginMessage.getUsername(), userConnection));
		LoginResponseMessage loginResponse = new LoginResponseMessage(true,
				"Login successful!");
		writeResponse(loginResponse, userConnection);
		return true;
	}

	private void writeResponse(ClientMessage message, Socket sourceConnection) {
		try {
			ObjectOutputStream output = new ObjectOutputStream(
					sourceConnection.getOutputStream());
			output.writeObject(message);
			output.flush();
		} catch (IOException e) {
			// oops
		}
	}

}
