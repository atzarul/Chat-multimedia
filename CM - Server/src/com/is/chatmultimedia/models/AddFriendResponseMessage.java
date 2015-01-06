package com.is.chatmultimedia.models;

public class AddFriendResponseMessage extends ClientMessage {

  private boolean isSuccessful;
  private String message;
  private static final long serialVersionUID = 1;

  public AddFriendResponseMessage(boolean isSuccessful, String message) {
    super(ClientMessageType.ADD_FRIEND_RESPONSE);
    this.isSuccessful = isSuccessful;
    this.message = message;
  }

  public boolean isSuccessful() {
    return isSuccessful;
  }

  public String getMessage() {
    return message;
  }

}
