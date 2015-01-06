package com.is.chatmultimedia.models;

public class AddFriendMessage extends ServerMessage {

  private String sourceUsername;
  private String sourceName;
  private String targetUsername;
  private static final long serialVersionUID = 1;

  public AddFriendMessage(String sourceUsername, String sourceName, String targetUsername) {
    super(ServerMessage.ServerMessageType.ADD_FRIEND);
    this.sourceUsername = sourceUsername;
    this.sourceName = sourceName;
    this.targetUsername = targetUsername;
  }

  public String getSourceUsername() {
    return sourceUsername;
  }

  public String getSourceName() {
    return this.sourceName;
  }

  public String getTargetUsername() {
    return targetUsername;
  }

}
