package com.is.chatmultimedia.server.models;


public class User {

  private String username;
  private Connection connection;

  public User(String username, Connection connection) {
    this.username = username;
    this.connection = connection;
  }

  public String getUsername() {
    return this.username;
  }

  public Connection getConnection() {
    return this.connection;
  }

}
