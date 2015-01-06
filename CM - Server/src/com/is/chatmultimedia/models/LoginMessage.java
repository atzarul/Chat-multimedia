package com.is.chatmultimedia.models;

public class LoginMessage extends ServerMessage {

  private String username;
  private char[] password;
  private static final long serialVersionUID = 1;

  public LoginMessage(String username, char[] password) {
    super(ServerMessageType.LOGIN);
    this.username = username;
    this.password = password;
  }

  public String getUsername() {
    return this.username;
  }

  public char[] getPassword() {
    return this.password;
  }

}
