package com.is.chatmultimedia.models;

public class CloseConnectionMessage extends ServerMessage {

  private static final long serialVersionUID = 1;

  public CloseConnectionMessage() {
    super(ServerMessageType.CLOSE_CONNECTION);
  }

}
