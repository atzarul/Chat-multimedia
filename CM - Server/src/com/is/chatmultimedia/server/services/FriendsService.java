package com.is.chatmultimedia.server.services;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.sql.SQLException;

import com.is.chatmultimedia.models.AddFriendMessage;
import com.is.chatmultimedia.models.AddFriendResponseMessage;
import com.is.chatmultimedia.models.ClientMessage;
import com.is.chatmultimedia.models.DeleteFriendMessage;
import com.is.chatmultimedia.models.DeleteFriendResponseMessage;
import com.is.chatmultimedia.models.Friend;
import com.is.chatmultimedia.models.FriendRequest;
import com.is.chatmultimedia.models.FriendRequestMessage;
import com.is.chatmultimedia.models.FriendRequestResponseMessage;
import com.is.chatmultimedia.models.NewFriendMessage;
import com.is.chatmultimedia.models.ServerMessage;
import com.is.chatmultimedia.server.UserManager;
import com.is.chatmultimedia.server.database.DatabaseOperations;
import com.is.chatmultimedia.server.database.UserRecord;
import com.is.chatmultimedia.server.models.Connection;
import com.is.chatmultimedia.server.models.User;

public class FriendsService {

  private DatabaseOperations databaseOperations;
  private UserManager userManager;

  private static FriendsService instance;
  private static final String USER_DOESNT_EXIST = "User doesn't exist!";
  private static final String FRIEND_REQUST_SENT = "Friend request successfully sent!";
  private static final String OPERATION_FAILED = "Operation failed!";
  private static final String DELETE_SUCCESFUL = "Delete successful!";

  private FriendsService() {
    databaseOperations = new DatabaseOperations();
    userManager = UserManager.getInstance();
  }

  public static FriendsService getInstance() {
    if (instance == null) {
      instance = new FriendsService();
    }
    return instance;
  }

  public boolean serverRequest(ServerMessage message, Connection sourceConnection) {
    switch (message.getMessageType()) {
    case ADD_FRIEND:
      return addFriendMessage((AddFriendMessage) message, sourceConnection);
    case DELETE_FRIEND:
      return deleteFriendMessage((DeleteFriendMessage) message, sourceConnection);
    case FRIEND_REQUEST_RESPONSE:
      return friendRequestResponse((FriendRequestResponseMessage) message, sourceConnection);
    default:
      return false;
    }
  }

  private boolean addFriendMessage(AddFriendMessage message, Connection sourceConnection) {
    AddFriendResponseMessage response;
    try {
      UserRecord user = databaseOperations.getUserRecord(message.getTargetUsername());
      if (user == null) { // user doens't exist
        response = new AddFriendResponseMessage(false, USER_DOESNT_EXIST);
      }
      else {
        databaseOperations.addFriendRequest(message.getSourceUsername(), message.getTargetUsername());
        response = new AddFriendResponseMessage(true, FRIEND_REQUST_SENT);
        User targetUser = userManager.getUserByUsername(message.getTargetUsername());
        if (targetUser != null) { // send friend request if target user is online
          FriendRequest request = new FriendRequest(message.getSourceUsername(), message.getSourceName(),
              targetUser.getUsername());
          FriendRequestMessage requestMessage = new FriendRequestMessage(request);
          writeResponse(requestMessage, targetUser.getConnection());
        }
      }
      writeResponse(response, sourceConnection);
    }
    catch (SQLException e) {
      response = new AddFriendResponseMessage(false, OPERATION_FAILED);
      writeResponse(response, sourceConnection);
      return false;
    }
    return true;
  }

  private boolean deleteFriendMessage(DeleteFriendMessage message, Connection sourceConnection) {
    DeleteFriendResponseMessage response;
    try {
      databaseOperations.deleteFriend(message.getSourceUsername(), message.getTargetUsername());
      response = new DeleteFriendResponseMessage(true, DELETE_SUCCESFUL);
      writeResponse(response, sourceConnection);
    }
    catch (SQLException e) {
      response = new DeleteFriendResponseMessage(false, OPERATION_FAILED);
      writeResponse(response, sourceConnection);
      return false;
    }
    return true;
  }

  private boolean friendRequestResponse(FriendRequestResponseMessage message, Connection sourceConnection) {
    FriendRequest friendRequest = message.getFriendRequest();
    try {
      if (message.isAccepted()) {
        databaseOperations.addFriend(friendRequest.getFromUsername(), friendRequest.getToUsername());
        User user = userManager.getUserByUsername(friendRequest.getFromUsername());
        NewFriendMessage response = new NewFriendMessage(new Friend(friendRequest.getFromName(),
            friendRequest.getFromUsername(), user == null ? false : true));
        writeResponse(response, sourceConnection);
        if (user != null) {
          UserRecord userRecord = databaseOperations.getUserRecord(friendRequest.getToUsername());
          response = new NewFriendMessage(new Friend(userRecord.getName(), userRecord.getUsername(), true));
          writeResponse(response, user.getConnection());
        }
      }
      databaseOperations.deleteFriendRequest(friendRequest.getFromUsername(), friendRequest.getToUsername());
    }
    catch (SQLException e) {
      return false;
    }
    return true;
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
