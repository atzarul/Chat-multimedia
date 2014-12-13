package com.is.chatmultimedia.server.models;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Connection {

  private Socket socket;
  private ObjectInputStream inputStream;
  private ObjectOutputStream outputStream;

  public Connection(Socket socket, ObjectInputStream inputStream, ObjectOutputStream outputStream) {
    this.socket = socket;
    this.inputStream = inputStream;
    this.outputStream = outputStream;
  }

  public Socket getSocket() {
    return socket;
  }

  public ObjectInputStream getInputStream() {
    return inputStream;
  }

  public ObjectOutputStream getOutputStream() {
    return outputStream;
  }

}
