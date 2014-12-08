package com.is.chatmultimedia.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

import com.is.chatmultimedia.models.ChatMessage;

public class ClientThread extends Thread {

	private Server server;
	private Socket clientSocket;
	private ObjectInputStream input;
	private String username;

	public static ClientThread getInstance(Socket clientSocket, Server server)
			throws IOException {
		ClientThread clientThread = new ClientThread(clientSocket, server);
		clientThread.setInputStream(new ObjectInputStream(clientSocket
				.getInputStream()));
		return clientThread;
	}

	@Override
	public void run() {
		while (clientSocket.isConnected()) {
			try {
				ChatMessage message = (ChatMessage) input.readObject();
				switch (message.getMessageType()) {
				case LOGIN:
					break;
				case LOGOUT:
					break;
				case MESSAGE:
					break;
				}
			} catch (Exception e) {
				// message failed
			}
		}
	}

	private ClientThread(Socket clientSocket, Server server) {
		this.clientSocket = clientSocket;
		this.server = server;
	}

	private void setInputStream(ObjectInputStream input) {
		this.input = input;
	}

}
