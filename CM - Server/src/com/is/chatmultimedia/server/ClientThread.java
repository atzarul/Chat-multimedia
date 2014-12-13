package com.is.chatmultimedia.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import com.is.chatmultimedia.models.ServerMessage;
import com.is.chatmultimedia.server.models.Connection;

public class ClientThread extends Thread {

  private Connection connection;

  public static ClientThread getInstance(Socket clientSocket) throws IOException {

    ObjectOutputStream outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
    outputStream.flush();
    ObjectInputStream inputStream = new ObjectInputStream(clientSocket.getInputStream());
    ClientThread clientThread = new ClientThread(clientSocket, inputStream, outputStream);
    return clientThread;
  }

  @Override
  public void run() {
    Socket clientSocket = connection.getSocket();
    ObjectInputStream input = connection.getInputStream();
    while (clientSocket.isConnected()) {
      try {
        ServerMessage serverMessage = (ServerMessage) input.readObject();
        Server.getInstance().processMessage(serverMessage, connection);
      }
      catch (Exception e) {
        // message failed
      }
    }
  }

  private ClientThread(Socket clientSocket, ObjectInputStream inputStream, ObjectOutputStream outputStream) {
    this.connection = new Connection(clientSocket, inputStream, outputStream);
  }

}
