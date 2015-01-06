package com.is.chatmultimedia.client.ui;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.StyledDocument;
import javax.swing.text.html.HTMLDocument;

public class ConversationPanel extends JPanel {

  private String identifier;
  private DateFormat dateFormat;

  private JTextPane conversationDisplayArea;
  private JTextArea inputArea;
  private JButton sendButton;
  private MessageListener messageListener;

  private static final String USER = "Me";
  private static final long serialVersionUID = 1;

  public ConversationPanel(String panelIdentifier) {
    this.identifier = panelIdentifier;

    GridBagLayout layout = new GridBagLayout();
    GridBagConstraints layoutConstraints = new GridBagConstraints();
    this.setLayout(layout);

    layoutConstraints.insets = new Insets(5, 5, 5, 5);

    conversationDisplayArea = new JTextPane();
    conversationDisplayArea.setContentType("text/html");
    conversationDisplayArea.setBorder(BorderFactory.createLineBorder(Color.GRAY));
    conversationDisplayArea.setEditable(false);
    layoutConstraints.gridwidth = 2;
    layoutConstraints.weightx = 1;
    layoutConstraints.weighty = 1;
    layoutConstraints.gridx = 0;
    layoutConstraints.gridy = 0;
    layoutConstraints.fill = GridBagConstraints.BOTH;
    JScrollPane conversationScroll = new JScrollPane(conversationDisplayArea);
    this.add(conversationScroll, layoutConstraints);

    inputArea = new JTextArea();
    inputArea.setRows(2);
    inputArea.setLineWrap(true);
    inputArea.setWrapStyleWord(true);
    inputArea.setBorder(BorderFactory.createLineBorder(Color.GRAY));
    inputArea.addKeyListener(new InputEnterKeyListener());
    layoutConstraints.gridwidth = 1;
    layoutConstraints.weightx = 1;
    layoutConstraints.weighty = 0;
    layoutConstraints.gridx = 0;
    layoutConstraints.gridy = 1;
    layoutConstraints.fill = GridBagConstraints.HORIZONTAL;
    JScrollPane inputScroll = new JScrollPane(inputArea);
    this.add(inputScroll, layoutConstraints);

    sendButton = new JButton("Send");
    sendButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent arg0) {
        String message = inputArea.getText();
        inputArea.setText("");
        if (!message.isEmpty()) {
          messageListener.messageFromUi(identifier, message);
          writeMessage(USER, message);
        }
      }
    });
    layoutConstraints.gridwidth = 1;
    layoutConstraints.weightx = 0;
    layoutConstraints.gridx = 1;
    layoutConstraints.gridy = 1;
    layoutConstraints.fill = GridBagConstraints.NONE;
    this.add(sendButton, layoutConstraints);

    this.setVisible(true);
    inputArea.requestFocusInWindow();

    dateFormat = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
  }

  public void writeMessage(String from, String message) {
    Date date = new Date();
    StringBuilder builder = new StringBuilder();

    builder.append("<font color=");
    if (from.compareTo(USER) == 0) {
      builder.append("blue>");
    }
    else {
      builder.append("red>");
    }
    builder.append("(");
    builder.append(dateFormat.format(date));
    builder.append(") <b>");
    builder.append(from);
    builder.append("</b></font>: ");
    builder.append(message);
    builder.append("<br>");

    // write on UI in EDT
    EventQueue.invokeLater(new Runnable() {
      @Override
      public void run() {
        StyledDocument styleDoc = conversationDisplayArea.getStyledDocument();
        HTMLDocument doc = (HTMLDocument) styleDoc;
        Element last = doc.getParagraphElement(doc.getLength());
        try {
          doc.insertBeforeEnd(last, builder.toString());
        }
        catch (BadLocationException | IOException e) {
          // message display failed
        }
      }
    });
  }

  public String getIdentifier() {
    return this.identifier;
  }

  public void registerMessageListener(MessageListener messageListener) {
    this.messageListener = messageListener;
  }

  public interface MessageListener {

    public void messageFromUi(String identifier, String message);

  }

  private class InputEnterKeyListener extends KeyAdapter {
    @Override
    public void keyPressed(KeyEvent e) {
      if (e.getKeyCode() == KeyEvent.VK_ENTER) {
        e.consume();
        sendButton.doClick();
      }
    }
  }

}
