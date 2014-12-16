package com.is.chatmultimedia.models;

import java.io.Serializable;

public class Friend implements Serializable {

  private String name;
  private String username;
  private boolean isOnline;
  private static final long serialVersionUID = 1;

  public Friend(String name, String username) {
    this(name, username, false);
  }

  public Friend(String name, String username, boolean isOnline) {
    this.name = name;
    this.username = username;
    this.isOnline = isOnline;
  }

  public String getName() {
    return name;
  }

  public String getUsername() {
    return username;
  }

  public void setIsOnline(boolean isOnline) {
    this.isOnline = isOnline;
  }

  public boolean isOnline() {
    return this.isOnline;
  }

}
