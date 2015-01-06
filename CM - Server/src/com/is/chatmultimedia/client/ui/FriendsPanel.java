package com.is.chatmultimedia.client.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Collection;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import com.is.chatmultimedia.models.Friend;
import com.is.chatmultimedia.client.FriendsManagerListener;
import com.is.chatmultimedia.client.ui.models.FriendsListModel;

public class FriendsPanel extends JPanel implements FriendsManagerListener {

  private boolean showOnlineFriends = true;

  private GridBagLayout friendsLayout = new GridBagLayout();
  private GridBagConstraints layoutConstraints = new GridBagConstraints();

  private JMenuBar menuBar = new JMenuBar();
  private JMenu menu = new JMenu("Menu");
  private JMenuItem logOutMenuItem = new JMenuItem("Log out");
  private JLabel onlineFriendsLabel = new JLabel();
  private JList<String> friendsList = new JList<String>();
  private FriendsListModel friendsListModel;
  private JScrollPane friendsScrollPane;
  private JTextField searchTextField = new JTextField();
  private JLabel changeOnlineFriendsViewButton = new JLabel();
  private JLabel searchLogo = new JLabel();

  private static final String ONLINE_FRIENDS = " Online friends:";
  private static final String ALL_FRIENDS = " All friends:";
  private static final String FRIENDS_VISIBILITY_BUTTON_ICON = "resources//FriendsVisibilityButtonLogo.png";
  private static final String SEARCH_ICON = "resources//SearchLogo.png";
  private static final long serialVersionUID = 1;

  public FriendsPanel(Collection<Friend> friends) {
    this.setLayout(friendsLayout);

    menu.add(logOutMenuItem);
    menuBar.add(menu);

    layoutConstraints.gridx = 0;
    layoutConstraints.gridy = 0;
    layoutConstraints.gridwidth = 4;
    layoutConstraints.fill = GridBagConstraints.HORIZONTAL;
    layoutConstraints.anchor = GridBagConstraints.NORTHWEST;
    this.add(menuBar, layoutConstraints);

    layoutConstraints.gridx = 0;
    layoutConstraints.gridy = 1;
    onlineFriendsLabel.setText(ONLINE_FRIENDS);
    this.add(onlineFriendsLabel, layoutConstraints);

    layoutConstraints.gridx = 0;
    layoutConstraints.gridy = 2;
    this.add(friendsList, layoutConstraints);

    layoutConstraints.weightx = 1;
    layoutConstraints.weighty = 1;
    layoutConstraints.fill = GridBagConstraints.BOTH;
    friendsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    friendsList.setLayoutOrientation(JList.VERTICAL);

    setListOfFriends(friends);
    redisplayListOfFriends();

    layoutConstraints.gridx = 0;
    layoutConstraints.gridy = 3;
    friendsList.setFont(new Font("Arial", Font.BOLD, 16));
    friendsScrollPane = new JScrollPane(friendsList);
    this.add(friendsScrollPane, layoutConstraints);

    layoutConstraints.weightx = 0;
    layoutConstraints.weighty = 0;
    layoutConstraints.gridwidth = 1;
    layoutConstraints.gridx = 0;
    layoutConstraints.gridy = 4;
    layoutConstraints.fill = GridBagConstraints.NONE;
    ImageIcon image = new ImageIcon(FRIENDS_VISIBILITY_BUTTON_ICON);
    changeOnlineFriendsViewButton.setPreferredSize(new Dimension(21, 19));
    changeOnlineFriendsViewButton.setIcon(image);
    changeOnlineFriendsViewButton.setBorder(BorderFactory.createLineBorder(Color.GRAY));
    changeOnlineFriendsViewButton.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        showOnlineFriends = !showOnlineFriends;
        redisplayListOfFriends();
      }
    });
    this.add(changeOnlineFriendsViewButton, layoutConstraints);

    layoutConstraints.gridx = 1;
    layoutConstraints.gridy = 4;
    image = new ImageIcon(SEARCH_ICON);
    searchLogo.setIcon(image);
    searchLogo.setPreferredSize(new Dimension(25, 19));
    this.add(searchLogo, layoutConstraints);

    layoutConstraints.gridwidth = 2;
    layoutConstraints.gridx = 2;
    layoutConstraints.gridy = 4;
    layoutConstraints.fill = GridBagConstraints.HORIZONTAL;
    this.add(searchTextField, layoutConstraints);

    this.setVisible(true);
  }

  public void setListOfFriends(Collection<Friend> listOfFriends) {
    if (friendsListModel == null) {
      friendsListModel = new FriendsListModel(listOfFriends);
    }
    else {
      friendsListModel.setFriendsList(listOfFriends);
    }
  }

  public Friend getSelectedFriend() {
    if (friendsListModel != null) {
      return friendsListModel.getFriendAt(friendsList.getSelectedIndex());
    }
    return null;
  }

  public void addLogoutButtonActionListener(ActionListener actionListener) {
    logOutMenuItem.addActionListener(actionListener);
  }

  public void addFriendsListMouseListener(MouseListener mouseListener) {
    friendsList.addMouseListener(mouseListener);
  }

  @Override
  public void userCameOnline(String username) {
    redisplayListOfFriends();
  }

  @Override
  public void userWentOffline(String username) {
    redisplayListOfFriends();
  }

  private void redisplayListOfFriends() {
    friendsList.setModel(getFriendsToDisplay());
  }

  private DefaultListModel<String> getFriendsToDisplay() {
    if (searchTextField.getText().isEmpty()) {
      if (showOnlineFriends) {
        return friendsListModel.getOnlineFriendsNames();
      }
      else {
        return friendsListModel.getAllFriendsNames();
      }
    }
    return null;
  }

}
