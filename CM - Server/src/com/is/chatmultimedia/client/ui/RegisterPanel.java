package com.is.chatmultimedia.client.ui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class RegisterPanel extends JPanel{
	
	private GridBagLayout registerLayout = new GridBagLayout();
	private GridBagConstraints layoutConstraints = new GridBagConstraints();
	
	private JLabel registerLabel = new JLabel("Register");
	private JLabel nameLabel = new JLabel("Name");
	private JLabel usernameLabel = new JLabel("Username");
	private JLabel passwordLabel = new JLabel("Password");
	private JLabel passwordConfirmationLabel = new JLabel("Confirm password");
	
	private JTextField nameTextField = new JTextField(10);
	private JTextField usernameTextField = new JTextField(10);
	private JPasswordField passwordField = new JPasswordField(10);
	private JPasswordField passwordConfirmationField = new JPasswordField(10);
	
	private JButton registerButton = new JButton("Register");
	private JButton cancelButton = new JButton("Cancel");
	
	public RegisterPanel(){
		this.setLayout(registerLayout);
		
		layoutConstraints.insets = new Insets(5, 5, 5, 5);
		
		layoutConstraints.gridwidth = 4;
		
		layoutConstraints.gridx = 0;
	    layoutConstraints.gridy = 0;
	    String fontName = registerLabel.getFont().getFontName();
	    registerLabel.setFont(new Font(fontName, Font.BOLD, 20));
	    this.add(registerLabel,layoutConstraints);
	    
	    layoutConstraints.gridx = 0;
	    layoutConstraints.gridy = 1;
	    this.add(Box.createRigidArea(new Dimension(0, 20)), layoutConstraints);
	    
	    layoutConstraints.gridwidth = 1;
		
		layoutConstraints.gridx = 0;
	    layoutConstraints.gridy = 2;
	    this.add(nameLabel,layoutConstraints);
	    
	    layoutConstraints.gridx = 0;
	    layoutConstraints.gridy = 3;
	    this.add(usernameLabel,layoutConstraints);
	    
	    layoutConstraints.gridx = 0;
	    layoutConstraints.gridy = 4;
	    this.add(passwordLabel,layoutConstraints);
	    
	    layoutConstraints.gridx = 0;
	    layoutConstraints.gridy = 5;
	    this.add(passwordConfirmationLabel,layoutConstraints);
	    
	    layoutConstraints.gridwidth = 3;
	    
	    layoutConstraints.gridx = 1;
	    layoutConstraints.gridy = 2;
	    this.add(nameTextField,layoutConstraints);
	    
	    layoutConstraints.gridx = 1;
	    layoutConstraints.gridy = 3;
	    this.add(usernameTextField,layoutConstraints);
	    
	    layoutConstraints.gridx = 1;
	    layoutConstraints.gridy = 4;
	    this.add(passwordField,layoutConstraints);
	    
	    layoutConstraints.gridx = 1;
	    layoutConstraints.gridy = 5;
	    this.add(passwordConfirmationField,layoutConstraints);
	    
	    layoutConstraints.gridwidth = 4;
		
	    layoutConstraints.gridx = 0;
	    layoutConstraints.gridy = 6;
	    this.add(Box.createRigidArea(new Dimension(0, 20)), layoutConstraints);
	    
	    layoutConstraints.gridwidth = 1;
	    
		layoutConstraints.gridx = 0;
	    layoutConstraints.gridy = 7;
	    this.add(registerButton,layoutConstraints); 
	    
	    layoutConstraints.gridx = 3;
	    layoutConstraints.gridy = 7;
	    this.add(cancelButton,layoutConstraints);
	}
	
	public void addRegisterButtonListener(MouseListener l){
		registerButton.addMouseListener(l);
	}
	
	public void addCancelButtonListener(MouseListener l){
		cancelButton.addMouseListener(l);
	}
	
	public void clearTextFields(){
		nameTextField.setText("");
		usernameTextField.setText("");
		passwordField.setText("");
		passwordConfirmationField.setText("");
	}
	
	public String getName(){
		return nameTextField.getText();
	}
	
	public String getUsername(){
		return usernameTextField.getText();
	}
	
	public char[] getPassword(){
		return passwordField.getPassword();
	}
	
	public char[] getPasswordConfirmation(){
		return passwordConfirmationField.getPassword();
	}
	
	public String getStringPassword(){
		return passwordField.getText();
	}
}
