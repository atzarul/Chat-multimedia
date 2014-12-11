package com.is.chatmultimedia.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import com.is.chatmultimedia.models.ClientConversationMessage;
import com.is.chatmultimedia.models.ClientMessage;
import com.is.chatmultimedia.models.LoginMessage;
import com.is.chatmultimedia.models.LoginResponseMessage;
import com.is.chatmultimedia.models.ServerConversationMessage;
import com.is.chatmultimedia.models.ServerMessage;

public class Client {

	private Socket connection;
	private BlockingQueue<ServerMessage> messageQueue;
	private Thread inputThread;
	private Thread outputThread;

	// Listeners
	private MessageListener messageListener;

	private static final String HOST_NAME = "localhost";
	private static final int HOST_PORT = 8888;
	
	public Client() {
		messageQueue = new ArrayBlockingQueue<ServerMessage>(20);
	}

	public void run() throws IOException {
		connection = new Socket(HOST_NAME, HOST_PORT);
		ObjectOutputStream output = new ObjectOutputStream(
				connection.getOutputStream());
		output.flush();
		ObjectInputStream input = new ObjectInputStream(
				connection.getInputStream());
		startInputThread(input);
		startOutputThread(output);
	}

	public void stop() throws IOException {
		inputThread.interrupt();
		outputThread.interrupt();
		// !!! ASK SERVER TO CLOSE CONNECTION AS WELL
		connection.close();
	}

	public void sendMessageToServer(ServerMessage message) {
		while (true) {
			try {
				messageQueue.put(message);
				break;
			} catch (InterruptedException e) {
				// continue
			}

		}
	}

	public void registerMessageListener(MessageListener messageListener) {
		this.messageListener = messageListener;
	}

	// !!!! TEMPORAR
	public void sendMessageTo(String from, String to, String messageToSend) {
		ServerConversationMessage message = new ServerConversationMessage(from,
				to, messageToSend);
		sendMessageToServer(message);
	}

	// !! TEMPORAR
	public void login(String username, String password) {
		LoginMessage loginMessage = new LoginMessage(username, password);
		sendMessageToServer(loginMessage);
	}

	public interface MessageListener {

		public void displayMessage(String from, String message);

	}

	private void startInputThread(final ObjectInputStream input) {
		inputThread = new Thread(new Runnable() {

			@Override
			public void run() {
				ClientMessage message;

				while (true) {
					try {
						message = (ClientMessage) input.readObject();
						handleMessageFromServer(message);
					} catch (Exception e) {
						// oops
					}
				}
			}
		});
		inputThread.start();
	}

	private void startOutputThread(final ObjectOutputStream output) {
		outputThread = new Thread(new Runnable() {

			@Override
			public void run() {
				ServerMessage message;
				while (true) {
					try {
						message = messageQueue.take();
						output.writeObject(message);
						output.flush();
					} catch (InterruptedException | IOException e) {
						// continue
					}
				}
			}

		});
		outputThread.start();
	}

	private void handleMessageFromServer(ClientMessage message) {
		switch (message.getMessageType()) {
		case CONVERSATION:
			ClientConversationMessage conversationMessage = (ClientConversationMessage) message;
			messageListener.displayMessage(conversationMessage.getSourceUser(),
					conversationMessage.getMessageToSend());
			break;
		case LOGIN_RESPONSE:
			LoginResponseMessage loginResponse = (LoginResponseMessage) message;
			// TEMPORAR
			messageListener.displayMessage("Server", "Logged in successful: " + loginResponse.isSuccessful() + ". " + loginResponse.getMessage());
			break;
		}
	}

}
