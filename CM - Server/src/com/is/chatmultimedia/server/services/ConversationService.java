package com.is.chatmultimedia.server.services;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

import com.is.chatmultimedia.models.ConversationMessage;
import com.is.chatmultimedia.models.ServerMessage;
import com.is.chatmultimedia.models.ServerMessage.ServerMessageType;
import com.is.chatmultimedia.server.models.User;

public class ConversationService {

	private UserManager userManager;
	private static ConversationService instance;

	private ConversationService() {
		userManager = UserManager.getInstance();
	}

	public static ConversationService getInstance() {
		if (instance == null) {
			instance = new ConversationService();
		}
		return instance;
	}

	public boolean serverRequest(ServerMessage message) {
		if (message.getMessageType() != ServerMessageType.CONVERSATION) {
			return false;
		}
		ConversationMessage conversationMessage = (ConversationMessage) message;
		User targetUser = userManager.getUserByUsername(conversationMessage
				.getTargetUser());
		Socket targetConnection = targetUser.getConnection();
		try {
			ObjectOutputStream output = new ObjectOutputStream(
					targetConnection.getOutputStream());
			output.writeObject(message);
		} catch (IOException e) {
			return false;
		}
		return true;
	}
}
