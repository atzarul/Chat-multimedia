package com.is.chatmultimedia.client;

import com.is.chatmultimedia.models.Friend;

public interface FriendsManagerListener {

  public void userCameOnline(String username);

  public void userWentOffline(String username);

  public void newFriendAdded(Friend friend);

}
