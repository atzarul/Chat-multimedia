package com.is.chatmultimedia.client.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class LoginPanel extends JPanel {

  private GridBagLayout logInLayout = new GridBagLayout();
  private GridBagConstraints layoutConstraints = new GridBagConstraints();

  private JLabel usernameLabel = new JLabel("Username");
  private JTextField usernameTextField = new JTextField(10);
  private JLabel passwordLabel = new JLabel("Password");
  private JPasswordField passwordTextFields = new JPasswordField(10);
  private JButton loginButton = new JButton("Login");

  private static final long serialVersionUID = 1;

  public LoginPanel() {
    this.setLayout(logInLayout);

    layoutConstraints.insets = new Insets(5, 5, 5, 5);

    layoutConstraints.gridx = 0;
    layoutConstraints.gridy = 0;
    this.add(usernameLabel, layoutConstraints);

    layoutConstraints.gridx = 1;
    layoutConstraints.gridy = 0;
    usernameTextField.addKeyListener(new EnterKeyListener());
    this.add(usernameTextField, layoutConstraints);

    layoutConstraints.gridx = 0;
    layoutConstraints.gridy = 1;
    this.add(passwordLabel, layoutConstraints);

    layoutConstraints.gridx = 1;
    layoutConstraints.gridy = 1;
    passwordTextFields.addKeyListener(new EnterKeyListener());
    this.add(passwordTextFields, layoutConstraints);

    layoutConstraints.gridwidth = 2;
    layoutConstraints.gridx = 0;
    layoutConstraints.gridy = 2;
    this.add(loginButton, layoutConstraints);
  }

  public String getUsername() {
    return usernameTextField.getText();
  }

  public void setUsername(String text) {
    usernameTextField.setText(text);
  }

  public char[] getPassword() {
    return passwordTextFields.getPassword();
  }

  public void setPassword(String text) {
    passwordTextFields.setText(text);
  }

  public void addLoginButtonActionListener(ActionListener actionListener) {
    loginButton.addActionListener(actionListener);
  }

  private class EnterKeyListener extends KeyAdapter {
    @Override
    public void keyPressed(KeyEvent e) {
      if (e.getKeyCode() == KeyEvent.VK_ENTER) {
        loginButton.doClick();
      }
    }
  }

}
