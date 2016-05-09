/** This page will allow the user to send e-invitations of his/her wedding to the guests. This feature is completely independent of the 
 * planning process. So the user can simultaneously plan and invite the guests.
 * @author Vipul Patil */

package com.proj;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

import javax.mail.AuthenticationFailedException;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;

public class InvitationsPage{
	JFrame invitationWindow = new JFrame("Wedding Planner - Invitation");
	final JPanel invitationPanel = new JPanel();
	JLabel lineLabel = new JLabel("Enter the following details for sending email invitation...");
	JLabel emailAddressLabel = new JLabel("Your email address: ");
	final JTextField emailAddressTextField = new JTextField();
	JLabel passwordLabel = new JLabel("Your account's password: ");
	final JPasswordField passwordTextField = new JPasswordField();
	JLabel toLabel = new JLabel("Recipients: ");
	final JTextField toTextField = new JTextField();
	JLabel subjectLabel = new JLabel("Subject: ");
	final JTextField subjectTextField = new JTextField();
	JLabel bodyLabel = new JLabel("Message: ");
	final JTextPane bodyTextPane = new JTextPane();
	
	/** Constructor which will open the invitations page for the user. */
	public InvitationsPage(){
		invitationWindow.setSize(600, 600);		
		invitationWindow.setResizable(false);
		
		invitationPanel.setSize(600, 600);
		invitationPanel.setLayout(null);
		invitationPanel.setBackground(new Color(255,229,219));
		
		lineLabel.setBounds(10, 10, 500, 20);
		lineLabel.setFont(new Font("comic sans ms",Font.BOLD ,15));
		invitationPanel.add(lineLabel);
		
		emailAddressLabel.setBounds(10, 40, 200, 20);		// Simple label asking user to enter his/her accounts email address.
		emailAddressLabel.setFont(new Font("comic sans ms",Font.BOLD ,13));
		invitationPanel.add(emailAddressLabel);
		
		emailAddressTextField.setBounds(180, 40, 250, 24);	// User will enter his/her email address here.
		invitationPanel.add(emailAddressTextField);
		
		passwordLabel.setBounds(10, 70, 200, 20);			// Label asking password of that account.
		passwordLabel.setFont(new Font("comic sans ms",Font.BOLD ,13));
		invitationPanel.add(passwordLabel);
		
		passwordTextField.setBounds(180, 70, 250, 24);		// User will enter the password of his/her email account here.
		invitationPanel.add(passwordTextField);
		
		toLabel.setBounds(10, 100, 200, 20);				// Label asking user to enter the recipients email addresses 
		toLabel.setFont(new Font("comic sans ms",Font.BOLD ,13));
		invitationPanel.add(toLabel);
		
		toTextField.setBounds(180, 100, 250, 24);			// User will enter email address of the guests. Multiple addresses can be entered using a ',' separator.
		invitationPanel.add(toTextField);
		toTextField.setToolTipText("Enter multiple addresses separated by a comma.");
		
		subjectLabel.setBounds(10, 130, 200, 20);			// Label asking user to enter some subject for the email.
		subjectLabel.setFont(new Font("comic sans ms",Font.BOLD ,13));
		invitationPanel.add(subjectLabel);
		
		subjectTextField.setBounds(180, 130, 250, 24);		// User will enter the subject of the invitation email here.
		invitationPanel.add(subjectTextField);
			
		bodyLabel.setBounds(10, 160, 200, 20);				// Label asking user to enter main content, body of the email.
		bodyLabel.setFont(new Font("comic sans ms",Font.BOLD ,13));
		invitationPanel.add(bodyLabel);
		
		JScrollPane scroll = new JScrollPane(bodyTextPane, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);		
		scroll.setBounds(180, 160, 400, 300);			// User will frame and construct the invitation email in this space. 
		invitationPanel.add(scroll);
		
		try{
			Connection myConn = DriverManager.getConnection("jdbc:derby:test;create=true");	// Get a connection	
			Statement myStmt = myConn.createStatement();		// Create a statement
			// This SQL query will retrieve all the details of this page, if the user has saved them previously.
			ResultSet retrieveRs = myStmt.executeQuery("Select EmailAddress, Password, EmailTo, EmailSubject, EmailBody from invitationtable where USER_ID = " + LoginClass.loginUserID);
			if(retrieveRs.next()){
				emailAddressTextField.setText(retrieveRs.getString("EmailAddress").toString());
				passwordTextField.setText(retrieveRs.getString("Password").toString());
				toTextField.setText(retrieveRs.getString("EmailTo").toString());
				subjectTextField.setText(retrieveRs.getString("EmailSubject").toString());
				bodyTextPane.setText(retrieveRs.getString("EmailBody").toString());
			}
			else{	// If not saved anything previously then leave all the fields blank.
				emailAddressTextField.setText("");
				passwordTextField.setText("");
				toTextField.setText("");
				subjectTextField.setText("");
				bodyTextPane.setText("");
			}
			myConn.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		// Before closing the invitation window warn the user and ask whether he/she has to save all this data which would be helpful in the future. 
		invitationWindow.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
			 	int selectedOption = JOptionPane.showConfirmDialog(invitationWindow, "Save any changes from this page?", "Confirm Save", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
			 	if(selectedOption == JOptionPane.YES_OPTION){
			 		saveInvitationData();	// if user says 'Yes' then save all the data in the database table.
			 		invitationWindow.setVisible(false);
			 		FrontPage.window.setVisible(true);
			 	}
			 	else if(selectedOption == JOptionPane.NO_OPTION){
			 		invitationWindow.setVisible(false);		// If user selects 'No' then simple close the invitation window and open the Home page of the application.
			 		FrontPage.window.setVisible(true);
			 	}
			 	else
			 		invitationWindow.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
			}
		});

		// Separate button for allowing user to save the data in the database. (Same functionality like ctrl+S)
		JButton saveButton = new JButton("Save Template");
		saveButton.setBounds(50, 500, 120, 30);
		invitationPanel.add(saveButton);
		saveButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0){
				saveInvitationData();
				JOptionPane.showMessageDialog(invitationPanel, "Email template saved.", "Invitation - Template", JOptionPane.INFORMATION_MESSAGE);
			}	
		});
		
		// This button click will send the email to all the recipients. 
		JButton sendButton = new JButton("Send Email");
		sendButton.setBounds(250, 500, 100, 30);
		invitationPanel.add(sendButton);
		sendButton.addActionListener(new ActionListener(){
			@SuppressWarnings("deprecation")
			public void actionPerformed(ActionEvent arg0){
				if(emailAddressTextField.getText().length() == 0){	// Validation 1: Email address not entered.
					JOptionPane.showMessageDialog(invitationPanel, "Enter your email address.", "Invitation - Email", JOptionPane.ERROR_MESSAGE);
					emailAddressTextField.requestFocus();
				}
				else if(!(emailAddressTextField.getText().toLowerCase().contains("@yahoo.com") || emailAddressTextField.getText().toLowerCase().contains("@gmail.com"))) {
					JOptionPane.showMessageDialog(invitationPanel, "Only Gmail and Yahoo accounts addresses.", "Invitation - Email", JOptionPane.ERROR_MESSAGE);
					emailAddressTextField.requestFocus();
				}
				else if(passwordTextField.getText().length() == 0){	// Validation 2: Password not entered.
					JOptionPane.showMessageDialog(invitationPanel, "Enter your password.", "Invitation - Password", JOptionPane.ERROR_MESSAGE);
					passwordTextField.requestFocus();
				}
				else if(toTextField.getText().length() == 0){		// Validation 3: Recipients email address is not entered.
					JOptionPane.showMessageDialog(invitationPanel, "Enter recipients email address.", "Invitation - Recipient", JOptionPane.ERROR_MESSAGE);
					toTextField.requestFocus();
				}
				else if(!(toTextField.getText().toLowerCase().contains("@yahoo.com") || toTextField.getText().toLowerCase().contains("@gmail.com"))) {
					JOptionPane.showMessageDialog(invitationPanel, "Only Gmail and Yahoo accounts addresses.", "Invitation - Recipient", JOptionPane.ERROR_MESSAGE);
					toTextField.requestFocus();
				}
				else if(subjectTextField.getText().length() == 0){	// Validation 4: Subject field is empty.
					JOptionPane.showMessageDialog(invitationPanel, "Enter some subject.", "Invitation - Subject", JOptionPane.ERROR_MESSAGE);
					subjectTextField.requestFocus();
				}
				else if(bodyTextPane.getText().length() == 0){		// Validation 5: Empty email, no body constructed. 
					JOptionPane.showMessageDialog(invitationPanel, "Enter some content.", "Invitation - Body", JOptionPane.ERROR_MESSAGE);
					bodyTextPane.requestFocus();
				}
				else{		// Set all the properties and put all the required fields.
					Properties properties = new Properties();
					properties.put("mail.smtp.auth", "true");
					properties.put("mail.smtp.starttls.enable", "true");
					if(emailAddressTextField.getText().toLowerCase().contains("@gmail.com"))	
						properties.put("mail.smtp.host", "smtp.gmail.com");
					else if(emailAddressTextField.getText().toLowerCase().contains("@yahoo.com"))
						properties.put("mail.smtp.host", "smtp.mail.yahoo.com");
					properties.put("mail.smtp.port", "587");
				
					final String username = emailAddressTextField.getText();
					final String password = passwordTextField.getText();
					String toEmail = toTextField.getText();
					final String toNewEmail = toEmail.replaceAll("\\s+", "");
					final String subject = subjectTextField.getText();
					final String message = bodyTextPane.getText();
				
					// Confirm the account's email address and password is valid.
					try{
						Session session = Session.getInstance(properties, new Authenticator() {
							protected PasswordAuthentication getPasswordAuthentication(){
								return new PasswordAuthentication(username, password);
							}
						});
						Message textMessage = new MimeMessage(session);	// Construct the whole email
						textMessage.setFrom(new InternetAddress(username));
						textMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toNewEmail));
						textMessage.setSubject(subject);
						textMessage.setText(message);
						Transport.send(textMessage);		// send the email too all the recipients.
						JOptionPane.showMessageDialog(invitationPanel, "Invitation sent successfully.", "Invitation - Success", JOptionPane.INFORMATION_MESSAGE);
					}	
					catch (AuthenticationFailedException e) {
						JOptionPane.showMessageDialog(invitationPanel, "Username or password invalid.", "Invitation - Invalid", JOptionPane.ERROR_MESSAGE);
						emailAddressTextField.requestFocus();
					}
					catch(MessagingException e){
						JOptionPane.showMessageDialog(invitationPanel, "Couldn't connect to the server. Check the internet connection.", "Connection problem", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		
		JButton backButton = new JButton("Back");	// Simply close the invitation page without saving any data.
		backButton.setBounds(430, 500, 100, 30);
		invitationPanel.add(backButton);
		backButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0){
				
				int selectedOption = JOptionPane.showConfirmDialog(invitationWindow, "Save any changes from this page?", "Confirm Save", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
			 	if(selectedOption == JOptionPane.YES_OPTION){
			 		saveInvitationData();	// if user says 'Yes' then save all the data in the database table.
			 		invitationWindow.setVisible(false);
			 		FrontPage.window.setVisible(true);
			 	}
			 	else if(selectedOption == JOptionPane.NO_OPTION){
			 		invitationWindow.setVisible(false);		// If user selects 'No' then simple close the invitation window and open the Home page of the application.
			 		FrontPage.window.setVisible(true);
			 	}
			 	else
			 		invitationWindow.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
			}
		});
		
		invitationWindow.add(invitationPanel);
		invitationWindow.setVisible(true);
	}
	
	/** This method will help user by saving all the data on invitation page in the database table. */
	@SuppressWarnings("deprecation")
	public void saveInvitationData(){
		try{
			Connection myConn = DriverManager.getConnection("jdbc:derby:test;create=true");	
			Statement myStmt = myConn.createStatement();
			 
			/** Execute SQL Query -> Select from Table: To check whether User ID already exists in the table or not */
			ResultSet userRsCheck = myStmt.executeQuery("Select USER_ID from invitationtable where USER_ID = " + LoginClass.loginUserID);
			if(userRsCheck.next())	// If the user already exists then simply update the data.
				myStmt.executeUpdate("Update invitationtable set USER_ID = " + LoginClass.loginUserID + ", EmailAddress = '" + emailAddressTextField.getText() +  "', Password =  '" + passwordTextField.getText() + "', EmailTo = '" + toTextField.getText() + "', EmailSubject = '" + subjectTextField.getText() + "', EmailBody = '" + bodyTextPane.getText() + "' where USER_ID = " + LoginClass.loginUserID);
			else	// If the user has not save any data previously then insert it for the first time.
				myStmt.executeUpdate("Insert into invitationtable(USER_ID, EmailAddress, Password, EmailTo, EmailSubject, EmailBody) values(" + LoginClass.loginUserID + ", '" + emailAddressTextField.getText().toString() + "', '" + passwordTextField.getText().toString() + "', '" + toTextField.getText().toString() + "', '" + subjectTextField.getText().toString() + "', '" + bodyTextPane.getText().toString() + "')");
			myConn.close();
		}
		catch(Exception e){
			e.printStackTrace();
			JOptionPane.showMessageDialog(invitationPanel, "Database problem. Data not saved.", "Invitation - Database", JOptionPane.ERROR_MESSAGE);
		}
	}
}