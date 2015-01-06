package com.is.chatmultimedia.server.services;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.is.chatmultimedia.models.ClientMessage;
import com.is.chatmultimedia.models.Friend;
import com.is.chatmultimedia.models.LoginMessage;
import com.is.chatmultimedia.models.LoginResponseMessage;
import com.is.chatmultimedia.models.LogoutMessage;
import com.is.chatmultimedia.models.ServerMessage;
import com.is.chatmultimedia.models.UserCameOnlineMessage;
import com.is.chatmultimedia.models.UserWentOfflineMessage;
import com.is.chatmultimedia.server.UserManager;
import com.is.chatmultimedia.server.database.DatabaseOperations;
import com.is.chatmultimedia.server.database.UserRecord;
import com.is.chatmultimedia.server.models.Connection;
import com.is.chatmultimedia.server.models.User;

public class AuthenticationService {

  private UserManager userManager;
  private DatabaseOperations databaseOperations;
  private static AuthenticationService instance;
  private static final String LOGIN_SUCCESSFUL = "Login successful!";
  private static final String INCORRECT_USERNAME = "Incorrect username";
  private static final String INCORRECT_PASSWORD = "Incorrect password!";

  private AuthenticationService() {
    userManager = UserManager.getInstance();
    databaseOperations = new DatabaseOperations();
  }

  public static AuthenticationService getInstance() {
    if (instance == null) {
      instance = new AuthenticationService();
    }
    return instance;
  }

  public boolean serveRequest(ServerMessage message, Connection sourceConnection) {
    switch (message.getMessageType()) {
    case LOGIN:
      return login((LoginMessage) message, sourceConnection);
    case LOGOUT:
      return logout((LogoutMessage) message);
    default:
      return false;
    }
  }

  private boolean login(LoginMessage loginMessage, Connection userConnection) {
    LoginResponseMessage responseMessage;
    try {
      UserRecord userRecord = databaseOperations.getUserRecord(loginMessage.getUsername());
      if (userRecord == null) { // user doens't exist
        responseMessage = new LoginResponseMessage(false, INCORRECT_USERNAME, null);
      }
      else {
        String correctPassword = userRecord.getPassword();
        String inputPassword = new String(loginMessage.getPassword());
        if (correctPassword.compareTo(inputPassword) != 0) { // incorrect password
          responseMessage = new LoginResponseMessage(false, INCORRECT_PASSWORD, null);
        }
        else {
          com.is.chatmultimedia.models.User userData = databaseOperations.getUserData(loginMessage.getUsername());
          List<User> onlineFriends = getOnlineFriendsForUser(userData);
          responseMessage = new LoginResponseMessage(true, LOGIN_SUCCESSFUL, userData);
          notifyFriendsThatUserCameOnline(loginMessage.getUsername(), onlineFriends);
          // save user as logged in
          User user = new User(loginMessage.getUsername(), userConnection);
          userManager.addUser(user);
          // TEST MESSAGE
          System.out.println("User - " + user.getUsername() + " - logged in! Logged in users ("
              + userManager.getNumberOfUsers() + "): " + userManager.getListOfUsers());
        }
      }
      // write response to user
      writeResponse(responseMessage, userConnection);
      return true;
    }
    catch (SQLException e) {
      // database query failed
    }
    return false;
  }

  private boolean logout(LogoutMessage logoutMessage) {
    User targetUser = userManager.getUserByUsername(logoutMessage.getUser().getUsername());
    if (targetUser != null) {
      userManager.removeUser(targetUser);
      // TEST MESSAGE
      System.out.println("User - " + targetUser.getUsername() + " - logged out! Logged in users ("
          + userManager.getNumberOfUsers() + "): " + userManager.getListOfUsers());
      notifyFriendsThatUserWentOffline(logoutMessage.getUser());
      return true;
    }
    return false;
  }

  private void writeResponse(ClientMessage message, Connection userConnection) {
    try {
      ObjectOutputStream output = userConnection.getOutputStream();
      output.writeObject(message);
      output.flush();
    }
    catch (IOException e) {
      // oops
    }
  }

  private List<User> getOnlineFriendsForUser(com.is.chatmultimedia.models.User user) {
    List<User> onlineFriends = new ArrayList<User>();
    Collection<Friend> allFriends = user.getFriends();
    User aUser = null;
    for (Friend it : allFriends) {
      aUser = userManager.getUserByUsername(it.getUsername());
      if (aUser != null) { // user is online
        onlineFriends.add(aUser);
        it.setIsOnline(true);
      }
    }
    return onlineFriends;
  }

  private void notifyFriendsThatUserCameOnline(String usernameThatCameOnline, List<User> onlineFriends) {
    if (onlineFriends.size() > 0) {
      new Runnable() {

        @Override
        public void run() {
          ObjectOutputStream output;
          UserCameOnlineMessage message = new UserCameOnlineMessage(usernameThatCameOnline);
          for (User it : onlineFriends) {
            try {
              output = it.getConnection().getOutputStream();
              output.writeObject(message);
              output.flush();
            }
            catch (IOException e) {
            }
          }
        }

      }.run();
    }
  }

  private void notifyFriendsThatUserWentOffline(com.is.chatmultimedia.models.User user) {
    new Runnable() {

      @Override
      public void run() {
        ObjectOutputStream output;
        Collection<Friend> friends = user.getFriends();
        User serverUser;
        UserWentOfflineMessage message = new UserWentOfflineMessage(user.getUsername());
        for (Friend it : friends) {
          try {
            if (it.isOnline()) {
              serverUser = userManager.getUserByUsername(it.getUsername());
              if (serverUser != null) { // friend is online, notify him
                output = serverUser.getConnection().getOutputStream();
                output.writeObject(message);
                output.flush();
              }
            }
          }
          catch (IOException e) {
          }
        }
      }
    }.run();
  }
}
