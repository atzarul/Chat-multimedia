package com.is.chatmultimedia.models;

public class RegisterMessage extends ServerMessage {

  private String username;
  private String password;
  private String name;
  private static final long serialVersionUID = 1;

  public RegisterMessage(String username, String password, String name) {
    super(ServerMessageType.REGISTER);
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
