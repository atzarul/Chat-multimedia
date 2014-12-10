package com.is.chatmultimedia.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

import com.is.chatmultimedia.models.ServerMessage;

public class ClientThread extends Thread {

	private Socket clientSocket;
	private ObjectInputStream input;

	public static ClientThread getInstance(Socket clientSocket)
			throws IOException {
		ClientThread clientThread = new ClientThread(clientSocket);
		clientThread.setInputStream(new ObjectInputStream(clientSocket
				.getInputStream()));
		return clientThread;
	}

	@Override
	public void run() {
		while (clientSocket.isConnected()) {
			try {
				ServerMessage serverMessage = (ServerMessage) input
						.readObject();
				Server.getInstance().processMessage(serverMessage, clientSocket);
			} catch (Exception e) {
				// message failed
			}
		}
	}

	private ClientThread(Socket clientSocket) {
		this.clientSocket = clientSocket;
	}

	private void setInputStream(ObjectInputStream input) {
		this.input = input;
	}

}
