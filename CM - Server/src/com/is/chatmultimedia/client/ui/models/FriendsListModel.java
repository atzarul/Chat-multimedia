package com.is.chatmultimedia.client.ui.models;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

import javax.swing.AbstractListModel;
import javax.swing.DefaultListModel;

import com.is.chatmultimedia.models.Friend;

public class FriendsListModel extends AbstractListModel<String> {

  private ArrayList<Friend> friendsList;
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

  public Friend getFriendAt(int index, boolean fromOnline) {
    if (index < friendsList.size()) {
    	if(!fromOnline)
    		return friendsList.get(index);
    	else{
    		int iterator = -1;
    		for(Friend friend: friendsList){
    			if(friend.isOnline())
    				iterator ++;
    			if(iterator == index)
    				return friend;
    		}
    	}
    }
    return null;
  }

  public void addElement(Friend friend) {
	  friendsList.add(friend);
	  Collections.sort(this.friendsList,new FriendsComparator());
  }

  public void setFriendsList(Collection<Friend> friendsList) {
    this.friendsList = new ArrayList<Friend>(friendsList);
    Collections.sort(this.friendsList, new FriendsComparator());
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
	String searchText = partOfName.toLowerCase();
	DefaultListModel<String> selectedFriendsList = new DefaultListModel<String>();
    for (Friend friend : friendsList) {
      if (friend.getName().toLowerCase().contains(searchText) && friend.isOnline())
        selectedFriendsList.addElement(BULLET + friend.getName());
    }
    return selectedFriendsList;
  }

  public DefaultListModel<String> selectAllFriendsFromList(String partOfName) {
	String searchText = partOfName.toLowerCase();
	DefaultListModel<String> selectedFriendsList = new DefaultListModel<String>();
    for (Friend friend : friendsList) {
      if (friend.getName().toLowerCase().contains(searchText)){
    	  if (friend.isOnline()) {
    		  selectedFriendsList.addElement("<html><font color=green>&nbsp;&nbsp;&nbsp;" + BULLET + "&nbsp;" + friend.getName()
    	            + "</font></html>");
    	      }
    	      else {
    	    	  selectedFriendsList.addElement("<html><font color=gray>&nbsp;&nbsp;&nbsp;" + BULLET + "&nbsp;" + friend.getName()
    	            + "</font></html>");
    	      }
      }
    }
    return selectedFriendsList;
  }
  
  private class FriendsComparator implements Comparator<Friend>{

	@Override
	public int compare(Friend o1, Friend o2) {
		return o1.getName().compareTo(o2.getName());
	}
	  
  }

}
