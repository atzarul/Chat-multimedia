package com.is.chatmultimedia.client;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.is.chatmultimedia.client.ui.FriendsPanel;
import com.is.chatmultimedia.client.ui.LoginPanel;
import com.is.chatmultimedia.models.Friend;
import com.is.chatmultimedia.models.User;

public class ClientController implements AuthenticationListener {

  private Client client;

  private JFrame mainWindow;
  private LoginPanel loginPanel;
  private FriendsPanel friendsPanel;
  private ConversationController conversationController;

  private static final String WINDOW_TITLE = "MultiChat";
  private static final String SERVER_CONNECTION_ERROR = "Cannot establish connection to server!";
  private static final String FIELDS_EMPTY = "Some fields are empty!";

  public ClientController(Client client) {
    this.client = client;

    try {
      client.start();
    }
    catch (IOException e) {
      e.printStackTrace();
      showErrorMessage(SERVER_CONNECTION_ERROR);
      System.exit(1);
    }

    client.registerAuthenticationListener(this);
    conversationController = new ConversationController(client);

    loginPanel = new LoginPanel();
    loginPanel.addLoginButtonActionListener(new LoginButtonAction());
    mainWindow = new JFrame();
    mainWindow.addWindowListener(new ClientClosing());
    mainWindow.setContentPane(loginPanel);
    mainWindow.setSize(new Dimension(260, 450));
    mainWindow.setLocation(1050, 170);
    mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    mainWindow.setTitle(WINDOW_TITLE);
    mainWindow.setVisible(true);
  }

  @Override
  public void loginSuccesfull(User user) {
    friendsPanel = new FriendsPanel(user.getFriends());
    friendsPanel.addLogoutButtonActionListener(new LogoutButtonAction());
    friendsPanel.addFriendsListMouseListener(new ClickOnFriendListItem());
    client.registerFriendsManagerListener(friendsPanel);
    mainWindow.setContentPane(friendsPanel);
    mainWindow.setTitle(WINDOW_TITLE + " : " + client.getLoggedInUser().getName());
    mainWindow.revalidate();
    mainWindow.repaint();
  }

  @Override
  public void loginFailed(String message) {
    showErrorMessage(message);
  }

  private void showErrorMessage(String message) {
    JOptionPane.showMessageDialog(mainWindow, message, "Error", JOptionPane.ERROR_MESSAGE);
  }

  private class LoginButtonAction implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
      String username = loginPanel.getUsername();
      char[] password = loginPanel.getPassword();
      if (username.isEmpty() || password.length == 0) {
        showErrorMessage(FIELDS_EMPTY);
        return;
      }
      client.login(username, password);
    }
  }

  private class LogoutButtonAction implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
      client.logout();
      conversationController.closeAllConversations();
      friendsPanel = null;
      loginPanel.setUsername("");
      loginPanel.setPassword("");
      mainWindow.setContentPane(loginPanel);
      mainWindow.setTitle(WINDOW_TITLE);
      mainWindow.revalidate();
      mainWindow.repaint();
    }
  }

  private class ClickOnFriendListItem extends MouseAdapter {
    public void mouseClicked(MouseEvent e) {
      if (e.getClickCount() == 2) {
        Friend friend = friendsPanel.getSelectedFriend();
        // START CONVERSATION - ONLY IF FRIEND IS ONLINE
        if (friend.isOnline()) {
          conversationController.openConversationWindowFor(friend.getUsername(), friend.getName());
        }
      }
    }
  }

  private class ClientClosing extends WindowAdapter {
    @Override
    public void windowClosing(WindowEvent e) {
      try {
        client.stop();
      }
      catch (IOException exception) {
        // client stop failed
      }
      conversationController.closeAllConversations();
    }
  }

  public static void main(String[] args) {
    Client client = new Client();
    ClientController clientController = new ClientController(client);
  }

}
