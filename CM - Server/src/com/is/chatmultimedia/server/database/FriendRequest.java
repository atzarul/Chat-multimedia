package com.is.chatmultimedia.server.database;

public class FriendRequest {

  private String fromUsername;
  private String fromName;
  private String toUsername;

  public FriendRequest(String fromUsername, String fromName, String toUsername) {
    this.fromUsername = fromUsername;
    this.fromName = fromName;
    this.toUsername = toUsername;
  }

  public String getFromUsername() {
    return fromUsername;
  }

  public String getFromName() {
    return fromName;
  }

  public String getToUsername() {
    return toUsername;
  }

}
