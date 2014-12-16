package com.is.chatmultimedia.server.services;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.sql.SQLException;

import com.is.chatmultimedia.models.ClientMessage;
import com.is.chatmultimedia.models.RegisterMessage;
import com.is.chatmultimedia.models.RegisterResponseMessage;
import com.is.chatmultimedia.models.ServerMessage;
import com.is.chatmultimedia.models.ServerMessage.ServerMessageType;
import com.is.chatmultimedia.server.database.DatabaseOperations;
import com.is.chatmultimedia.server.models.Connection;

public class RegisterService {

  private DatabaseOperations databaseOperations;
  private static RegisterService instance;
  private static final String REGISTER_SUCCESSFUL = "Register successful!";
  private static final String USERNAME_ALREADY_EXISTS = "Username already exists!";

  private RegisterService() {
    this.databaseOperations = new DatabaseOperations();
  }

  public static RegisterService getInstance() {
    if (instance == null) {
      instance = new RegisterService();
    }
    return instance;
  }

  public boolean serveRequest(ServerMessage message, Connection userConnection) {
    if (message.getMessageType() != ServerMessageType.REGISTER) {
      return false;
    }
    RegisterResponseMessage responseMessage;
    RegisterMessage registerMessage = (RegisterMessage) message;
    try {
      databaseOperations.addNewUser(registerMessage.getUsername(), registerMessage.getPassword(),
          registerMessage.getName());
      responseMessage = new RegisterResponseMessage(true, REGISTER_SUCCESSFUL);
    }
    catch (SQLException e) {
      responseMessage = new RegisterResponseMessage(false, USERNAME_ALREADY_EXISTS);
    }
    writeResponse(responseMessage, userConnection);
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
