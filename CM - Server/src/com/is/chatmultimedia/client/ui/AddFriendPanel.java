package com.is.chatmultimedia.client.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class AddFriendPanel extends JPanel {

  private JLabel text;
  private JTextField inputText;
  private static final String TEXT = "Friend's username: ";
  private static final long serialVersionUID = 1;

  public AddFriendPanel() {
    this.setLayout(new GridBagLayout());
    GridBagConstraints constraints = new GridBagConstraints();

    text = new JLabel(TEXT);
    constraints.gridx = 0;
    constraints.gridy = 0;
    constraints.gridwidth = 1;
    this.add(text, constraints);

    inputText = new JTextField(10);
    constraints.gridx = 1;
    this.add(inputText, constraints);
  }

  public void setTextField(String text) {
    this.inputText.setText(text);
  }

  public String getText() {
    return this.inputText.getText();
  }

}
