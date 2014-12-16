package com.is.chatmultimedia.models;

public class LogoutMessage extends ServerMessage {

  private User user;
  private static final long serialVersionUID = 1;

  public LogoutMessage(User user) {
    super(ServerMessageType.LOGOUT);
    this.user = user;
  }

  public User getUser() {
    return this.user;
  }

}
