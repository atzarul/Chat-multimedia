package com.is.chatmultimedia.server.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {

  private Connection connection;
  private static Database instance;
  private static final String CONNECTION_URL = "jdbc:mysql://localhost/is-chatmultimedia?user=root&password=";

  private Database() {
  }

  public static Database getInstance() {
    if (instance == null) {
      instance = new Database();
    }
    return instance;
  }

  public Connection getConnection() throws SQLException {
    if (connection == null || connection.isClosed()) {
      try {
        if (connection != null) {
          Class.forName("com.mysql.jdbc.Driver").newInstance();
        }
        connection = DriverManager.getConnection(CONNECTION_URL);
      }
      catch (Exception e) {
        // error
        return null;
      }
    }
    return connection;
  }

}
