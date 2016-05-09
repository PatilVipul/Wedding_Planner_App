/**	This class consists of design and logic about how the user will log into the Wedding Planner Application. User will be asked to enter his
 * or her user name and password. After validating those details the user will be allowed to log into the application.
 * @author Vipul Patil */
package com.proj;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class LoginClass{	
	public static int loginUserID;
	final JFrame loginWindow = new JFrame();
	final JPanel loginPanel = new JPanel();				
	JLabel nameLabel = new JLabel("Enter username: ");
	final JTextField nameTextField = new JTextField();
	JLabel passwordLabel = new JLabel("Enter password: ");
	final JPasswordField passwordTextField = new JPasswordField();	
	JButton okButton = new JButton("OK");
	
	/** Constructor which generates a separate frame for user log in. Elements like labels, textfields and buttons are added to this frame. */
	public LoginClass(){
		loginWindow.setTitle("Wedding Planner - Log In");		// frame for user log in.
		loginWindow.setVisible(true);
		loginWindow.setSize(300, 300);
		loginWindow.setResizable(false);
		loginWindow.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				loginWindow.setVisible(false);
				FrontPage.window.setVisible(true);
			}	
		});
	
		loginPanel.setLayout(null);								// panel on top of that frame.
		loginPanel.setPreferredSize(new Dimension(250, 250));
		loginPanel.setBackground(new Color(255,229,219));
		loginWindow.add(loginPanel);
		
		nameLabel.setFont(new Font("comic sans ms",Font.BOLD ,15));	// simple label asking user to enter his or her user name.
		nameLabel.setBounds(10, 10, 200, 25);
		loginPanel.add(nameLabel);
			
		nameTextField.setFont(new Font("comic sans ms",Font.BOLD ,15));	// element where the user will enter the user name.
		nameTextField.setBounds(10, 40, 250, 25);
		loginPanel.add(nameTextField);
		nameTextField.addKeyListener(new KeyListener() {
			public void keyTyped(KeyEvent e){}
			public void keyReleased(KeyEvent e){}
			public void keyPressed(KeyEvent e){
				if(e.getKeyCode() == KeyEvent.VK_ENTER){
					login();
				}
			}
		});
		
		passwordLabel.setFont(new Font("comic sans ms",Font.BOLD ,15));	// simple label asking user to enter his or her password.
		passwordLabel.setBounds(10, 80, 200, 25);
		loginPanel.add(passwordLabel);
		
		passwordTextField.setBounds(10, 110, 250, 25);				//space for user to enter his or her hidden password.
		loginPanel.add(passwordTextField);
		passwordTextField.addKeyListener(new KeyListener() {
			public void keyTyped(KeyEvent e){}
			public void keyReleased(KeyEvent e){}
			public void keyPressed(KeyEvent e){
				if(e.getKeyCode() == KeyEvent.VK_ENTER){
					login();
				}
			}
		});
		
		okButton.setBounds(20, 170, 100, 30);	// On click of this OK button user will be validated and logged in accordingly.
		loginPanel.add(okButton);
		okButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				login();
			}
		});
		
		/** This button should close the log in window and open the Home page of the application without logging in the user. */
		JButton cancelButton = new JButton("Cancel");
		cancelButton.setBounds(150, 170, 100, 30);
		loginPanel.add(cancelButton);
		cancelButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				loginWindow.setVisible(false);
				FrontPage.window.setVisible(true);
			}
		});
	}
	
	@SuppressWarnings("deprecation")
	public void login()
	{
		if(nameTextField.getText().length() == 0){	// Validation 1: user did not enter the user name.
			JOptionPane.showMessageDialog(loginPanel, "Enter the user name.", "Log In - User Name", JOptionPane.ERROR_MESSAGE);
			nameTextField.requestFocus();
		}
		else if(passwordTextField.getText().length() == 0){	// Validation 2: user did not enter the password
			JOptionPane.showMessageDialog(loginPanel, "Enter the password.", "Log In - Password", JOptionPane.ERROR_MESSAGE);
			passwordTextField.requestFocus();
		}
		else{
			// Validation 3: Checking whether user name and password are correct.
			try{
				Connection myConn = DriverManager.getConnection("jdbc:derby:test;create=true");	
				Statement myStmt = myConn.createStatement();
				
				/** Execute SQL Query -> Select from Table: To check whether same user name already exists in the table	*/
				ResultSet myRsCheck = myStmt.executeQuery("Select USER_ID, USER_NAME from usertable where USER_NAME = '" + nameTextField.getText().toString() + "' AND USER_PASSWORD = '" + passwordTextField.getText().toString() +"'");
				if(myRsCheck.next()){	
					/** Entered credentials are valid and the user should be logged in and directed to the home page of the application. */
					loginUserID = myRsCheck.getInt("USER_ID");
					nameTextField.setText("");
					passwordTextField.setText("");
					loginWindow.setVisible(false);
					FrontPage.window.setVisible(true);
					FrontPage.panel.remove(FrontPage.loginButton);
					FrontPage.panel.remove(FrontPage.signupButton);
					FrontPage.planButton.setEnabled(true);
					FrontPage.invitationsButton.setEnabled(true);
					FrontPage.planButton.setToolTipText(null);
		        	FrontPage.invitationsButton.setToolTipText(null);
					
					/** On successfully logging in, instead of log in and sign up buttons log out button should appear on Home page. */
					FrontPage.logoutButton.setBounds(410, 37, 160, 30);
				 	FrontPage.logoutButton.setFont(new Font("Arial", Font.BOLD, 15));
				 	FrontPage.logoutButton.setForeground(new Color(225,0,0));
				 	FrontPage.logoutButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
				 	FrontPage.panel.add(FrontPage.logoutButton);
				 	FrontPage.logoutButton.addActionListener(new ActionListener(){
						public void actionPerformed(ActionEvent arg0){
							 FrontPage.panel.remove(FrontPage.logoutButton);
							 FrontPage.panel.add(FrontPage.loginButton);
							 FrontPage.panel.add(FrontPage.signupButton);
							 FrontPage.planButton.setEnabled(false);
							 FrontPage.invitationsButton.setEnabled(false);
							 FrontPage.planButton.setToolTipText("First log into the application.");
							 FrontPage.invitationsButton.setToolTipText("First log into the application.");
						}
				 	});
				 	 
				 	FrontPage.window.setVisible(true);
				}
				else
					// Validation 4: Entered credentials are invalid and the user should be asked to re-enter the details.
				 	JOptionPane.showMessageDialog(loginPanel, "Invalid credentials, enter again.", "Login - Invalid", JOptionPane.ERROR_MESSAGE);
				 myConn.close();
			}
			catch(Exception e1){
				e1.printStackTrace();
			}
		}
	}
}