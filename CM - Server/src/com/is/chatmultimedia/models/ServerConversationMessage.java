package com.is.chatmultimedia.models;

public class ServerConversationMessage extends ServerMessage {

  private String sourceUser;
  private String targetUser;
  private String messageToSend;
  private static final long serialVersionUID = 1;

  public ServerConversationMessage(String soruceUser, String targetUser, String messageToSend) {
    super(ServerMessageType.CONVERSATION);
    this.sourceUser = soruceUser;
    this.targetUser = targetUser;
    this.messageToSend = messageToSend;
  }

  public String getSourceUser() {
    return this.sourceUser;
  }

  public String getTargetUser() {
    return this.targetUser;
  }

  public String getMessageToSend() {
    return this.messageToSend;
  }

}
