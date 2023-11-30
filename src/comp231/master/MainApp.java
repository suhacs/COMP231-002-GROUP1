package comp231.master;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MainApp extends JFrame {

	public MainApp() {

		// Set up the JFrame
		setTitle("Inventory Management App");
		setSize(600, 400);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Create navigation links
		JButton homeButton = new JButton("Home");
		JButton inventoryButton = new JButton("Inventory Management");
		JButton orderButton = new JButton("Order Management");
		JButton salesButton = new JButton("Sales Lookup");
		JButton wastageButton = new JButton("Wastage Lookup");

		// Set layout manager
		setLayout(new GridLayout(5, 1));

		// Add buttons to the layout
		add(homeButton);
		add(inventoryButton);
		add(orderButton);
		add(salesButton);
		add(wastageButton);

		// Add action listeners to handle button clicks
		homeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(MainApp.this, "You are on the Home page.");
			}
		});

		inventoryButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				openInventoryManagement();
			}
		});

		orderButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				openOrderManagement();
			}
		});

		salesButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				openSalesLookup();
			}
		});

		wastageButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				openWastageLookup();
			}
		});

	}

	private void openInventoryManagement() {
		// Create and display the InventoryPage JFrame
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new InventoryPage().setVisible(true);
			}
		});
	}

	private void openOrderManagement() {
		// Create and display the OrderManagementPage JFrame
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new OrderManagementPage().setVisible(true);
			}
		});
	}

	private void openSalesLookup() {
		// Create and display the SalesLookupPage JFrame
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				SalesLookupPage salesLookupPage = new SalesLookupPage();
				salesLookupPage.displaySalesRecords();
				salesLookupPage.setVisible(true);
			}
		});
	}

	private void openWastageLookup() {
		// Create and display the WastageLookUpPage JFrame
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new WastageLookUpPage().setVisible(true);
			}
		});
	}
	public static void main(String[] args) {
	    // Attempt to connect to the database and display the JFrame
	    try {
	        // Register the MySQL JDBC driver class
	        Class.forName("com.mysql.cj.jdbc.Driver");

	        // Establish a connection to the database
	        Connection connection = DatabaseManager.getConnection();
	        JOptionPane.showMessageDialog(null, "Connected to the database successfully!");

	        // Create and display the main application JFrame
	        SwingUtilities.invokeLater(new Runnable() {
	            @Override
	            public void run() {
	                new MainApp().setVisible(true);
	            }
	        });
	    } catch (ClassNotFoundException ex) {
	        // Handle the case where the MySQL Connector/J library is not found
	        ex.printStackTrace();
	        JOptionPane.showMessageDialog(null, "MySQL Connector/J library not found!");
	    } catch (SQLException ex) {
	        // Handle the case where there is a failure to connect to the database
	        ex.printStackTrace();
	        JOptionPane.showMessageDialog(null, "Failed to connect to the database!");
	    }
	}
}