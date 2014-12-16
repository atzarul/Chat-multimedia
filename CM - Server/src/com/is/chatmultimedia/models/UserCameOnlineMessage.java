package com.is.chatmultimedia.models;

public class UserCameOnlineMessage extends ClientMessage {

  private String username;
  private static final long serialVersionUID = 1;

  public UserCameOnlineMessage(String username) {
    super(ClientMessageType.USER_CAME_ONLINE);
    this.username = username;
  }

  public String getUsername() {
    return this.username;
  }

}
