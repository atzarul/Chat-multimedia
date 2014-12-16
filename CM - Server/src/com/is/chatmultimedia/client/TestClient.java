package com.is.chatmultimedia.client;

import java.io.IOException;

public class TestClient implements Client.MessageListener {

  public static void main(String[] args) throws IOException {
    TestClient test = new TestClient();
    Client client = new Client();
    client.registerMessageListener(test);
    client.run();

    client.login("atzaruri", "password");
    client.login("atzaruri", "password");
  }

  @Override
  public void displayMessage(String from, String message) {
    System.out.println(from + ">" + message);
  }

}
