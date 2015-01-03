package com.is.chatmultimedia.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.is.chatmultimedia.models.User;

public class TestClient implements MessageListener, AuthenticationListener, FriendsManagerListener,
    ServerMessageListener {

  public static void main(String[] args) throws IOException {
    TestClient test = new TestClient();
    Client client = new Client();
    client.registerMessageListener(test);
    client.registerAuthenticationListener(test);
    client.registerFriendsManagerListener(test);
    client.registerServerMessageListener(test);
    client.start();
    String line;
    String[] comp;
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    int arg = Integer.parseInt(args[0]);
    switch (arg) {
    case 0:
      client.login("atzaruri", "password");
      break;
    case 1:
      client.login("oana.todea", "password");
      break;
    case 2:
      client.login("butiri", "password");
      break;
    }

    while (true) {
      line = br.readLine();
      comp = line.split(" ");
      if (comp[0].compareTo("message") == 0) {
        client.sendMessageTo(comp[1], comp[2]);
        continue;
      }
      if (comp[0].compareTo("logout") == 0) {
        client.logout();
        continue;
      }
      if (comp[0].compareTo("stop") == 0) {
        client.stop();
        continue;
      }
    }

  }

  @Override
  public void displayMessage(String from, String message) {
    System.out.println(from + ">" + message);
  }

  @Override
  public void ServerStopped() {
    System.out.println("Sever stopped!");
    System.exit(0);
  }

  @Override
  public void userCameOnline(String username) {
    System.out.println("Server> User came online: " + username);
  }

  @Override
  public void userWentOffline(String username) {
    System.out.println("Server> User went offline: " + username);
  }

  @Override
  public void loginSuccesfull(User user) {
    System.out.println("Server> Login succesfull!" + user.getUsername());
  }

  @Override
  public void loginFailed(String message) {
    System.out.println("Server> " + message);
  }

  @Override
  public void logout(String message) {
    System.out.println(message);
  }

}
