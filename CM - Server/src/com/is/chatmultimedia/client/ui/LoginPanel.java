package com.is.chatmultimedia.client.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseListener;
import java.awt.font.TextAttribute;
import java.util.Map;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class LoginPanel extends JPanel {

  private GridBagLayout logInLayout = new GridBagLayout();
  private GridBagConstraints layoutConstraints = new GridBagConstraints();

  private JLabel logo = new JLabel();
  private JLabel usernameLabel = new JLabel("Username");
  private JTextField usernameTextField = new JTextField(10);
  private JLabel passwordLabel = new JLabel("Password");
  private JPasswordField passwordTextFields = new JPasswordField(10);
  private JButton loginButton = new JButton("Login");
  
  private JLabel registerLabel = new JLabel("Register");
  
  private static final String LOGO = "resources//chat logo 128x128.png";

  private static final long serialVersionUID = 1;

  public LoginPanel() {
    this.setLayout(logInLayout);

    layoutConstraints.insets = new Insets(5, 5, 5, 5);
    
    logo.setIcon(new ImageIcon(LOGO));
    layoutConstraints.gridwidth = 2;
    layoutConstraints.gridx = 0;
    layoutConstraints.gridy = 0;
    this.add(logo, layoutConstraints);
    
    layoutConstraints.gridwidth = 2;
    layoutConstraints.gridx = 0;
    layoutConstraints.gridy = 1;
    this.add(Box.createRigidArea(new Dimension(0, 7)), layoutConstraints);
    
    layoutConstraints.gridwidth = 1;
    layoutConstraints.gridx = 0;
    layoutConstraints.gridy = 2;
    this.add(usernameLabel, layoutConstraints);

    layoutConstraints.gridx = 1;
    layoutConstraints.gridy = 2;
    usernameTextField.addKeyListener(new EnterKeyListener());
    this.add(usernameTextField, layoutConstraints);

    layoutConstraints.gridx = 0;
    layoutConstraints.gridy = 3;
    this.add(passwordLabel, layoutConstraints);

    layoutConstraints.gridx = 1;
    layoutConstraints.gridy = 3;
    passwordTextFields.addKeyListener(new EnterKeyListener());
    this.add(passwordTextFields, layoutConstraints);

    layoutConstraints.gridwidth = 2;
    layoutConstraints.gridx = 0;
    layoutConstraints.gridy = 4;
    this.add(loginButton, layoutConstraints);
    
    layoutConstraints.gridwidth = 2;
    layoutConstraints.gridx = 0;
    layoutConstraints.gridy = 5;
    this.add(Box.createRigidArea(new Dimension(0, 80)), layoutConstraints);
    
    layoutConstraints.gridx = 0;
    layoutConstraints.gridy = 6;
    registerLabel.setForeground(new Color(37, 74, 158));
    Font font = registerLabel.getFont();
    Map attributes = font.getAttributes(); 
    attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
    registerLabel.setFont(font.deriveFont(attributes));
    this.add(registerLabel,layoutConstraints);
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
  
  public void addRegisterLabelMouseListener(MouseListener l){
	  registerLabel.addMouseListener(l);
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
