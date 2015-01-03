package com.is.chatmultimedia.client;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import com.is.chatmultimedia.models.ClientConversationMessage;
import com.is.chatmultimedia.models.ClientMessage;
import com.is.chatmultimedia.models.CloseConnectionMessage;
import com.is.chatmultimedia.models.Friend;
import com.is.chatmultimedia.models.LoginMessage;
import com.is.chatmultimedia.models.LoginResponseMessage;
import com.is.chatmultimedia.models.LogoutMessage;
import com.is.chatmultimedia.models.RegisterMessage;
import com.is.chatmultimedia.models.RegisterResponseMessage;
import com.is.chatmultimedia.models.ServerConversationMessage;
import com.is.chatmultimedia.models.ServerMessage;
import com.is.chatmultimedia.models.User;
import com.is.chatmultimedia.models.UserCameOnlineMessage;
import com.is.chatmultimedia.models.UserWentOfflineMessage;

public class Client {

  private Socket connection;
  private BlockingQueue<ServerMessage> messageQueue;
  private Thread inputThread;
  private Thread outputThread;
  private boolean started = false;
  private boolean isAuthorized = false;
  private User loggedInUser;

  // Listeners
  private MessageListener messageListener;
  private AuthenticationListener authenticationListener;
  private RegisterListener registerListener;
  private FriendsManagerListener friendsManagerListener;
  private ServerMessageListener serverMessageListener;

  private static final String HOST_NAME = "localhost";
  private static final int HOST_PORT = 8888;

  public Client() {
    messageQueue = new ArrayBlockingQueue<ServerMessage>(20);
    started = false;
  }

  public void start() throws IOException {
    if (started == false) {
      started = true;
      connection = new Socket(HOST_NAME, HOST_PORT);
      ObjectOutputStream output = new ObjectOutputStream(connection.getOutputStream());
      output.flush();
      ObjectInputStream input = new ObjectInputStream(connection.getInputStream());
      startInputThread(input);
      startOutputThread(output);
    }
  }

  public void stop() throws IOException {
    if (started) {
      if (isAuthorized) {
        this.logout();
      }
      if (connection != null) {
        CloseConnectionMessage closeConnectionMessage = new CloseConnectionMessage();
        sendMessageToServer(closeConnectionMessage);
        outputThread.interrupt();
      }
    }
  }

  public void registerMessageListener(MessageListener messageListener) {
    this.messageListener = messageListener;
  }

  public void registerAuthenticationListener(AuthenticationListener authenticationListener) {
    this.authenticationListener = authenticationListener;
  }

  public void registerRegisterListener(RegisterListener registerListener) {
    this.registerListener = registerListener;
  }

  public void registerFriendsManagerListener(FriendsManagerListener friendsManagerListener) {
    this.friendsManagerListener = friendsManagerListener;
  }

  public void registerServerMessageListener(ServerMessageListener serverMessageListener) {
    this.serverMessageListener = serverMessageListener;
  }

  public boolean isUserAuthorized() {
    return this.isAuthorized;
  }

  public User getLoggedInUser() {
    return this.loggedInUser;
  }

  public boolean login(String username, String password) {
    if (!isAuthorized) {
      LoginMessage loginMessage = new LoginMessage(username, password);
      sendMessageToServer(loginMessage);
      return true;
    }
    return false;
  }

  public boolean logout() {
    if (isAuthorized) {
      LogoutMessage logoutMessage = new LogoutMessage(loggedInUser);
      sendMessageToServer(logoutMessage);
      isAuthorized = false;
      this.loggedInUser = null;
      return true;
    }
    return false;
  }

  public boolean sendMessageTo(String to, String messageToSend) {
    if (isAuthorized) {
      ServerConversationMessage message = new ServerConversationMessage(loggedInUser.getUsername(), to, messageToSend);
      sendMessageToServer(message);
      return true;
    }
    return false;
  }

  public boolean register(String username, String password, String name) {
    if (!isAuthorized) {
      RegisterMessage registerMessage = new RegisterMessage(username, password, name);
      sendMessageToServer(registerMessage);
      return true;
    }
    return false;
  }

  private void sendMessageToServer(ServerMessage message) {
    while (true) {
      try {
        messageQueue.put(message);
        break;
      }
      catch (InterruptedException e) {
        // continue
      }
    }
  }

  private void startInputThread(final ObjectInputStream input) {
    inputThread = new Thread(new Runnable() {
      @Override
      public void run() {
        ClientMessage message;

        while (connection != null && !connection.isClosed()) {
          try {
            message = (ClientMessage) input.readObject();
            handleMessageFromServer(message);
          }
          catch (SocketException | EOFException e) {
            break;
          }
          catch (Exception e) {
            e.printStackTrace();
          }
        }
      }
    });
    inputThread.start();
  }

  private void startOutputThread(final ObjectOutputStream output) {
    outputThread = new Thread(new Runnable() {
      @Override
      public void run() {
        ServerMessage message;
        while (connection != null && !connection.isClosed()) {
          try {
            message = messageQueue.take();
            output.writeObject(message);
            output.flush();
          }
          catch (SocketException | EOFException e) {
            break;
          }
          catch (InterruptedException | IOException e) {
            // continue
          }
        }
      }
    });
    outputThread.start();
  }

  private void handleMessageFromServer(ClientMessage message) {
    switch (message.getMessageType()) {
    case CONVERSATION:
      ClientConversationMessage conversationMessage = (ClientConversationMessage) message;
      if (messageListener != null) {
        messageListener.displayMessage(conversationMessage.getSourceUser(), conversationMessage.getMessageToSend());
      }
      break;
    case LOGIN_RESPONSE:
      LoginResponseMessage loginResponse = (LoginResponseMessage) message;
      handleLoginResponse(loginResponse);
      break;
    case REGISTER_RESPONSE:
      RegisterResponseMessage registerResponse = (RegisterResponseMessage) message;
      handleRegisterResponse(registerResponse);
      break;
    case USER_CAME_ONLINE:
      UserCameOnlineMessage userCameOnlineMessage = (UserCameOnlineMessage) message;
      if (friendsManagerListener != null) {
        Friend friend = loggedInUser.getFriendByUsername(userCameOnlineMessage.getUsername());
        friend.setIsOnline(true);
        friendsManagerListener.userCameOnline(userCameOnlineMessage.getUsername());
      }
      break;
    case USER_WENT_OFFLINE:
      UserWentOfflineMessage userWentOfflineMessage = (UserWentOfflineMessage) message;
      if (friendsManagerListener != null) {
        Friend friend = loggedInUser.getFriendByUsername(userWentOfflineMessage.getUsername());
        friend.setIsOnline(false);
        friendsManagerListener.userWentOffline(userWentOfflineMessage.getUsername());
      }
      break;
    case SERVER_STOPPED:
      if (serverMessageListener != null) {
        serverMessageListener.ServerStopped();
      }
      break;
    default: // do nothing
    }
  }

  private void handleLoginResponse(LoginResponseMessage message) {
    if (authenticationListener != null) {
      if (message.isSuccessful()) {
        isAuthorized = true;
        loggedInUser = message.getUserData();
        authenticationListener.loginSuccesfull(this.loggedInUser);
      }
      else {
        authenticationListener.loginFailed(message.getMessage());
      }
    }
  }

  private void handleRegisterResponse(RegisterResponseMessage message) {
    if (registerListener != null) {
      if (message.isSuccessful()) {
        registerListener.registerSuccesfull(message.getMessage());
      }
      else {
        registerListener.registerFailed(message.getMessage());
      }
    }
  }

}
