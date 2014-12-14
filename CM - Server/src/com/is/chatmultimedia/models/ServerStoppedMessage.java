package com.is.chatmultimedia.models;

public class ServerStoppedMessage extends ClientMessage {

  private static final long serialVersionUID = 1;

  public ServerStoppedMessage() {
    super(ClientMessageType.SERVER_STOPPED);
  }

}
