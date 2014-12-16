package com.is.chatmultimedia.models;

import java.io.Serializable;
import java.util.List;

public class User implements Serializable {

  private String username;
  private String name;
  private List<Friend> friends;
  private static final long serialVersionUID = 1;

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
