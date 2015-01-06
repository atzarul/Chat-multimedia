package com.is.chatmultimedia.models;

public class NewFriendMessage extends ClientMessage {

  private Friend friend;
  private static final long serialVersionUID = 1;

  public NewFriendMessage(Friend friend) {
    super(ClientMessageType.NEW_FRIEND);
    this.friend = friend;
  }

  public Friend getFriend() {
    return this.friend;
  }

}
