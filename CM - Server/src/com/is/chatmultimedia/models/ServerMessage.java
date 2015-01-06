package com.is.chatmultimedia.models;

import java.io.Serializable;

public abstract class ServerMessage implements Serializable {

  private static final long serialVersionUID = 1;

  public enum ServerMessageType {
    REGISTER, LOGIN, LOGOUT, CONVERSATION, ADD_FRIEND, DELETE_FRIEND, FRIEND_REQUEST_RESPONSE, CLOSE_CONNECTION
  }

  private ServerMessageType messageType;

  public ServerMessage(ServerMessageType messageType) {
    this.messageType = messageType;
  }

  public ServerMessageType getMessageType() {
    return this.messageType;
  }

}
