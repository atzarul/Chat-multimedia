package com.is.chatmultimedia.server.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.is.chatmultimedia.models.Friend;
import com.is.chatmultimedia.models.FriendRequest;
import com.is.chatmultimedia.models.User;

public class DatabaseOperations {

  private Database database;
  private static final String GET_USER_RECORD = "SELECT password, name FROM users WHERE username = \'%s\'";
  private static final String GET_USER_FRIENDS = "SELECT friends.friendUsername, users.name FROM friends, users WHERE friends.username = \'%s\' and users.username = friends.friendUsername";
  private static final String ADD_FRIEND = "INSERT INTO friends VALUES(\'%s\', \'%s\'), (\'%s\', \'%s\')";
  private static final String DELETE_FRIEND = "DELETE FROM friends WHERE (username = \'%s\' and friendUsername = \'%s\') or (username = \'%s\' and friendUsername = \'%s\')";
  private static final String ADD_NEW_USER = "INSERT INTO users VALUES(\'%s\', \'%s\', \'%s\')";
  private static final String ADD_FRIEND_REQUEST = "INSERT INTO friendRequests VALUES(\'%s\', \'%s\')";
  private static final String DELETE_FRIEND_REQUEST = "DELETE FROM friendRequests WHERE fromUsername = \'%s\' and toUsername = \'%s\'";
  private static final String GET_ALL_REQUEST_FOR_USER = "SELECT friendrequests.fromUsername, users.name FROM friendrequests, users WHERE toUsername = \'%s\' and users.username = friendrequests.fromUsername";

  public DatabaseOperations() {
    this.database = Database.getInstance();
  }

  public UserRecord getUserRecord(String username) throws SQLException {
    Connection databaseConnection = database.getConnection();
    Statement statement = databaseConnection.createStatement();
    String query = String.format(GET_USER_RECORD, username);
    statement.executeQuery(query);
    ResultSet result = statement.getResultSet();
    if (result.next()) {
      String password = result.getString(1);
      String name = result.getString(2);
      return new UserRecord(username, password, name);
    }
    return null;
  }

  public User getUserData(String username) throws SQLException {
    UserRecord user = getUserRecord(username);
    if (user != null) {
      Connection databaseConnection = database.getConnection();
      Statement statement = databaseConnection.createStatement();
      String query = String.format(GET_USER_FRIENDS, username);
      statement.executeQuery(query);

      ResultSet results = statement.getResultSet();
      ArrayList<Friend> listOfFriends = new ArrayList<>();
      String friendUsername, friendName;
      while (results.next()) {
        friendUsername = results.getString(1);
        friendName = results.getString(2);
        listOfFriends.add(new Friend(friendUsername, friendName));
      }
      return new User(username, user.getName(), listOfFriends);
    }

    return null;
  }

  public void addFriend(String username, String friendsUsername) throws SQLException {
    Connection databaseConnection = database.getConnection();
    Statement statement = databaseConnection.createStatement();
    String query = String.format(ADD_FRIEND, username, friendsUsername, friendsUsername, username);
    statement.executeUpdate(query);
  }

  public void deleteFriend(String username, String friendUsername) throws SQLException {
    Connection databaseConnection = database.getConnection();
    Statement statement = databaseConnection.createStatement();
    String query = String.format(DELETE_FRIEND, username, friendUsername, friendUsername, username);
    statement.executeUpdate(query);
  }

  public void addNewUser(String username, String password, String name) throws SQLException {
    Connection databaseConnection = database.getConnection();
    Statement statement = databaseConnection.createStatement();
    String query = String.format(ADD_NEW_USER, username, password, name);
    statement.executeUpdate(query);
  }

  public List<FriendRequest> getAllFriendRequestsForUser(String username) throws SQLException {
    Connection databaseConnection = database.getConnection();
    Statement statement = databaseConnection.createStatement();
    String query = String.format(GET_ALL_REQUEST_FOR_USER, username);
    ResultSet results = statement.executeQuery(query);
    ArrayList<FriendRequest> friendRequest = new ArrayList<>();
    String fromUsername, fromName;
    while (results.next()) {
      fromUsername = results.getString(1);
      fromName = results.getString(2);
      friendRequest.add(new FriendRequest(fromUsername, fromName, username));
    }
    return friendRequest;
  }

  public void addFriendRequest(String username, String friendUsername) throws SQLException {
    Connection databaseConnection = database.getConnection();
    Statement statement = databaseConnection.createStatement();
    String query = String.format(ADD_FRIEND_REQUEST, username, friendUsername);
    statement.executeUpdate(query);
  }

  public void deleteFriendRequest(String username, String friendUsername) throws SQLException {
    Connection databaseConnection = database.getConnection();
    Statement statement = databaseConnection.createStatement();
    String query = String.format(DELETE_FRIEND_REQUEST, username, friendUsername);
    statement.executeUpdate(query);
  }

}
