package com.is.chatmultimedia.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Map;

public class Server {

	private ServerSocket serverSocket;
	private List<Socket> temporaryConnections;
	private Map<String, Socket> connections;
	private int port;
	private volatile boolean stopped = false;

	public Server(int port) {
		this.port = port;
		temporaryConnections = new ArrayList<>();
		connections = new HashMap<>();
	}

	public void start() {
		Server server = this;
		new Runnable() {

			@Override
			public void run() {
				Socket clientSocket;

				try {
					serverSocket = new ServerSocket(port);
					serverSocket.setSoTimeout(1000);

					while (!stopped) {
						try {
							clientSocket = serverSocket.accept();
							ClientThread clientThread = ClientThread
									.getInstance(clientSocket, server);
							clientThread.start();
							addTemporaryConnection(clientSocket);
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

	private synchronized boolean addTemporaryConnection(Socket clientSocket) {
		return temporaryConnections.add(clientSocket);
	}

	private synchronized void changeTemporaryToGenuineConnection(
			Socket clientSocket, String username) {
		temporaryConnections.remove(clientSocket);
		connections.put(username, clientSocket);
	}
}
