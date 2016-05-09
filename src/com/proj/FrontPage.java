/**	This class displays the Home Page of Wedding Planner. It consists of control flow for all the major functionalities of this application
 * like log in, sign up, plan a wedding and send invitation.
 * @author Vipul Patil */

package com.proj;

import java.awt.Button;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class FrontPage{
	final static Button loginButton = new Button("Log In");
	final static Button signupButton = new Button("Sign Up");
	final static Button logoutButton = new Button("Log Out");
	final static JFrame window = new JFrame("Wedding Planner - Welcome");
	final static JPanel panel = new JPanel();
	final static JButton planButton = new JButton("Plan a Wedding");
	final static JButton invitationsButton = new JButton("Wedding Invitations");
	
	/** This class contains the main function which indicates that the control flow of the whole application begins from this page.
	 * @param args 
	 * @throws ClassNotFoundException */
	public static void main(String[] args) throws ClassNotFoundException{
		window.setSize(630, 600);	// (width, height)	/** Front Page Frame: window */
		window.setResizable(false);
		
		/** As this is the main window, if the user clicks to close this window then they should be asked once again whether he or she has to exit for sure. */
		window.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				int selectedOption = JOptionPane.showConfirmDialog(window, "Exit Wedding Planner?", "Confirm Exit", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
			 	if(selectedOption == JOptionPane.YES_OPTION)
			 		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				else if(selectedOption == JOptionPane.NO_OPTION)
					window.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
				else
					window.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
			}
		});
		
		/** panel is contained on top of the window, on which various components can be added */
		panel.setLayout(null);
		panel.setPreferredSize(new Dimension(560,540));	// (width, height)
		panel.setBackground(new Color(255,229,219));	// (r,g,b)
	    window.add(panel);
		
	    /** The title of the project: Wedding Planner is displayed using a simple label */
		JLabel titleLabel = new JLabel("Wedding Planner");
		titleLabel.setFont(new Font("Curlz MT", Font.BOLD, 45));
    	titleLabel.setForeground(new Color(225,0,0));	
    	titleLabel.setBounds(30, 20, 350, 55);			// (x, y, width, height)
    	panel.add(titleLabel);
    	
    	/** This button is for a returning user who wants to log into the application */
    	loginButton.setFont(new Font("Arial", Font.BOLD, 15));
    	loginButton.setBounds(410, 37, 80, 30);
    	loginButton.setForeground(new Color(225,0,0));
    	loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
    	panel.add(loginButton);
    	loginButton.addActionListener(new ActionListener(){		// When this button is clicked log in window should be opened.
			public void actionPerformed(ActionEvent arg0){
				window.setVisible(false);
				new LoginClass();
			}
		});
    	
    	/** This button is for a new user who wants to sign up for the application */
    	signupButton.setFont(new Font("Arial", Font.BOLD, 15));
    	signupButton.setBounds(500, 37, 80, 30);
    	signupButton.setForeground(new Color(225,0,0));
    	signupButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
    	panel.add(signupButton);
    	signupButton.addActionListener(new ActionListener(){		// When this button is clicked sign up window should be opened.
			public void actionPerformed(ActionEvent e){
				window.setVisible(false);
				new SignUpClass();
			}
		});
    	    	
    	/** A cover image is loaded and displayed on the front page of the application */
    	ImageIcon frontPageImageIcon = new ImageIcon(FrontPage.class.getResource("/cover-frontpage.jpg"));
    	JLabel frontPageImageLabel = new JLabel(frontPageImageIcon);
    	frontPageImageLabel.setBounds(25, 100, 550, 325);
    	frontPageImageLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 15));
    	panel.add(frontPageImageLabel);
	    
    	/**	This button is for planning the wedding which is disabled initially until user logs in */ 
    	planButton.setEnabled(false);
    	planButton.setBounds(125, 475, 150, 30);
    	planButton.setToolTipText("First log into the application.");
    	panel.add(planButton);
    	planButton.addActionListener(new ActionListener(){			// On click of this button planner page should be displayed.
			public void actionPerformed(ActionEvent e){
				window.setVisible(false);
				new PlannerPage();
			}
		});
    	
    	/** This button is for sending invitations which is also disabled initially until user logs in */
    	invitationsButton.setEnabled(false);
	    invitationsButton.setBounds(335, 475, 150, 30);
	    invitationsButton.setToolTipText("First log into the application.");
	    panel.add(invitationsButton);
        invitationsButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){				// This button should open the invitation page.
				window.setVisible(false);
				new InvitationsPage();
			}
		});
        
        JLabel copyrightLabel = new JLabel("© Wedding Planner - Created by Vipul Patil");
        copyrightLabel.setBounds(185,550,250,20);
        copyrightLabel.setForeground(new Color(60,60,60));
        panel.add(copyrightLabel);
        
	    window.setVisible(true);
	    
	    Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
		Connection connection;
		try{
			connection = DriverManager.getConnection("jdbc:derby:test;create=true");
			java.sql.DatabaseMetaData dbmd = connection.getMetaData();
			ResultSet rs = dbmd.getTables(null, null, "usertable", null);
			if(!rs.next()){
				connection.createStatement().execute("CREATE TABLE usertable(USER_ID INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), USER_NAME varchar(50) NOT NULL, USER_PASSWORD varchar(20) NOT NULL, PRIMARY KEY (User_ID))");
				
				connection.createStatement().execute("CREATE TABLE locationtable(LocationID int NOT NULL, LocationName varchar(45) NOT NULL, LocationCost int NOT NULL, LocationPhoto varchar(200) NOT NULL, PRIMARY KEY (LocationID))");
				connection.createStatement().execute("INSERT INTO locationtable VALUES (1,'Santa Clara',2500,'/Location_SantaClara.jpg'),(2,'Udaipur',3600,'/Location_Udaipur.jpg'),(3,'Fremont',1000,'/Location_Fremont.jpg'),(4,'Mountain View',3500,'/Location_MountainView.jpg'),(5,'Sunnyvale',950,'/Location_Sunnyvale.jpg'),(6,'San Francisco',3200,'/Location_SanFrancisco.jpg'),(7,'San Jose',1500,'/Location_SanJose.jpg')");
				
				connection.createStatement().execute("CREATE TABLE bridedresstable(BrideDressID int NOT NULL, BrideDressDesigner varchar(80) NOT NULL, BrideDressCost int NOT NULL, BrideDressPhoto varchar(80) NOT NULL, PRIMARY KEY (BrideDressID))");
				connection.createStatement().execute("INSERT INTO bridedresstable VALUES (1,'Maggie Sottero',2500,'/BrideDress_MaggieSottero.jpg'),(2,'Deepika Padukone',3300,'/BrideDress_DeepikaPadukone.jpg'),(3,'Mark Zunino',3500,'/BrideDress_MarkZunino.jpg'),(4,'Alvina Valenta',3200,'/BrideDress_AlvinaValenta.jpg'),(5,'Augusta Jones',2750,'/BrideDress_AugustaJones.jpg'),(6,'Danielle Caprese',5000,'/BrideDress_DanielleCaprese.jpg'),(7,'Pnina Tornai',3800,'/BrideDress_PninaTornai.jpg'),(8,'Pallas Couture',7500,'/BrideDress_PallasCouture.jpg'),(9,'Ian Stuart',4200,'/BrideDress_IanStuart.jpg'),(10,'Lazaro',5600,'/BrideDress_Lazaro.jpg'),(11,'Austin Scarlett',2000,'/BrideDress_AustinScarlett.jpg')");
						
				connection.createStatement().execute("CREATE TABLE groomdresstable(GroomDressID int NOT NULL, GroomDressDesigner varchar(80) NOT NULL, GroomDressCost int NOT NULL, GroomDressPhoto varchar(80) NOT NULL, PRIMARY KEY (GroomDressID))");
				connection.createStatement().execute("INSERT INTO groomdresstable VALUES (1,'Pronto Uomo',2750,'/GroomDress_ProntoUomo.jpg'),(2,'Ranbir Kapoor',3300,'/GroomDress_RanbirKapoor.jpg'),(3,'Joseph Abboud',2200,'/GroomDress_JosephAbboud.jpg'),(4,'Calvin Klein',3750,'/GroomDress_CalvinKlein.jpg'),(5,'Armani',1750,'/GroomDress_Armani.jpg'),(6,'Mon Cheri',3500,'/GroomDress_MonCheri.jpg'),(7,'Rina Di Montella',2000,'/GroomDress_RinaDiMontella.jpg'),(8,'Paul Smith',1500,'/GroomDress_PaulSmith.jpg'),(9,'Vera Wang',2200,'/GroomDress_VeraWang.jpg'),(10,'Alexander McQueen',4200,'/GroomDress_AlexanderMcQueen.jpg'),(11,'Joseph & Feiss',3000,'/GroomDress_Joseph&Feiss.jpg')");
				
				connection.createStatement().execute("CREATE TABLE cateringtable(dishID int NOT NULL, dishName varchar(60) NOT NULL, dishCost int NOT NULL, PRIMARY KEY (dishID))");
				connection.createStatement().execute("INSERT INTO cateringtable VALUES (1,'Lemon Chicken Breast ............ $5',5),(2,'Herb Roasted Chicken ........... $4',4),(3,'Baked BBQ Chicken ................. $8',8),(4,'Lasagna with Meat Sauce ..... $5',5),(5,'Vegetable Lasagna ................... $3',3),(6,'Three Cheese Lasagna .......... $4',4),(7,'Smoked and Roasted Beef ..... $7',7),(8,'Prime Rib Roast ....................... $9',9),(9,'Glazed Ham ................................. $6',6),(10,'Baked Salmon ........................ $11',11),(11,'Swedish Meatballs .................... $8',8),(12,'Beef Franks ............................... $8',8),(13,'Vegeterian Burgers .................. $3',3),(14,'Sliced Meats and Cheese ...... $7',7),(15,'Chicken Burgers ....................... $5',5),(16,'Caesar Salad ............................ $7',7),(17,'Tuscan Pasta Salad ................. $7',7),(18,'Spring Mix Salad ...................... $7',7),(19,'Chicken Salad ............................ $9',9),(20,'Herbed Red Potato .................. $4',4),(21,'Mashed Sweet Potatoes ......... $5',5),(22,'Potato Salad .............................. $7',7),(23,'Roasted Turkey ....................... $13',13),(24,'Steamed Broccoli .................... $6',6),(25,'California Vegetable Blend ..... $8',8),(26,'Fresh Fruit Tray ......................... $4',4),(27,'Donut Extravaganza ............... $10',10),(28,'Chunky Cherry Sundae ........... $9',9),(29,'Hot Fudge Sundae ................... $11',11),(30,'Caramel Sundae ....................... $8',8)");
				
				connection.createStatement().execute("CREATE TABLE savecatering(USER_ID int NOT NULL, dishID int NOT NULL)");
				connection.createStatement().execute("CREATE TABLE savedatatable (USER_ID int NOT NULL, LocationID int NOT NULL, BrideDressID int NOT NULL, GroomDressID int NOT NULL, LocationPhoto varchar(45) NOT NULL, BrideDressPhoto varchar(45) NOT NULL, GroomDressPhoto varchar(45) NOT NULL, DecorationThemeID int NOT NULL, TotalEstimation int NOT NULL, TotalGuests int NOT NULL, CateringCost int NOT NULL, PRIMARY KEY (USER_ID))");
				connection.createStatement().execute("CREATE TABLE invitationtable (USER_ID int NOT NULL, EmailAddress varchar(45) NOT NULL, Password varchar(45) NOT NULL, EmailTo varchar(4000) NOT NULL, EmailSubject varchar(100) NOT NULL, EmailBody varchar(1000) NOT NULL, PRIMARY KEY (USER_ID))");
			}
			connection.close();
		} 
		catch (SQLException e1){
			System.out.println("Tables already exists");
			System.exit(0);
		}
	}
}