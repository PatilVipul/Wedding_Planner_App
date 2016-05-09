/**	This class displays the Planner Page of Wedding Planner Application. It mainly consists selection of location, bride's dress, groom's dress 
 * decoration theme and catering. Based on all these selections of the user, total estimation amount of the wedding will also be displayed on 
 * this page so that according to his/her budget they can manage the selection. 
 * @author Vipul Patil */

package com.proj;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.*;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

public class PlannerPage{
	JLabel decorationImageLabel1 = new JLabel(), decorationImageLabel2 = new JLabel(), decorationImageLabel3 = new JLabel(), decorationImageLabel4 = new JLabel();
	int totalEstimationAmount = 0, locationCost = 0, brideDressCost = 0, groomDressCost = 0, decorationThemeCost = 0, cateringCost = 0;
	JLabel totalEstimationLabel = new JLabel();
	final JFrame plannerWindow = new JFrame("Wedding Planner - Plan");
	public int decorationID = 1;
	String locations[] = {"Select Wedding Location","Santa Clara","Udaipur","Fremont","Mountain View","Sunnyvale","San Francisco","San Jose"};
	final JComboBox<?> locationComboBox = new JComboBox<>(locations);
	String brideDresses[] = {"Select Bride's Dress Designer", "Maggie Sottero","Deepika Padukone","Mark Zunino","Alvina Valenta","Augusta Jones", "Danielle Caprese", "Pnina Tornai", "Pallas Couture", "Ian Stuart", "Lazaro", "Austin Scarlett"};
	final JComboBox<?> brideDressesComboBox = new JComboBox<>(brideDresses);
	String groomDresses[] = {"Select Groom's Dress Designer", "Pronto Uomo", "Ranbir Kapoor", "Joseph Abboud", "Calvin Klein", "Armani", "Mon Cheri", "Rina Di Montella", "Paul Smith", "Vera Wang", "Alexander McQueen", "Joseph & Feiss"};
	final JComboBox<?> groomDressesComboBox = new JComboBox<>(groomDresses);
	final ImageIcon locationImageIcon = new ImageIcon();
	final JLabel locationImageLabel = new JLabel();
	final ImageIcon brideDressesImageIcon = new ImageIcon();
	final JLabel brideDressesImageLabel = new JLabel();
	final ImageIcon groomDressesImageIcon = new ImageIcon();
	final JLabel groomDressesImageLabel = new JLabel();
	final JRadioButton aquaRadioButton = new JRadioButton("Aqua");
	final JRadioButton goldenRadioButton = new JRadioButton("Golden");
	final JRadioButton redRadioButton = new JRadioButton("Red");
	final JRadioButton lavenderRadioButton = new JRadioButton("Lavender");
	final JButton estimationButton = new JButton("Calculate");	
	String locPhotoPath="", bridePhotoPath="", groomPhotoPath="";
	public boolean buttonPressed = false;
	int people = 0;
			
	public PlannerPage(){
		try{
			Connection myConn = DriverManager.getConnection("jdbc:derby:test;create=true");	
			Statement myStmt = myConn.createStatement();
			 
			/** Checking whether current user has already saved any data or not. */
   			ResultSet retrieveRs = myStmt.executeQuery("Select USER_ID, LocationID, BrideDressID, GroomDressID, LocationPhoto, BrideDressPhoto, GroomDressPhoto, DecorationThemeID, TotalEstimation from savedatatable where USER_ID = " + LoginClass.loginUserID);
			int locId = 0, brideId = 0, groomId = 0;
			if(retrieveRs == null){		// User has not saved any data on planner page previously. 
				locationComboBox.setSelectedIndex(0);
				brideDressesComboBox.setSelectedIndex(0);
				groomDressesComboBox.setSelectedIndex(0);
				locationCost = 0; brideDressCost = 0; groomDressCost = 0; decorationThemeCost = 0; cateringCost = 0;
				totalEstimationAmount = 0;
				aquaRadioButton.setFont(new Font("Comic Sans MS", Font.PLAIN, 13));
				goldenRadioButton.setFont(new Font("Comic Sans MS", Font.PLAIN, 13));
				redRadioButton.setFont(new Font("Comic Sans MS", Font.PLAIN, 13));
				lavenderRadioButton.setFont(new Font("Comic Sans MS", Font.PLAIN, 13));
			}
			else if(retrieveRs.next()){		// User have saved some data previously on planner page, so retrieve it and display on this page..
				locId = retrieveRs.getInt("LocationID");
				locationComboBox.setSelectedIndex(locId);
				brideId = retrieveRs.getInt("BrideDressID");
				brideDressesComboBox.setSelectedIndex(brideId);
				groomId = retrieveRs.getInt("GroomDressID");
				groomDressesComboBox.setSelectedIndex(groomId);
				if(locId != 0){
					Image img1 = new ImageIcon(PlannerPage.class.getResource(retrieveRs.getString("LocationPhoto").toString())).getImage();
					locationImageIcon.setImage(img1);
					locationImageLabel.setIcon(locationImageIcon);
					locationImageLabel.setBorder(BorderFactory.createLineBorder(Color.black, 1));
				}
				if(brideId  != 0){
					Image img2 = new ImageIcon(PlannerPage.class.getResource(retrieveRs.getString("BrideDressPhoto").toString())).getImage();
					brideDressesImageIcon.setImage(img2);
					brideDressesImageLabel.setIcon(brideDressesImageIcon);
					brideDressesImageLabel.setBorder(BorderFactory.createLineBorder(Color.black, 1));
				}
				if(groomId != 0){
					Image img3 = new ImageIcon(PlannerPage.class.getResource(retrieveRs.getString("GroomDressPhoto").toString())).getImage();
					groomDressesImageIcon.setImage(img3);
					groomDressesImageLabel.setIcon(groomDressesImageIcon);
					groomDressesImageLabel.setBorder(BorderFactory.createLineBorder(Color.black, 1));
				}
				int decorationID = retrieveRs.getInt("DecorationThemeID");
				switch(decorationID){
					case 1: aquaRadioButton.setSelected(true);
				 			aquaRadioButton.setFont(new Font("Comic Sans MS", Font.BOLD, 13));
				 			decorationImageLabel1.setBorder(BorderFactory.createLineBorder(Color.BLACK, 7));
				 			decorationThemeCost = 4500;
				 			break;
					 
				 	case 2: goldenRadioButton.setSelected(true);
				 			goldenRadioButton.setFont(new Font("Comic Sans MS", Font.BOLD, 13));
				 			decorationImageLabel2.setBorder(BorderFactory.createLineBorder(Color.BLACK, 7));
				 			decorationThemeCost = 5250;
				 			break;
					 
				 	case 3: redRadioButton.setSelected(true);
				 			redRadioButton.setFont(new Font("Comic Sans MS", Font.BOLD, 13));
				 			decorationImageLabel3.setBorder(BorderFactory.createLineBorder(Color.BLACK, 7));
				 			decorationThemeCost = 2750;
				 			break;
					 
				 	case 4: lavenderRadioButton.setSelected(true);
				 			lavenderRadioButton.setFont(new Font("Comic Sans MS", Font.BOLD, 13));
				 			decorationImageLabel4.setBorder(BorderFactory.createLineBorder(Color.BLACK, 7));
				 			decorationThemeCost = 2000;
				 			break;
					 
				 	default:aquaRadioButton.setSelected(true);
				 			decorationImageLabel1.setBorder(BorderFactory.createLineBorder(Color.BLACK, 7));
				}
				totalEstimationLabel.setText("");
			}
			myConn.close();
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		
		plannerWindow.setSize(627,730);		// This frame is for opening new window for Planner page.
		plannerWindow.setResizable(false);
		
		JPanel plannerPanel = new JPanel();	// This panel will be placed on top of this frame.
		plannerPanel.setLayout(null);
		plannerPanel.setPreferredSize(new Dimension(600, 1000));
		plannerPanel.setBackground(new Color(255,229,219));
		plannerWindow.add(plannerPanel);		
		
		totalEstimationLabel.setBounds(350, 945, 225, 40);	// At the bottom of the page, total estimation of whole wedding will be displayed using this label.
		totalEstimationLabel.setFont(new Font("Times New Roman", Font.BOLD, 20));
		
		locationComboBox.setBounds(10, 10, 175, 20);	// User can select various locations according to his/her choice and budget.
		plannerPanel.add(locationComboBox);
		
    	locationImageLabel.setBounds(10, 40, 550, 250);	//Based on user's location selection, image of that particular location will be displayed here
    	locationImageLabel.setBorder(BorderFactory.createLineBorder(Color.black, 2));
    	plannerPanel.add(locationImageLabel);
    	
    	locationComboBox.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0){
				// Retrieve data from locationtable depending upon the value selected in LocationComboBox
				if(locationComboBox.getSelectedIndex() != 0){
					try{
						Connection myConn = DriverManager.getConnection("jdbc:derby:test;create=true");	
						Statement myStmt = myConn.createStatement();
					 
						/** Execute SQL Query -> Select from Table: To retrieve image path and cost of the location from locationtable */
						ResultSet locationRs = myStmt.executeQuery("Select LocationPhoto, LocationCost from locationtable where LocationID = " + locationComboBox.getSelectedIndex());
						if(locationRs.next()){
							locPhotoPath = locationRs.getString("LocationPhoto");
							locationCost = locationRs.getInt("LocationCost");
						}
						myConn.close();
					}	
					catch(Exception e1){
						e1.printStackTrace();
					}
					
					// load the image of that location using that retrieved image path.
					Image img = new ImageIcon(PlannerPage.class.getResource(locPhotoPath)).getImage();
					locationImageIcon.setImage(img);
					locationImageLabel.setIcon(locationImageIcon);
					locationImageLabel.setBorder(BorderFactory.createLineBorder(Color.black, 1));
				}
			}
		});
    	
		brideDressesComboBox.setBounds(10, 300, 200, 20);	// User can select a bride's dress from a variety of collection from here.
		plannerPanel.add(brideDressesComboBox);
		
    	brideDressesImageLabel.setBounds(10, 330, 270, 400);	// Based on user's selection, bride's dress photo will be displayed here. 
    	brideDressesImageLabel.setBorder(BorderFactory.createLineBorder(Color.black, 2));
    	plannerPanel.add(brideDressesImageLabel);

    	brideDressesComboBox.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0){
				// Retrieve data from bridedresstable depending upon the value selected in brideDressesComboBox
				if(brideDressesComboBox.getSelectedIndex() != 0){
					try{
						Connection myConn = DriverManager.getConnection("jdbc:derby:test;create=true");	
						Statement myStmt = myConn.createStatement();
						/** Execute SQL Query -> Select from Table: To retrieve image path and cost of the bride's dress from bridedresstable */
						ResultSet brideDressRs = myStmt.executeQuery("Select BrideDressPhoto, BrideDressCost from bridedresstable where BrideDressID = " + brideDressesComboBox.getSelectedIndex());
						if(brideDressRs.next()){
							bridePhotoPath = brideDressRs.getString("BrideDressPhoto");
							brideDressCost = brideDressRs.getInt("BrideDressCost");
						}
						myConn.close();
					}
					catch(Exception e1){
						e1.printStackTrace();
					}
					
					// load the image of that bride's dress using that retrieved image path.
					Image img = new ImageIcon(PlannerPage.class.getResource(bridePhotoPath)).getImage();
					brideDressesImageIcon.setImage(img);
					brideDressesImageLabel.setIcon(brideDressesImageIcon);
					brideDressesImageLabel.setBorder(BorderFactory.createLineBorder(Color.black, 1));
				}
			}
		});
    	
		groomDressesComboBox.setBounds(290, 300, 210, 20);	// User can select a groom's dress from a variety of collection from here.
		plannerPanel.add(groomDressesComboBox);
			
    	groomDressesImageLabel.setBounds(290, 330, 270, 400);	// Based on user's selection, groom's dress photo will be displayed here.
    	groomDressesImageLabel.setBorder(BorderFactory.createLineBorder(Color.black, 2));
    	plannerPanel.add(groomDressesImageLabel);
    	
    	groomDressesComboBox.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0){
				// Retrieve data from groomdresstable depending upon the value selected in groomDressesComboBox
				if(groomDressesComboBox.getSelectedIndex() != 0){
					try{
						Connection myConn = DriverManager.getConnection("jdbc:derby:test;create=true");	
						Statement myStmt = myConn.createStatement();
						/** Execute SQL Query -> Select from Table: To retrieve image path and cost of the groom's dress from groomdresstable */					 
						ResultSet groomDressRs = myStmt.executeQuery("Select GroomDressPhoto, GroomDressCost from groomdresstable where GroomDressID = " + groomDressesComboBox.getSelectedIndex());
						if(groomDressRs.next()){
							groomPhotoPath = groomDressRs.getString("GroomDressPhoto");
							groomDressCost = groomDressRs.getInt("GroomDressCost");
						}
						myConn.close();
					}
					catch(Exception e1){
						e1.printStackTrace();
					}
				
					// load the image of that bride's dress using that retrieved image path.
					Image img = new ImageIcon(PlannerPage.class.getResource(groomPhotoPath)).getImage();
					groomDressesImageIcon.setImage(img);
					groomDressesImageLabel.setIcon(groomDressesImageIcon);
					groomDressesImageLabel.setBorder(BorderFactory.createLineBorder(Color.black, 1));
				}
			}
		});
    	
    	JLabel decorationLabel = new JLabel("Select the decoration theme for wedding location...");
    	decorationLabel.setBounds(10, 740, 300, 20);
    	plannerPanel.add(decorationLabel);
    	
    	JPanel decorationPhotoPanel = new JPanel();		// Separate panel to display various decoration color theme images.
    	decorationPhotoPanel.setBounds(10, 760, 550, 180);
    	decorationPhotoPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
    	decorationPhotoPanel.setBackground(new Color(255,229,219));
    	decorationPhotoPanel.setLayout(new FlowLayout());
    	
    	// Loading first image theme - Aqua
    	ImageIcon decorationImageIcon1 = new ImageIcon(PlannerPage.class.getResource("/DecorationIcon_Aqua.jpg"));
    	decorationImageLabel1.setIcon(decorationImageIcon1);
    	decorationImageLabel1.setPreferredSize(new Dimension(130, 135));
    	decorationImageLabel1.setCursor(new Cursor(Cursor.HAND_CURSOR));
    	decorationImageLabel1.setToolTipText("Click to enlarge");
    	decorationPhotoPanel.add(decorationImageLabel1);
    	// On clicking on the first image, it should be enlarged.
    	decorationImageLabel1.addMouseListener(new MouseAdapter(){
    		public void mouseClicked(MouseEvent e){
    			decorationThemeCost = 4500;
    			decorationID = 1;
    			newFrame("Decoration Theme - Aqua", "/Decoration_Aqua.jpg");	// this will enlarge the Aqua-image and open into new frame.
    			// Borders around the images are managed according to the user's selection
    			decorationImageLabel1.setBorder(BorderFactory.createLineBorder(Color.BLACK, 7));
    			decorationImageLabel2.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
    			decorationImageLabel3.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
    			decorationImageLabel4.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
    			// Also the radio buttons below th images are managed according to the user's selection.
    			aquaRadioButton.setSelected(true);
    			aquaRadioButton.setFont(new Font("Comic Sans MS", Font.BOLD, 13));
    			goldenRadioButton.setSelected(false);
    			goldenRadioButton.setFont(new Font("Comic Sans MS", Font.PLAIN, 13));
    			redRadioButton.setSelected(false);
    			redRadioButton.setFont(new Font("Comic Sans MS", Font.PLAIN, 13));
    			lavenderRadioButton.setSelected(false);
    			lavenderRadioButton.setFont(new Font("Comic Sans MS", Font.PLAIN, 13));
    		}
		});
    	
    	ImageIcon decorationImageIcon2 = new ImageIcon(PlannerPage.class.getResource("/DecorationIcon_Golden.jpg"));
    	decorationImageLabel2.setIcon(decorationImageIcon2);
    	decorationImageLabel2.setPreferredSize(new Dimension(130, 135));
    	decorationImageLabel2.setCursor(new Cursor(Cursor.HAND_CURSOR));
    	decorationImageLabel2.setToolTipText("Click to enlarge");
    	decorationPhotoPanel.add(decorationImageLabel2);
    	// On clicking the second image, it should be enlarged.
    	decorationImageLabel2.addMouseListener(new MouseAdapter(){
    		public void mouseClicked(MouseEvent e){
    			decorationThemeCost = 5250;
    			decorationID = 2;
    			newFrame("Decoration Theme - Golden", "/Decoration_Golden.jpg");	// this will enlarge the Golden-image and open into new frame.
    			// Managing the borders of all the images according to the user's selection.
    			decorationImageLabel1.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
    			decorationImageLabel2.setBorder(BorderFactory.createLineBorder(Color.BLACK, 7));
    			decorationImageLabel3.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
    			decorationImageLabel4.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
    			// Managing all the radio buttons placed below the images, based on the user's selection.
    			aquaRadioButton.setSelected(false);
    			aquaRadioButton.setFont(new Font("Comic Sans MS", Font.PLAIN, 13));
    			goldenRadioButton.setSelected(true);
    			goldenRadioButton.setFont(new Font("Comic Sans MS", Font.BOLD, 13));
    			redRadioButton.setSelected(false);
    			redRadioButton.setFont(new Font("Comic Sans MS", Font.PLAIN, 13));
    			lavenderRadioButton.setSelected(false);
    			lavenderRadioButton.setFont(new Font("Comic Sans MS", Font.PLAIN, 13));
    		}
		});
    	
    	ImageIcon decorationImageIcon3 = new ImageIcon(PlannerPage.class.getResource("/DecorationIcon_Red.jpg"));
    	decorationImageLabel3.setIcon(decorationImageIcon3);
    	decorationImageLabel3.setPreferredSize(new Dimension(130, 135));
    	decorationImageLabel3.setCursor(new Cursor(Cursor.HAND_CURSOR));
    	decorationImageLabel3.setToolTipText("Click to enlarge");
    	decorationPhotoPanel.add(decorationImageLabel3);
    	// On clicking the third image, it should be enlarged.
    	decorationImageLabel3.addMouseListener(new MouseAdapter(){
    		public void mouseClicked(MouseEvent e){
    			decorationThemeCost = 2750;
    			decorationID = 3;
    			newFrame("Decoration Theme - Red", "/Decoration_Red.jpg");	// this will enlarge the Red-image and open into new frame.
    			// Managing all the borders of the images based on the user's selection.
    			decorationImageLabel1.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
    			decorationImageLabel2.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
    			decorationImageLabel3.setBorder(BorderFactory.createLineBorder(Color.BLACK, 7));
    			decorationImageLabel4.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
    			// Also setting correct radio buttons based on the user's selection.
    			aquaRadioButton.setSelected(false);
    			aquaRadioButton.setFont(new Font("Comic Sans MS", Font.PLAIN, 13));
    			goldenRadioButton.setSelected(false);
    			goldenRadioButton.setFont(new Font("Comic Sans MS", Font.PLAIN, 13));
    			redRadioButton.setSelected(true);
    			redRadioButton.setFont(new Font("Comic Sans MS", Font.BOLD, 13));
    			lavenderRadioButton.setSelected(false);
    			lavenderRadioButton.setFont(new Font("Comic Sans MS", Font.PLAIN, 13));
    		}
		});
    	
    	ImageIcon decorationImageIcon4 = new ImageIcon(PlannerPage.class.getResource("/DecorationIcon_Lavender.jpg"));
    	decorationImageLabel4.setIcon(decorationImageIcon4);
    	decorationImageLabel4.setPreferredSize(new Dimension(130, 135));
    	decorationImageLabel4.setCursor(new Cursor(Cursor.HAND_CURSOR));
    	decorationImageLabel4.setToolTipText("Click to enlarge");
    	decorationPhotoPanel.add(decorationImageLabel4);
    	// On clicking on this fourth image, it should be enlarged.
    	decorationImageLabel4.addMouseListener(new MouseAdapter(){
    		public void mouseClicked(MouseEvent e){
    			decorationThemeCost = 2000;
    			decorationID = 4;
    			newFrame("Decoration Theme - Lavender", "/Decoration_Lavender.jpg");	// The Lavender image will be enlarged and opened in  a new frame.
    			// Managing borders of all the images according to the user's selection.
    			decorationImageLabel1.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
    			decorationImageLabel2.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
    			decorationImageLabel3.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
    			decorationImageLabel4.setBorder(BorderFactory.createLineBorder(Color.BLACK, 7));
    			// Managing all the radio buttons based on the user's selection.
    			aquaRadioButton.setSelected(false);
    			aquaRadioButton.setFont(new Font("Comic Sans MS", Font.PLAIN, 13));
    			goldenRadioButton.setSelected(false);
    			goldenRadioButton.setFont(new Font("Comic Sans MS", Font.PLAIN, 13));
    			redRadioButton.setSelected(false);
    			redRadioButton.setFont(new Font("Comic Sans MS", Font.PLAIN, 13));
    			lavenderRadioButton.setSelected(true);
    			lavenderRadioButton.setFont(new Font("Comic Sans MS", Font.BOLD, 13));
    		}
		});
    	
    	// Defining all the radio buttons which are placed under the decoration images on the decorationPanel. 
		aquaRadioButton.setPreferredSize(new Dimension(130, 20));
		aquaRadioButton.setBackground(new Color(255,229,219));
		aquaRadioButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		aquaRadioButton.setToolTipText("Click on the image to enlarge it");
		
		goldenRadioButton.setPreferredSize(new Dimension(130, 20));
		goldenRadioButton.setBackground(new Color(255,229,219));
		goldenRadioButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		goldenRadioButton.setToolTipText("Click on the image to enlarge it");

		redRadioButton.setPreferredSize(new Dimension(130, 20));
		redRadioButton.setBackground(new Color(255,229,219));
		redRadioButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		redRadioButton.setToolTipText("Click on the image to enlarge it");

		lavenderRadioButton.setPreferredSize(new Dimension(130, 20));
		lavenderRadioButton.setBackground(new Color(255,229,219));
		lavenderRadioButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		lavenderRadioButton.setToolTipText("Click on the image to enlarge it");

		//Group the radio buttons.
		ButtonGroup group = new ButtonGroup();
		group.add(aquaRadioButton);
		group.add(goldenRadioButton);
		group.add(redRadioButton);
		group.add(lavenderRadioButton);
		    
		//Register a listener for all the radio buttons. Based on user's selection manage the borders and and all the radio buttons.
	    aquaRadioButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0){
				decorationThemeCost = 4500;
				decorationID = 1;
				
				decorationImageLabel1.setBorder(BorderFactory.createLineBorder(Color.BLACK, 7));
				decorationImageLabel2.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
				decorationImageLabel3.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
				decorationImageLabel3.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
				
				aquaRadioButton.setFont(new Font("Comic Sans MS", Font.BOLD, 13));
				goldenRadioButton.setFont(new Font("Comic Sans MS", Font.PLAIN, 13));
				redRadioButton.setFont(new Font("Comic Sans MS", Font.PLAIN, 13));
				lavenderRadioButton.setFont(new Font("Comic Sans MS", Font.PLAIN, 13));
			}
		});
	    
		goldenRadioButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				decorationThemeCost = 5250;
				decorationID = 2;
				
				decorationImageLabel2.setBorder(BorderFactory.createLineBorder(Color.BLACK, 7));
				decorationImageLabel1.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
				decorationImageLabel3.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
				decorationImageLabel4.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
				
				aquaRadioButton.setFont(new Font("Comic Sans MS", Font.PLAIN, 13));
				goldenRadioButton.setFont(new Font("Comic Sans MS", Font.BOLD, 13));
				redRadioButton.setFont(new Font("Comic Sans MS", Font.PLAIN, 13));
				lavenderRadioButton.setFont(new Font("Comic Sans MS", Font.PLAIN, 13));
			}	
		});
		
		redRadioButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				decorationThemeCost = 2750;
				decorationID = 3;
				
				decorationImageLabel3.setBorder(BorderFactory.createLineBorder(Color.BLACK, 7));
				decorationImageLabel1.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
				decorationImageLabel2.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
				decorationImageLabel4.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
				
				aquaRadioButton.setFont(new Font("Comic Sans MS", Font.PLAIN, 13));
				goldenRadioButton.setFont(new Font("Comic Sans MS", Font.PLAIN, 13));
				redRadioButton.setFont(new Font("Comic Sans MS", Font.BOLD, 13));
				lavenderRadioButton.setFont(new Font("Comic Sans MS", Font.PLAIN, 13));
			}
		});
		
		lavenderRadioButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				decorationThemeCost = 2000;
				decorationID = 4;
				
				decorationImageLabel4.setBorder(BorderFactory.createLineBorder(Color.BLACK, 7));
				decorationImageLabel1.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
				decorationImageLabel2.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
				decorationImageLabel3.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
				
				aquaRadioButton.setFont(new Font("Comic Sans MS", Font.PLAIN, 13));
				goldenRadioButton.setFont(new Font("Comic Sans MS", Font.PLAIN, 13));
				redRadioButton.setFont(new Font("Comic Sans MS", Font.PLAIN, 13));
				lavenderRadioButton.setFont(new Font("Comic Sans MS", Font.BOLD, 13));
			}
		});	
		    
		decorationPhotoPanel.add(aquaRadioButton);
		decorationPhotoPanel.add(goldenRadioButton);
		decorationPhotoPanel.add(redRadioButton);
		decorationPhotoPanel.add(lavenderRadioButton);
		
		plannerPanel.add(decorationPhotoPanel);
		
		JButton cateringButton = new JButton("Catering");	// A button which will help the user decide the food menu for the wedding.
		cateringButton.setBounds(10, 950, 100, 30);
		cateringButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				cateringEstimation();
				plannerWindow.setVisible(false);
			}
		});
		
		plannerPanel.add(cateringButton);
	
		estimationButton.setBounds(120, 950, 100, 30);	// This button will let the user to know total estimation of his/her wedding.
		estimationButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0){
				try{
					Connection myConn = DriverManager.getConnection("jdbc:derby:test;create=true");	
					Statement myStmt = myConn.createStatement();
					// Retrieve the cost of all the selection of the user from the database.
					ResultSet locCostRs = myStmt.executeQuery("Select LocationCost from locationtable where LocationID = " + locationComboBox.getSelectedIndex());
					if(locCostRs.next())
						locationCost = locCostRs.getInt("LocationCost");
				
					ResultSet brideCostRs = myStmt.executeQuery("Select BrideDressCost from bridedresstable where BrideDressID = " + brideDressesComboBox.getSelectedIndex());
					if(brideCostRs.next())
						brideDressCost = brideCostRs.getInt("BrideDressCost");
				
					ResultSet groomCostRs = myStmt.executeQuery("Select GroomDressCost from groomdresstable where GroomDressID = " + groomDressesComboBox.getSelectedIndex());
					if(groomCostRs.next())
						groomDressCost = groomCostRs.getInt("GroomDressCost");
				
					ResultSet cateringRs = myStmt.executeQuery("Select CateringCost from savedatatable where USER_ID = " + LoginClass.loginUserID);
					if(cateringRs.next())
						cateringCost = cateringRs.getInt("CateringCost");
				
					myConn.close();
				}
				catch(Exception e){
					e.printStackTrace();
				}
				// Add the all the retrieved values and then display it on the totalEstimationLabel.
				totalEstimationAmount = locationCost + brideDressCost + groomDressCost + decorationThemeCost + cateringCost;
				totalEstimationLabel.setText("Total Estimation: $" + totalEstimationAmount);
			}
		});
		
		plannerPanel.add(estimationButton);
		plannerPanel.add(totalEstimationLabel);
		
		JButton backButton = new JButton("Back");	// A button which will help the user decide the food menu for the wedding.
		backButton.setBounds(230, 950, 100, 30);
		backButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				saveData();
			}
		});
		plannerPanel.add(backButton);
		
		// Add a scroll bar to the panel to allow the user to scroll vertically.
		JScrollPane scroll = new JScrollPane(plannerPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);		
		plannerWindow.add(scroll);
		
		// Ask user before closing that whether he/she has to save the changes made on this page.
		plannerWindow.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				saveData();
			}	
		});
		plannerWindow.setVisible(true);
	}
	
	public void saveData(){
		int selectedOption = JOptionPane.showConfirmDialog(plannerWindow, "Save any changes from this page?", "Confirm Save", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
		if(selectedOption == JOptionPane.YES_OPTION){
			int locCost = 0, brideCost = 0, groomCost = 0;
			try{
				Connection myConn = DriverManager.getConnection("jdbc:derby:test;create=true");			 
				Statement myStmt = myConn.createStatement();
				int userID = LoginClass.loginUserID;
				int locID = locationComboBox.getSelectedIndex();
				ResultSet locPathRs = myStmt.executeQuery("Select LocationCost, LocationPhoto from locationtable where LocationID = " + locationComboBox.getSelectedIndex());
				if(locPathRs.next()){
					locPhotoPath = locPathRs.getString("LocationPhoto");
					locCost = locPathRs.getInt("LocationCost");
				}

				int brideID = brideDressesComboBox.getSelectedIndex();
				ResultSet bridePathRs = myStmt.executeQuery("Select BrideDressCost, BrideDressPhoto from bridedresstable where BrideDressID = " + brideDressesComboBox.getSelectedIndex());
				if(bridePathRs.next()){
					bridePhotoPath = bridePathRs.getString("BrideDressPhoto");
					brideCost = bridePathRs.getInt("BrideDressCost");
				}
					 
				int groomID = groomDressesComboBox.getSelectedIndex();
				ResultSet groomPathRs = myStmt.executeQuery("Select GroomDressCost, GroomDressPhoto from groomdresstable where GroomDressID = " + groomDressesComboBox.getSelectedIndex());
				if(groomPathRs.next()){
					groomPhotoPath = groomPathRs.getString("GroomDressPhoto");
					groomCost = groomPathRs.getInt("GroomDressCost");
				}
					 
				if(aquaRadioButton.isSelected())
					decorationID = 1;
				else if(goldenRadioButton.isSelected())
					decorationID = 2;
				else if(redRadioButton.isSelected())
					decorationID = 3;
				else if(lavenderRadioButton.isSelected())
					decorationID = 4;
				else
					decorationThemeCost = 0;
				
				totalEstimationAmount = locCost + brideCost + groomCost + decorationThemeCost + cateringCost;
					 
				ResultSet userRs = myStmt.executeQuery("Select USER_ID from savedatatable where USER_ID = " + userID);
				if(userRs.next())			// If user data already exists, then simply update the previous entry 
					 myStmt.executeUpdate("Update savedatatable SET LocationID = " + locID + ", BrideDressID = " + brideID +", GroomDressID = " + groomID + ", LocationPhoto = '" + locPhotoPath + "', BrideDressPhoto = '" + bridePhotoPath + "', GroomDressPhoto = '" + groomPhotoPath + "', DecorationThemeID = " + decorationID + ", TotalEstimation = " + totalEstimationAmount + " where USER_ID = " + userID);
				else				// If the user is new and there is no user entry, then insert it into the table
					 myStmt.executeUpdate("Insert into savedatatable(USER_ID, LocationID, BrideDressID, GroomDressID, LocationPhoto, BrideDressPhoto, GroomDressPhoto, DecorationThemeID, TotalEstimation, TotalGuests, CateringCost) Values(" + userID + ", " + locID + ", " + brideID +", " +groomID + ", '" + locPhotoPath + "', '" + bridePhotoPath + "', '" + groomPhotoPath + "', " + decorationID + ", " + totalEstimationAmount + ", " + people + ", " + cateringCost + ")");
				myConn.close();
			}
			catch(Exception e1){
				e1.printStackTrace();
			}
		 	FrontPage.window.setVisible(true);
		 	plannerWindow.setVisible(false);
		}
		else if(selectedOption == JOptionPane.NO_OPTION){
			FrontPage.window.setVisible(true);
		 	plannerWindow.setVisible(false);
		}	  
		else
		 	plannerWindow.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	}
	
	/** This method is used when the user clicks on the decoration theme image. At that time a new window must appear with the enlarged version
	 * of that particular image.
	 * @param frameTitle To set the title of the new frame window.
	 * @param iconPath To set the enlarged image using icon path.
	 */
	public void newFrame(String frameTitle, String iconPath){				
		JLabel bigImageLabel = new JLabel();
		// load the bigger image
		Image img = new ImageIcon(PlannerPage.class.getResource(iconPath)).getImage();
    	ImageIcon bigImageIcon = new ImageIcon(img);
    	bigImageLabel.setIcon(bigImageIcon);
    	bigImageLabel.setBounds(10, 10, 325, 500);
    	bigImageLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
    	
		final JComponent[] inputs = new JComponent[] {bigImageLabel};
		JOptionPane.showMessageDialog(plannerWindow, inputs, frameTitle, JOptionPane.PLAIN_MESSAGE);
	}
	
	/** This method is triggered when the user clicks on the catering button. It manages all the details related to catering menu and catering total cost. */
	public void cateringEstimation(){
		final JFrame cateringFrame = new JFrame("Wedding Planner - Catering");	// new window will be displayed for food menu selection.
		cateringFrame.setVisible(true);
		cateringFrame.setSize(627, 600);
		cateringFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		final JPanel cateringPanel = new JPanel();
		cateringPanel.setLayout(null);
		cateringPanel.setBackground(new Color(255,229,219));
		
		JLabel dishesLabel = new JLabel("Select the dishes according to your choice (Prices per person)");
		dishesLabel.setBounds(10, 10, 450, 15);
		dishesLabel.setFont(new Font("Times New Roman", Font.BOLD, 15));
		cateringPanel.add(dishesLabel);
		
		JPanel dishesPanel = new JPanel();
		dishesPanel.setLayout(new GridLayout(15, 2, 60, 10));
		dishesPanel.setBounds(10, 35, 590, 400);
		dishesPanel.setBackground(new Color(255,229,219));
		// Check boxes array, for assisting user to select from variety of food items according to his/her choice and budget.
		final ArrayList<JCheckBox> checkboxes = new ArrayList<JCheckBox>();
		
		int i = 0;
		String dish = "";
		for(i = 0; i < 30; ++i){
			JCheckBox itemCheckBox = new JCheckBox();
			checkboxes.add(itemCheckBox);
			checkboxes.get(i).setSelected(false);
			try{
				Connection myConn = DriverManager.getConnection("jdbc:derby:test;create=true");	// Get a connection	
				Statement myStmt = myConn.createStatement();		// Create a statement
				// Retrieving dish name from cateringtable from the database and then assigning it to each checkbox. 
				ResultSet dishRs = myStmt.executeQuery("Select dishName from cateringtable where dishID = " + (i+1));
				if(dishRs.next()){
					dish = dishRs.getString("dishName");
					itemCheckBox.setText("  " + dish);
					itemCheckBox.setBackground(Color.WHITE);
				}
				 myConn.close();
			}
			catch(Exception e1){
				e1.printStackTrace();
			}
			dishesPanel.add(itemCheckBox);
		}
		cateringPanel.add(dishesPanel);
		
		JLabel personsLabel = new JLabel("Rough number of guests ");	// Simple label asking user to enter rough number of guests for the wedding.
		personsLabel.setBounds(10, 470, 175, 15);
		personsLabel.setFont(new Font("Times New Roman", Font.BOLD, 15));
		cateringPanel.add(personsLabel);
		
		final JTextField personsTextField = new JTextField();	// User can enter rough number of guests in order to calculate the catering cost for the wedding.
		personsTextField.setBounds(185, 468, 90, 20);
		personsTextField.setFont(new Font("Times New Roman", Font.BOLD, 15));
		personsTextField.addKeyListener(new KeyListener(){
			public void keyTyped(KeyEvent e){
				char vchar = e.getKeyChar();	// Validation: Only digits must be entered in the personsTextField.
				if(!(Character.isDigit(vchar)) || (vchar == KeyEvent.VK_BACK_SPACE) || (vchar == KeyEvent.VK_DELETE))
					e.consume();
			}
			public void keyReleased(KeyEvent arg0) {}
			public void keyPressed(KeyEvent arg0) {}
		});
		cateringPanel.add(personsTextField);
		
		final JLabel amountLabel = new JLabel("Total catering amount: ");	// Label displaying the total catering amount calculated.
		amountLabel.setBounds(338, 468, 300, 15);
		amountLabel.setFont(new Font("Times New Roman", Font.BOLD, 15));
		cateringPanel.add(amountLabel);
				
		/** Initially, if the user has saved any catering data in the database then first retrieve it and display it during loading the page. So that the user can edit or make changes to previous selection. */
		try{
			Connection myConn = DriverManager.getConnection("jdbc:derby:test;create=true");	 
			Statement myStmt = myConn.createStatement();
			int dishNumber;
			ResultSet dishesRs = myStmt.executeQuery("Select USER_ID, dishID from savecatering where USER_ID = " + LoginClass.loginUserID);
			while(dishesRs.next()){	// If user has saved the selection of food items then retrieve those dishId's and check those particular boxes.
				dishNumber = dishesRs.getInt("dishID");
				checkboxes.get(dishNumber-1).setSelected(true);
			}
			// Also retrieve rough number of guests and previously calculated total catering amount for remembering the user to make any changes accordingly. 
			ResultSet cateringRs = myStmt.executeQuery("Select TotalGuests, CateringCost from savedatatable where USER_ID = " + LoginClass.loginUserID);
			if(cateringRs.next()){
				personsTextField.setText(cateringRs.getInt("TotalGuests") + "");
				amountLabel.setText("Total catering amount: $" + cateringRs.getInt("CateringCost"));
			}
			else{
				personsTextField.setText(people + "");
				amountLabel.setText("Total catering amount: $" + cateringCost);
			}
			myConn.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}

		/** calculateButton should calculate the total catering cost for the user, depending on the food items selected and number of guests coming for the wedding. */
		JButton calculateButton = new JButton("Calculate Catering Amount"); 
		calculateButton.setBounds(10, 515, 188, 30);
		cateringPanel.add(calculateButton);
		calculateButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0){
				buttonPressed = true;
				int i, totalAmount = 0, dishesAmount = 0, numberOfGuests;
				boolean itemSelected = false;
				for(i = 0; i < 30; ++i){
					if(checkboxes.get(i).isSelected()){
						itemSelected = true;
						break;
					}
					else
						itemSelected = false;
				}
				if(itemSelected == false)		// Validation 1: If none food items are selected then tell the user to select some food items. 
					JOptionPane.showMessageDialog(cateringPanel, "Select some food items.", "Catering - Food Items", JOptionPane.INFORMATION_MESSAGE);
				else if(personsTextField.getText().length() == 0 || (Integer.parseInt(personsTextField.getText())<50)) // Validation 2: If number of guests is not entered or is less than 50 then tell user accordingly to correct the entry.
					JOptionPane.showMessageDialog(cateringPanel, "Enter valid number of guests (minimum 50).", "Catering - Guests", JOptionPane.INFORMATION_MESSAGE);
				else{
					people = Integer.parseInt(personsTextField.getText());
					try{
						Connection myConn = DriverManager.getConnection("jdbc:derby:test;create=true");	
						Statement myStmt = myConn.createStatement();
						numberOfGuests = Integer.parseInt(personsTextField.getText());
						for(i = 0; i < 30; ++i){	// Retrieve the cost of each dish which has been selected by the user.
							if(checkboxes.get(i).isSelected()){
								ResultSet amountRs = myStmt.executeQuery("Select dishCost from cateringtable where dishID = " + (i+1));
								if(amountRs.next()){
									dishesAmount = dishesAmount + amountRs.getInt("dishCost");	// Add all the dishes cost and store it in dishesAmount.
								}
							}
						}
						totalAmount = numberOfGuests * dishesAmount;	// Now calculate total catering estimation by multiplying dishesAmount by number of guests.
						amountLabel.setText("Total catering amount: $" + totalAmount);	// display this total Amount in the amountLabel.
						cateringCost = totalAmount;
						myConn.close();
					}
					catch(Exception e1){
						e1.printStackTrace();
					}	
				}
			}
		});
		
		// This button will add the total catering cost to the planner window's total wedding's estimation.
		JButton addToPlannerButton = new JButton("Save and add to Planner");
		addToPlannerButton.setBounds(213, 515, 185, 30);
		cateringPanel.add(addToPlannerButton);
		addToPlannerButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0){
				if(buttonPressed){
					int i = 0;
					try{
						Connection myConn = DriverManager.getConnection("jdbc:derby:test;create=true");			 
						Statement myStmt = myConn.createStatement();
						
						// Save all the dishes selection of the user in the savecateringtable
						ResultSet checkRs = myStmt.executeQuery("Select USER_ID from savecatering where USER_ID = " + LoginClass.loginUserID);
						if(checkRs.next()){
							myStmt.executeUpdate("Delete from savecatering where USER_ID = " + LoginClass.loginUserID);
							for(i = 0; i < 30; ++i){
								if(checkboxes.get(i).isSelected())
									myStmt.executeUpdate("Insert into savecatering(USER_ID, dishID) Values (" + LoginClass.loginUserID + ", " + (i+1) + ")");
							}
						}
						else{
							for(i = 0; i < 30; ++i){
								if(checkboxes.get(i).isSelected())
									myStmt.executeUpdate("Insert into savecatering(USER_ID, dishID) Values(" + LoginClass.loginUserID + ", " + (i+1) + ")");
							}
						}
						
						// Also save the number of guests and total catering amount 
						ResultSet checkDataRs = myStmt.executeQuery("Select USER_ID from savedatatable where USER_ID = " + LoginClass.loginUserID);
						if(checkDataRs.next())
							myStmt.executeUpdate("Update savedatatable set TotalGuests = " + personsTextField.getText() + ", CateringCost = " + cateringCost + " where USER_ID = " + LoginClass.loginUserID);
						else
							people = Integer.parseInt(personsTextField.getText());
						myConn.close();
					}
					catch(Exception e){
						e.printStackTrace();
					}
					
					totalEstimationLabel.setText("");
					cateringFrame.setVisible(false);
					plannerWindow.setVisible(true);
					buttonPressed = false;
				}
				else
					JOptionPane.showMessageDialog(cateringPanel, "First calculate the catering amount.", "Catering - Estimation", JOptionPane.WARNING_MESSAGE);
			}
		});
		
		// This button will simply close the catering window and open the planner window without saving any changes on the catering page.
		JButton cancelButton = new JButton("Cancel");
		cancelButton.setBounds(410, 515, 185, 30);
		cateringPanel.add(cancelButton);
		cancelButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0){
				cateringFrame.setVisible(false);
				plannerWindow.setVisible(true);
				buttonPressed = false;
			}
		});
		cateringFrame.add(cateringPanel);
	}
}