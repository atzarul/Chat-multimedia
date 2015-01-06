package com.is.chatmultimedia.client;

import java.util.TreeMap;

import com.is.chatmultimedia.client.ui.ConversationPanel;
import com.is.chatmultimedia.client.ui.ConversationWindow;
import com.is.chatmultimedia.client.ui.ConversationWindow.ConversationsListener;
import com.is.chatmultimedia.models.Friend;

public class ConversationController implements MessageListener, ConversationPanel.MessageListener,
    ConversationsListener {

  private Client client;
  private ConversationWindow conversationWindow;
  private TreeMap<String, ConversationPanel> conversations;

  public ConversationController(Client client) {
    this.client = client;
    client.registerMessageListener(this);
    conversations = new TreeMap<String, ConversationPanel>();
  }

  public void openConversationWindowFor(String username, String name) {
    if (conversations.get(username) == null) {
      ConversationPanel panel = new ConversationPanel(username);
      panel.registerMessageListener(this);
      if (conversationWindow == null) {
        conversationWindow = new ConversationWindow(name, panel);
        conversationWindow.registerConversationListener(this);
      }
      else {
        conversationWindow.addConversationPanel(name, panel);
      }
      conversations.put(username, panel);
    }
  }

  public void closeAllConversations() {
    if (conversationWindow != null) {
      conversationWindow.dispose();
    }
    conversationWindowClosed();
  }

  @Override
  public void conversationClosed(String identifier) {
    conversations.remove(identifier);
  }

  @Override
  public void conversationWindowClosed() {
    conversationWindow = null;
    conversations.clear();
  }

  @Override
  public void messageFromUi(String identifier, String message) {
    client.sendMessageTo(identifier, message);
  }

  @Override
  public void displayMessage(String from, String message) {
    ConversationPanel panel = conversations.get(from);
    Friend friend = client.getLoggedInUser().getFriendByUsername(from);
    if (panel == null) {
      openConversationWindowFor(from, friend.getName());
      panel = conversations.get(from);
    }
    panel.writeMessage(friend.getName(), message);
  }

}
