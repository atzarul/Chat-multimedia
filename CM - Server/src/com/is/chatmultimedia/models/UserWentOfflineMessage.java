package com.is.chatmultimedia.models;

public class UserWentOfflineMessage extends ClientMessage {

  private String username;
  private static final long serialVersionUID = 1;

  public UserWentOfflineMessage(String username) {
    super(ClientMessageType.USER_WENT_OFFLINE);
    this.username = username;
  }

  public String getUsername() {
    return this.username;
  }

}
