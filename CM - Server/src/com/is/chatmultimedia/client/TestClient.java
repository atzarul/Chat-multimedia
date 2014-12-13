package com.is.chatmultimedia.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class TestClient implements Client.MessageListener {

  public static void main(String[] args) throws IOException {
    String line;
    String[] components;
    String username = args[0];

    TestClient test = new TestClient();
    Client client = new Client();
    client.registerMessageListener(test);

    System.out.println("Before client.run");
    client.run();
    client.login(username, "ana");
    System.out.println("Before while");
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    while (true) {
      System.out.print(">");
      line = br.readLine();
      components = line.split(" ");
      if (components[0].compareTo("stop") == 0) {
        client.stop();
        System.exit(0);
      }
      else {
        client.sendMessageTo(username, components[0], components[1]);
      }
    }
  }

  @Override
  public void displayMessage(String from, String message) {
    System.out.println(from + ">" + message);
  }

}
