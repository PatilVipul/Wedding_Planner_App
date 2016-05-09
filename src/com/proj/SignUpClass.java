/**	This class consists of design and logic about how a new user will register for Wedding Planner Application. User's user name and password
 * will be stored in the database so that these details will be further used at the time of log in.
 * @author Vipul Patil */

package com.proj;

import java.awt.Color;
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

public class SignUpClass{
	public static boolean status; 
	final JFrame signupWindow = new JFrame();
	final JPanel signupPanel = new JPanel();
	JLabel nameLabel = new JLabel("Enter user name: ");
	final JTextField nameTextField = new JTextField();
	JLabel passwordLabel = new JLabel("Enter password: ");
	final JPasswordField passwordTextField = new JPasswordField();
	JLabel repasswordLabel = new JLabel("Re-enter password: ");
	final JPasswordField repasswordTextField = new JPasswordField();
	JButton okButton = new JButton("OK");
	
	/** This constructor will generate a separate window which will allow the user to enter details required for registering for this application. */
	public SignUpClass(){		
		
		signupWindow.setTitle("Wedding Planner - Sign Up");					// frame for sign up.
		signupWindow.setVisible(true);
		signupWindow.setSize(300, 300);
		signupWindow.setResizable(false);
		signupWindow.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				signupWindow.setVisible(false);
				FrontPage.window.setVisible(true);
			}	
		});
		
		signupPanel.setLayout(null);										// panel which is placed on top of this frame.
		signupPanel.setPreferredSize(new Dimension(250, 250));
		signupPanel.setBackground(new Color(255,229,219));
		signupWindow.add(signupPanel);
		
		nameLabel.setFont(new Font("comic sans ms",Font.BOLD ,15));			// simple label asking a new user to enter it's user name.
		nameLabel.setBounds(10, 10, 200, 25);
		signupPanel.add(nameLabel);
		
		nameTextField.setFont(new Font("comic sans ms",Font.BOLD ,15));		// user will enter his or her user name here.
		nameTextField.setBounds(10, 35, 250, 25);
		signupPanel.add(nameTextField);
		nameTextField.addKeyListener(new KeyListener() {
			public void keyTyped(KeyEvent e){}
			public void keyReleased(KeyEvent e){}
			public void keyPressed(KeyEvent e){
				if(e.getKeyCode() == KeyEvent.VK_ENTER){
					signup();
				}
			}
		});
		
		passwordLabel.setFont(new Font("comic sans ms",Font.BOLD ,15));		// simple label asking a new user to enter it's password.
		passwordLabel.setBounds(10, 70, 200, 25);
		signupPanel.add(passwordLabel);
		
		passwordTextField.setBounds(10, 95, 250, 25);						// user will enter it's password here.
		signupPanel.add(passwordTextField);
		passwordTextField.addKeyListener(new KeyListener() {
			public void keyTyped(KeyEvent e){}
			public void keyReleased(KeyEvent e){}
			public void keyPressed(KeyEvent e){
				if(e.getKeyCode() == KeyEvent.VK_ENTER){
					signup();
				}
			}
		});
		
		repasswordLabel.setFont(new Font("comic sans ms",Font.BOLD ,15));	// to confirm, user will be asked to re-enter the same password.
		repasswordLabel.setBounds(10, 130, 200, 25);
		signupPanel.add(repasswordLabel);
		
		repasswordTextField.setBounds(10, 155, 250, 25);					// user will enter the same password again.
		signupPanel.add(repasswordTextField);
		repasswordTextField.addKeyListener(new KeyListener() {
			public void keyTyped(KeyEvent e){}
			public void keyReleased(KeyEvent e){}
			public void keyPressed(KeyEvent e){
				if(e.getKeyCode() == KeyEvent.VK_ENTER){
					signup();
				}
			}
		});
		
		/** After validating all the entered details, user will be either signed up for the application or else will be prompted with appropriate message. */
		okButton.setBounds(20, 200, 100, 30);
		signupPanel.add(okButton);
		okButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				signup();
			}
		});
		
		/** This button should close the sign up window and open the Home page of the application without registering the user. */
		JButton cancelButton = new JButton("Cancel");
		cancelButton.setBounds(150, 200, 100, 30);
		signupPanel.add(cancelButton);
		cancelButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				signupWindow.setVisible(false);
				FrontPage.window.setVisible(true);
			}
		});
	}
	
	@SuppressWarnings("deprecation")
	public void signup(){
		if(nameTextField.getText().length() == 0){	// Validation 1: User doesn't enters a user name. 
			JOptionPane.showMessageDialog(signupPanel, "Enter user name.", "Sign Up - User Name", JOptionPane.ERROR_MESSAGE);
			nameTextField.requestFocus();
		}
		else if(passwordTextField.getText().length() == 0){	// Validation 2: User doesn't enters the password.
			JOptionPane.showMessageDialog(signupPanel, "Enter password.", "Sign Up - Password", JOptionPane.ERROR_MESSAGE);
			passwordTextField.requestFocus();
		}
		else if(passwordTextField.getText().length() < 8){	// Validation 3: The entered password is less than 8 characters.
			JOptionPane.showMessageDialog(signupPanel, "Enter strong password. (minimum 8 characters)", "Sign Up - Password", JOptionPane.ERROR_MESSAGE);
			passwordTextField.requestFocus();
		}
		else if(repasswordTextField.getText().length() == 0){	// Validation 4: User doesn't re-enter the password.
			JOptionPane.showMessageDialog(signupPanel, "Re-enter the password.", "Sign Up - Password", JOptionPane.ERROR_MESSAGE);
			repasswordTextField.requestFocus();
		}
		else if(!(passwordTextField.getText().equals(repasswordTextField.getText()))){	 // Validation 5: Re-entered password doesn't match the previous entered password.
			JOptionPane.showMessageDialog(signupPanel, "Password doesn't match, enter again.", "Sign Up - Password", JOptionPane.ERROR_MESSAGE);
			repasswordTextField.requestFocus();
		}
		else{
			try{
				Connection myConn = DriverManager.getConnection("jdbc:derby:test;create=true");	
				Statement myStmt = myConn.createStatement();
				 
				/** Execute SQL Query -> Select from Table: To check whether same user name already exists in the table	*/
				ResultSet myRsCheck = myStmt.executeQuery("Select USER_NAME from usertable where USER_NAME = '" + nameTextField.getText().toString() + "'");
				if(!myRsCheck.next()){
					/** Validation 6: Entered user name is unique so save the details in the database.
					 * Execute SQL Query -> Insert into Table	*/
					myStmt.executeUpdate("Insert into usertable(USER_NAME,USER_PASSWORD) VALUES('" + nameTextField.getText().toString() + "', '" + passwordTextField.getText().toString() + "')");
				 	JOptionPane.showMessageDialog(signupPanel, "Successfully signed up. Thank you.", "Sign Up - Success", JOptionPane.INFORMATION_MESSAGE);
				 	
				 	nameTextField.setText("");
				 	passwordTextField.setText("");
				 	repasswordTextField.setText("");
				 	signupWindow.setVisible(false);
				 	FrontPage.window.setVisible(true);
				}
				else		// Entered user name is already taken. 
					JOptionPane.showMessageDialog(signupPanel, "User name already present. Enter some unique user name.", "Sign Up - User Name", JOptionPane.ERROR_MESSAGE);
				myConn.close();
			}
			catch(Exception e1){
				e1.printStackTrace();
			}
		}
	}
}