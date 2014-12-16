package com.is.chatmultimedia.models;

public class RegisterResponseMessage extends ClientMessage {

  private boolean successful;
  private String message;
  private static final long serialVersionUID = 1;

  public RegisterResponseMessage(boolean successful, String message) {
    super(ClientMessageType.REGISTER_RESPONSE);
    this.successful = successful;
    this.message = message;
  }

  public boolean isSuccessful() {
    return this.successful;
  }

  public String getMessage() {
    return this.message;
  }

}
