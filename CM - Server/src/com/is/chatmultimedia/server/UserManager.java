package com.is.chatmultimedia.server;

import java.util.HashMap;
import java.util.Map;

import com.is.chatmultimedia.server.models.User;

public class UserManager {

  private Map<String, User> users;
  private static UserManager instance;

  private UserManager() {
    users = new HashMap<String, User>();
  }

  public static UserManager getInstance() {
    if (instance == null) {
      instance = new UserManager();
    }
    return instance;
  }

  public void addUser(User user) {
    users.put(user.getUsername(), user);
  }

  public User removeUser(User user) {
    return users.remove(user.getClass());
  }

  public User getUserByUsername(String username) {
    return users.get(username);
  }

}
