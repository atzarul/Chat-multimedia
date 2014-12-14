package com.is.chatmultimedia.client.models;

import java.util.List;

public class User {

  private String username;
  private String name;
  private List<Friend> friends;

  public User(String username, String name, List<Friend> friends) {
    this.username = username;
    this.name = name;
    this.friends = friends;
  }

  public String getUsername() {
    return username;
  }

  public String getName() {
    return name;
  }

  public List<Friend> getFriends() {
    return friends;
  }

}
