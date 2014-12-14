package com.is.chatmultimedia.server.database;

public class UserRecord {

  private String username;
  private String password;
  private String name;

  public UserRecord(String username, String password, String name) {
    this.username = username;
    this.password = password;
    this.name = name;
  }

  public String getUsername() {
    return username;
  }

  public String getPassword() {
    return password;
  }

  public String getName() {
    return name;
  }

}
