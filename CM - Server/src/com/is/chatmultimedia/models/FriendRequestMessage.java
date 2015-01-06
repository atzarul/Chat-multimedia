package com.is.chatmultimedia.models;

public class FriendRequestMessage extends ClientMessage {

  private FriendRequest friendRequest;
  private static final long serialVersionUID = 1;

  public FriendRequestMessage(FriendRequest friendRequest) {
    super(ClientMessageType.FRIEND_REQUEST);
    this.friendRequest = friendRequest;
  }

  public FriendRequest getFriendRequst() {
    return this.friendRequest;
  }

}
