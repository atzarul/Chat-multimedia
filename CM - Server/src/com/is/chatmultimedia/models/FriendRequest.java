package com.is.chatmultimedia.models;

import java.io.Serializable;

public class FriendRequest implements Serializable {

  private String fromUsername;
  private String fromName;
  private String toUsername;
  private static final long serialVersionUID = 1;

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
