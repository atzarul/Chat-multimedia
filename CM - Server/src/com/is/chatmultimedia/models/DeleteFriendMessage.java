package com.is.chatmultimedia.models;

public class DeleteFriendMessage extends ServerMessage {

  private String sourceUsername;
  private String targetUsername;
  private static final long serialVersionUID = 1;

  public DeleteFriendMessage(String sourceUsername, String targetUsername) {
    super(ServerMessage.ServerMessageType.DELETE_FRIEND);
    this.sourceUsername = sourceUsername;
    this.targetUsername = targetUsername;
  }

  public String getSourceUsername() {
    return sourceUsername;
  }

  public String getTargetUsername() {
    return targetUsername;
  }

}
