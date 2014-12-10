package com.is.chatmultimedia.server.services;

import java.net.Socket;

import com.is.chatmultimedia.models.LoginMessage;
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
			return login((LoginMessage) message, sourceConnection);
		case LOGOUT:
			return false;
		}
		return false;
	}

	// TEMPORARY CODE
	private boolean login(LoginMessage loginMessage, Socket userConnection) {
		userManager
				.addUser(new User(loginMessage.getUsername(), userConnection));
		return true;
	}

}
