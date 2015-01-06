package com.is.chatmultimedia.models;

public class DeleteFriendResponseMessage extends ClientMessage {

  private boolean isSuccessful;
  private String message;
  private static final long serialVersionUID = 1;

  public DeleteFriendResponseMessage(boolean isSuccessful, String message) {
    super(ClientMessageType.DELETE_FRIEND_RESPONSE);
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
