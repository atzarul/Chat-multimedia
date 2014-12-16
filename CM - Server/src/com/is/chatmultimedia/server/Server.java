package com.is.chatmultimedia.server;

import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

import com.is.chatmultimedia.models.ServerMessage;
import com.is.chatmultimedia.models.ServerStoppedMessage;
import com.is.chatmultimedia.server.models.Connection;
import com.is.chatmultimedia.server.services.AuthenticationService;
import com.is.chatmultimedia.server.services.ConversationService;
import com.is.chatmultimedia.server.services.RegisterService;

public class Server {

  private ServerSocket serverSocket;
  private List<Connection> connections;
  private volatile boolean started = false;
  private volatile boolean stopped = false;

  // server services
  private AuthenticationService authenticationService;
  private ConversationService conversationService;
  private RegisterService registerService;

  private static Server instance;
  private static final int PORT = 8888;

  /**
   * Returns the server instance.
   * 
   * @return The server instance.
   */
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
    registerService = RegisterService.getInstance();
  }

  /**
   * Starts the sever if the server isn't already running.
   * 
   * @return True if the server has started with this call, false otherwise.
   */
  public boolean start() {
    if (!started) {
      started = true;
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
                ClientThread clientThread = ClientThread.getInstance(clientSocket);
                clientThread.start();
                addConnection(clientThread.getThreadsConnection());
              }
              catch (SocketTimeoutException timeOutException) {
                if (stopped) {
                  break;
                }
              }
              catch (IOException getInstanceException) {
                // creating client thread failed
              }
            }
          }
          catch (IOException e) {
            // server failed to start
            started = false;
          }
        }
      }.run();
    }
    return false;
  }

  /**
   * Stops the server and closes the connections with all the clients. The clients are notified that the server is
   * stopping.
   */
  public synchronized void stop() {
    stopped = true;
    notifyClientsThatServerIsClosingAndCloseConnections();
  }

  /**
   * Processed a message coming from a client.
   * 
   * @param message The incoming message.
   * @param sourceConnection The connection with the client that has sent the message.
   * @return True if the action specified in the message has completed successfully.
   */
  public boolean processMessage(ServerMessage message, Connection sourceConnection) {
    switch (message.getMessageType()) {
    case REGISTER:
      return registerService.serveRequest(message, sourceConnection);
    case LOGIN:
    case LOGOUT:
      return authenticationService.serveRequest(message, sourceConnection);
    case CONVERSATION:
      return conversationService.serveRequest(message);
    case CLOSE_CONNECTION:
      return removeConnecion(sourceConnection);
    }
    return false;
  }

  private synchronized boolean addConnection(Connection connection) {
    return connections.add(connection);
  }

  /*
   * Closes the specified connection.
   * 
   * @param connection The connection to be closed.
   */
  private synchronized boolean removeConnecion(Connection connection) {
    Socket socket = connection.getSocket();
    if (!socket.isClosed()) {
      try {
        socket.close();
      }
      catch (IOException e) {
        // closing failed ?!
        return false;
      }
    }
    return connections.remove(connection);
  }

  /*
   * Notifies all clients that the server is stopping and closes all connections with the clients.
   */
  private void notifyClientsThatServerIsClosingAndCloseConnections() {
    ObjectOutputStream output;
    ServerStoppedMessage message = new ServerStoppedMessage();
    for (Connection it : connections) {
      try {
        output = it.getOutputStream();
        output.writeObject(message);
        output.flush();
        it.getSocket().close();
      }
      catch (IOException e) {
        // continue
      }
    }
    connections.clear();
  }

}
