package com.is.chatmultimedia.server.services;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.sql.SQLException;

import com.is.chatmultimedia.models.ClientMessage;
import com.is.chatmultimedia.models.LoginMessage;
import com.is.chatmultimedia.models.LoginResponseMessage;
import com.is.chatmultimedia.models.LogoutMessage;
import com.is.chatmultimedia.models.ServerMessage;
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

  public boolean serverRequest(ServerMessage message, Connection sourceConnection) {
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
        responseMessage = new LoginResponseMessage(false, INCORRECT_USERNAME);
      }
      else {
        String correctPassword = userRecord.getPassword();
        String inputPassword = loginMessage.getPassword();
        if (correctPassword.compareTo(inputPassword) != 0) { // incorrect password
          responseMessage = new LoginResponseMessage(false, INCORRECT_PASSWORD);
        }
        else {
          responseMessage = new LoginResponseMessage(true, LOGIN_SUCCESSFUL);
        }
      }
      // save user ass logged in
      User user = new User(loginMessage.getUsername(), userConnection);
      userManager.addUser(user);
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
    User targetUser = userManager.getUserByUsername(logoutMessage.getUsername());
    if (targetUser != null) {
      userManager.removeUser(targetUser);
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

}
