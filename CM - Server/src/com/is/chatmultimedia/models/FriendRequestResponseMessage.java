package com.is.chatmultimedia.models;

public class FriendRequestResponseMessage extends ServerMessage {

  private boolean isAccepted;
  private FriendRequest friendRequest;
  private static final long serialVersionUID = 1;

  public FriendRequestResponseMessage(boolean isAccepted, FriendRequest friendRequest) {
    super(ServerMessageType.FRIEND_REQUEST_RESPONSE);
    this.isAccepted = isAccepted;
    this.friendRequest = friendRequest;
  }

  public boolean isAccepted() {
    return isAccepted;
  }

  public FriendRequest getFriendRequest() {
    return friendRequest;
  }

}
