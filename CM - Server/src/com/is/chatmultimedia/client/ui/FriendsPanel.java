package com.is.chatmultimedia.client.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
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
  private JMenuItem addFriendItem = new JMenuItem("Add friend");
  private JMenuItem logOutMenuItem = new JMenuItem("Log out");
  private JMenuItem exitItem = new JMenuItem("Exit");
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

    menu.add(addFriendItem);
    menu.addSeparator();
    menu.add(logOutMenuItem);
    menu.addSeparator();
    menu.add(exitItem);
    menuBar.add(menu);

    layoutConstraints.gridx = 0;
    layoutConstraints.gridy = 0;
    layoutConstraints.weightx = 1;
    layoutConstraints.gridwidth = 4;
    layoutConstraints.fill = GridBagConstraints.HORIZONTAL;
    layoutConstraints.anchor = GridBagConstraints.NORTHWEST;
    this.add(menuBar, layoutConstraints);

    layoutConstraints.gridx = 0;
    layoutConstraints.gridy = 1;
    onlineFriendsLabel.setText(ONLINE_FRIENDS);
    this.add(onlineFriendsLabel, layoutConstraints);

    setListOfFriends(friends);
    redisplayListOfFriends();

    friendsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    friendsList.setLayoutOrientation(JList.VERTICAL);
    friendsList.setFont(new Font("Arial", Font.BOLD, 16));

    layoutConstraints.weightx = 1;
    layoutConstraints.weighty = 1;
    layoutConstraints.fill = GridBagConstraints.BOTH;
    layoutConstraints.gridx = 0;
    layoutConstraints.gridy = 2;
    friendsScrollPane = new JScrollPane(friendsList);
    this.add(friendsScrollPane, layoutConstraints);

    layoutConstraints.weightx = 0;
    layoutConstraints.weighty = 0;
    layoutConstraints.gridwidth = 1;
    layoutConstraints.gridx = 0;
    layoutConstraints.gridy = 3;
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
    layoutConstraints.gridy = 3;
    image = new ImageIcon(SEARCH_ICON);
    searchLogo.setIcon(image);
    searchLogo.setPreferredSize(new Dimension(25, 19));
    this.add(searchLogo, layoutConstraints);

    searchTextField.addKeyListener(new SearchFriendKeyListener());
    layoutConstraints.gridwidth = 2;
    layoutConstraints.gridx = 2;
    layoutConstraints.gridy = 3;
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
      return friendsListModel.getFriendAt(friendsList.getSelectedIndex(),showOnlineFriends);
    }
    return null;
  }

  public void addAddFriendButtonActionListener(ActionListener actionListener) {
    addFriendItem.addActionListener(actionListener);
  }

  public void addLogoutButtonActionListener(ActionListener actionListener) {
    logOutMenuItem.addActionListener(actionListener);
  }

  public void addExitButtonActionListener(ActionListener actionListener) {
    exitItem.addActionListener(actionListener);
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

  @Override
  public void newFriendAdded(Friend friend) {
    friendsListModel.addElement(friend);
    redisplayListOfFriends();
  }

  private void redisplayListOfFriends() {
    friendsList.setModel(getFriendsToDisplay());
  }

  private DefaultListModel<String> getFriendsToDisplay() {
	  String searchText = searchTextField.getText().trim();
    if (searchText.isEmpty()) {
      if (showOnlineFriends) {
        onlineFriendsLabel.setText(ONLINE_FRIENDS);
        return friendsListModel.getOnlineFriendsNames();
      }
      else {
        onlineFriendsLabel.setText(ALL_FRIENDS);
        return friendsListModel.getAllFriendsNames();
      }
    }
    else{
    	if(showOnlineFriends){
    		return friendsListModel.selectOnlineFriendsFromList(searchText);
    	}
    	else{
    		return friendsListModel.selectAllFriendsFromList(searchText);
    	}
    }
  }

  private class SearchFriendKeyListener extends KeyAdapter{
	  
	  @Override
	  public void keyReleased(KeyEvent e) {
	    redisplayListOfFriends();
	  }
  }
}
