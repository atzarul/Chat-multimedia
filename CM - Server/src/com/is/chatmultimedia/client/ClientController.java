package com.is.chatmultimedia.client;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import com.is.chatmultimedia.client.ui.AddFriendPanel;
import com.is.chatmultimedia.client.ui.FriendsPanel;
import com.is.chatmultimedia.client.ui.LoginPanel;
import com.is.chatmultimedia.models.Friend;
import com.is.chatmultimedia.models.FriendRequest;
import com.is.chatmultimedia.models.User;

public class ClientController implements AuthenticationListener, FriendRequestListener {

  private Client client;

  private JFrame mainWindow;
  private LoginPanel loginPanel;
  private FriendsPanel friendsPanel;
  private ConversationController conversationController;

  private static final String WINDOW_TITLE = "MultiChat";
  private static final String SERVER_CONNECTION_ERROR = "Cannot establish connection to server!";
  private static final String FIELDS_EMPTY = "Some fields are empty!         ";
  private static final String ALREADY_FRIENDS = "Already friends!           ";
  private static final String NEW_FRIEND_REQUST = " wants to be friends with you. Accept?";
  private static final String CANT_ADD_YOURSELF = "Can't add yourself!               ";
  
  private static final String APP_ICON_16 = "resources//chat logo 16x16.png";
  private static final String APP_ICON_64 = "resources//chat logo 64x64.png";
  private static final String APP_ICON_128 = "resources//chat logo 128x128.png";

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
    client.registerFriendRequestListener(this);
    conversationController = new ConversationController(client);

    loginPanel = new LoginPanel();
    loginPanel.addLoginButtonActionListener(new LoginButtonAction());
    mainWindow = new JFrame();
    mainWindow.addWindowListener(new ClientClosing());
    ArrayList<Image> icons = new ArrayList<>();
    icons.add(new ImageIcon(APP_ICON_16).getImage());
    icons.add(new ImageIcon(APP_ICON_64).getImage());
    icons.add(new ImageIcon(APP_ICON_128).getImage());
    mainWindow.setIconImages(icons);
    mainWindow.setContentPane(loginPanel);
    mainWindow.setSize(new Dimension(275, 450));
    mainWindow.setLocation(1050, 170);
    mainWindow.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    mainWindow.setTitle(WINDOW_TITLE);
    mainWindow.setVisible(true);
  }

  @Override
  public void loginSuccesfull(User user) {
    friendsPanel = new FriendsPanel(user.getFriends());
    friendsPanel.addLogoutButtonActionListener(new LogoutButtonAction());
    friendsPanel.addFriendsListMouseListener(new ClickOnFriendListItem());
    friendsPanel.addExitButtonActionListener(new ExitButton());
    friendsPanel.addAddFriendButtonActionListener(new AddFriendButton());
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

  @Override
  public void newFriendRequest(FriendRequest friendRequest) {
    int option = JOptionPane.showConfirmDialog(mainWindow, new JLabel(friendRequest.getFromName() + NEW_FRIEND_REQUST,
        JLabel.CENTER), "New Friend Request", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
    if (option == JOptionPane.YES_OPTION) {
      client.respondToFriendRequest(true, friendRequest);
    }
    if (option == JOptionPane.NO_OPTION) {
      client.respondToFriendRequest(false, friendRequest);
    }
  }

  @Override
  public void requestSentSuccessful(String message) {
    JOptionPane.showMessageDialog(mainWindow, new JLabel(message, JLabel.CENTER), "Request send successful",
        JOptionPane.PLAIN_MESSAGE);
  }

  @Override
  public void requestSentFailed(String message) {
    showErrorMessage(message);
  }

  private void showErrorMessage(String message) {
    JOptionPane.showMessageDialog(mainWindow, new JLabel(message, JLabel.CENTER), "Error", JOptionPane.ERROR_MESSAGE);
  }

  private int showConfirmWarningMessage(String title, String message) {
    return JOptionPane.showConfirmDialog(mainWindow, new JLabel(message, JLabel.CENTER), title,
        JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
  }

  private class LoginButtonAction implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
      String username = loginPanel.getUsername();
      char[] password = loginPanel.getPassword();
      if (username.trim().isEmpty() || password.length == 0) {
        showErrorMessage(FIELDS_EMPTY);
        return;
      }
      client.login(username, password);
    }
  }

  private class LogoutButtonAction implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
      int option = showConfirmWarningMessage("Logout", "Are you sure you want to logout?");
      if (option == JOptionPane.YES_OPTION) {
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
      int option = client.isUserAuthorized() ? showConfirmWarningMessage("Exit application?",
          "Are you sure you want to exit? You will be logged out!") : JOptionPane.YES_OPTION;
      if (option == JOptionPane.YES_OPTION) {
        try {
          client.stop();
        }
        catch (IOException exception) {
          // client stop failed
        }
        finally {
          conversationController.closeAllConversations();
          mainWindow.dispose();
          System.exit(0);
        }
      }
    }
  }

  private class ExitButton implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
      mainWindow.dispatchEvent(new WindowEvent(mainWindow, WindowEvent.WINDOW_CLOSING));
    }
  }

  private class AddFriendButton implements ActionListener {

    private AddFriendPanel panel;

    @Override
    public void actionPerformed(ActionEvent arg0) {
      if (panel == null) {
        panel = new AddFriendPanel();
      }
      panel.setTextField("");
      while (true) {
        int option = JOptionPane.showConfirmDialog(mainWindow, panel, "Add friend", JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.PLAIN_MESSAGE);
        if (option == JOptionPane.CANCEL_OPTION) { // action canceled
          break;
        }
        String newFriend = panel.getText();
        if (newFriend.trim().isEmpty()) {
          showErrorMessage(FIELDS_EMPTY);
          continue;
        }
        if(newFriend.compareTo(client.getLoggedInUser().getUsername()) == 0) {
          showErrorMessage(CANT_ADD_YOURSELF);
          continue;
        }
        if (client.getLoggedInUser().getFriendByUsername(newFriend) != null) {
          showErrorMessage(ALREADY_FRIENDS);
          continue;
        }
        client.addFriend(newFriend);
        break;
      }
    }
  }

  public static void main(String[] args) {
    Client client = new Client();
    ClientController clientController = new ClientController(client);
  }

}
