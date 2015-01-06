package com.is.chatmultimedia.client;

import com.is.chatmultimedia.models.FriendRequest;

public interface FriendRequestListener {

  public void requestSentSuccessful(String message);

  public void requestSentFailed(String message);

  public void newFriendRequest(FriendRequest friendRequest);

}
