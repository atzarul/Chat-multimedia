package com.is.chatmultimedia.server;

import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

import com.is.chatmultimedia.models.ServerMessage;
import com.is.chatmultimedia.server.services.AuthenticationService;
import com.is.chatmultimedia.server.services.ConversationService;

public class Server {

	private ServerSocket serverSocket;
	private List<Socket> connections;
	private volatile boolean stopped = false;

	// server services
	private AuthenticationService authenticationService;
	private ConversationService conversationService;

	private static Server instance;
	private static final int PORT = 8888;

	public static Server getInstance() {
		if (instance == null) {
			instance = new Server();
		}
		return instance;
	}

	private Server() {
		connections = new ArrayList<>();
		authenticationService = AuthenticationService.getInstance();
		conversationService = ConversationService.getInstance();
	}

	public void start() {
		new Runnable() {

			@Override
			public void run() {
				Socket clientSocket;

				try {
					serverSocket = new ServerSocket(PORT);
					serverSocket.setSoTimeout(1000);

					while (!stopped) {
						try {
							clientSocket = serverSocket.accept();
							ClientThread clientThread = ClientThread
									.getInstance(clientSocket);
							clientThread.start();
							addConnection(clientSocket);
						} catch (SocketTimeoutException timeOutException) {
							if (stopped) {
								break;
							}
						} catch (IOException getInstanceException) {
							// creating client thread failed
						}
					}

				} catch (IOException e) {
					// server died
				}
			}

		}.run();

	}

	public void stop() {
		stopped = true;
	}

	public boolean processMessage(ServerMessage message, Socket sourceConnection) {
		switch (message.getMessageType()) {
		case REGISTER:
			return false;
		case LOGIN:
		case LOGOUT:
			return authenticationService.serverRequest(message,
					sourceConnection);
		case CONVERSATION:
			return conversationService.serverRequest(message);
		case CLOSE_CONNECTION:
			return false;
		}
		return false;
	}

	private synchronized boolean addConnection(Socket connection) {
		return connections.add(connection);
	}

}
