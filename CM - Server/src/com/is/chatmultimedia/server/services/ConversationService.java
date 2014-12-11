package com.is.chatmultimedia.server.services;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

import com.is.chatmultimedia.models.ClientConversationMessage;
import com.is.chatmultimedia.models.ServerConversationMessage;
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
		ServerConversationMessage conversationMessage = (ServerConversationMessage) message;
		User targetUser = userManager.getUserByUsername(conversationMessage
				.getTargetUser());
		Socket targetConnection = targetUser.getConnection();
		try {
			ObjectOutputStream output = new ObjectOutputStream(
					targetConnection.getOutputStream());
			ClientConversationMessage messageForClient = new ClientConversationMessage(
					conversationMessage);
			output.writeObject(messageForClient);
			output.flush();
		} catch (IOException e) {
			return false;
		}
		return true;
	}
}
