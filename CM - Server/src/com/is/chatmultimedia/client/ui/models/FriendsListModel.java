package com.is.chatmultimedia.client.ui.models;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.AbstractListModel;
import javax.swing.DefaultListModel;

import com.is.chatmultimedia.models.Friend;

public class FriendsListModel extends AbstractListModel<String> {

  private LinkedList<Friend> friendsList;
  private static String BULLET = "   \u2022  ";
  private static final long serialVersionUID = 1;

  public FriendsListModel(Collection<Friend> friendsList) {
    setFriendsList(friendsList);
  }

  @Override
  public String getElementAt(int index) {
    if (index < friendsList.size()) {
      return friendsList.get(index).getName();
    }
    return null;
  }

  @Override
  public int getSize() {
    return friendsList.size();
  }

  public Friend getFriendAt(int index) {
    if (index < friendsList.size()) {
      return friendsList.get(index);
    }
    return null;
  }

  public void addElement(Friend friend) {
    Friend temp;
    Iterator<Friend> it = friendsList.iterator();
    int insertPosition = 0;
    while (it.hasNext()) {
      temp = it.next();
      if (temp.getName().compareTo(friend.getName()) > 0) {
        break;
      }
      ++insertPosition;
    }
    friendsList.add(insertPosition, friend);
  }

  public void setFriendsList(Collection<Friend> friendsList) {
    this.friendsList = new LinkedList<Friend>(friendsList);
    Collections.sort(this.friendsList);
  }

  public DefaultListModel<String> getAllFriendsNames() {
    DefaultListModel<String> friendsNames = new DefaultListModel<String>();
    for (Friend friend : friendsList) {
      if (friend.isOnline()) {
        friendsNames.addElement("<html><font color=green>&nbsp;&nbsp;&nbsp;" + BULLET + "&nbsp;" + friend.getName()
            + "</font></html>");
      }
      else {
        friendsNames.addElement("<html><font color=gray>&nbsp;&nbsp;&nbsp;" + BULLET + "&nbsp;" + friend.getName()
            + "</font></html>");
      }
    }
    return friendsNames;
  }

  public DefaultListModel<String> getOnlineFriendsNames() {
    DefaultListModel<String> friendsNames = new DefaultListModel<String>();
    for (Friend friend : friendsList) {
      if (friend.isOnline()) {
        friendsNames.addElement(BULLET + friend.getName());
      }
    }
    return friendsNames;
  }

  public DefaultListModel<String> selectOnlineFriendsFromList(String partOfName) {
    LinkedList<Friend> selectedFriendsList = new LinkedList<Friend>();
    for (Friend friend : friendsList) {
      if (friend.getName().contains(partOfName))
        selectedFriendsList.add(friend);
    }
    this.friendsList = selectedFriendsList;
    return null;
  }

  public DefaultListModel<String> selectAllFriendsFromList(String partOfName) {
    LinkedList<Friend> selectedFriendsList = new LinkedList<Friend>();
    for (Friend friend : friendsList) {
      if (friend.getName().contains(partOfName) && friend.isOnline())
        selectedFriendsList.add(friend);
    }
    this.friendsList = selectedFriendsList;
    return null;
  }

}
