package comp231.master;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderManagementPage extends JFrame {

	JTextField itemNameField;
	JTextField quantityField;
	JTextField priceField;
	JTextField supplierIdField;
	JTextField expiryDateField;
	JTextField productDetailsField;
	JTextField optimumLevelField; // Added for OptimumLevel input
	private JButton placeOrderButton;
	private JButton displayOrderTableButton; // Added button to display order table

	public OrderManagementPage() {
		// Set up the JFrame for Order Management
		setTitle("Order Management");
		setSize(400, 300);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		// Initialize UI components
		itemNameField = new JTextField();
		quantityField = new JTextField();
		priceField = new JTextField();
		supplierIdField = new JTextField();
		expiryDateField = new JTextField();
		productDetailsField = new JTextField();
		optimumLevelField = new JTextField();
		placeOrderButton = new JButton("Place Order");
		displayOrderTableButton = new JButton("Display Order Record");

		// Set layout manager
		setLayout(new GridLayout(9, 2));

		// Add components to the layout
		add(new JLabel("Item Name:"));
		add(itemNameField);
		add(new JLabel("Quantity:"));
		add(quantityField);
		add(new JLabel("Price:"));
		add(priceField);
		add(new JLabel("Supplier ID:"));
		add(supplierIdField);
		add(new JLabel("Expiry Date (YYYY-MM-DD):"));
		add(expiryDateField);
		add(new JLabel("Product Details:"));
		add(productDetailsField);
		add(new JLabel("Optimum Level:")); // Added for OptimumLevel input
		add(optimumLevelField);
		add(new JLabel()); // Empty label for spacing
		add(placeOrderButton);
		add(new JLabel()); // Empty label for spacing
		add(displayOrderTableButton); // Added button to display order table

		// Add action listener for the "Place Order" button
		placeOrderButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					placeOrder();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		});

		// Add action listener for the "Display Order Table" button
		displayOrderTableButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				displayOrderTable();
			}
		});
	}

	private void placeOrder() throws SQLException {
		String itemName = itemNameField.getText();
	    int quantity = Integer.parseInt(quantityField.getText());
	    double price = Double.parseDouble(priceField.getText());
	    int supplierId = Integer.parseInt(supplierIdField.getText());
	    String expiryDate = expiryDateField.getText();
	    String productDetails = productDetailsField.getText();
	    int optimumLevel = Integer.parseInt(optimumLevelField.getText());

	    OrderManager orderManager = new OrderManager();
	    OrderManager.OrderResult result;

	    try {
	        result = orderManager.placeOrder(itemName, quantity, price, supplierId, expiryDate, productDetails, optimumLevel);
	        if (result.success) {
	            JOptionPane.showMessageDialog(this, "Order placed successfully! Order ID: " + result.orderId, "Success", JOptionPane.INFORMATION_MESSAGE);
	        } else {
	            JOptionPane.showMessageDialog(this, "Failed to place order.", "Error", JOptionPane.ERROR_MESSAGE);
	        }
	    } catch (IllegalArgumentException | SQLException e) {
	        JOptionPane.showMessageDialog(this, "Error placing order: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
	    }
	}

	private void displayOrderTable() {
		// SQL query to fetch order records
		String orderSql = "SELECT * FROM `Order`";

		try (Connection connection = DatabaseManager.getConnection();
				PreparedStatement orderStatement = connection.prepareStatement(orderSql)) {

			// Execute the query
			ResultSet orderResultSet = orderStatement.executeQuery();

			// Process the result set and update the UI
			StringBuilder orderResult = new StringBuilder("Order Table:\n");
			orderResult.append(String.format("%-10s%-20s%-15s%-10s%-20s%-20s\n", "Order ID", "Item Name", "Quantity",
					"Price", "Supplier ID", "Expiry Date"));

			while (orderResultSet.next()) {
				int orderId = orderResultSet.getInt("OrderID");
				String itemName = orderResultSet.getString("ItemName");
				int quantity = orderResultSet.getInt("Quantity");
				double price = orderResultSet.getDouble("Price");
				int supplierId = orderResultSet.getInt("SupplierID");
				String expiryDate = orderResultSet.getString("ExpiryDate");

				orderResult.append(String.format("%-10d%-20s%-15d%-10.2f%-20d%-20s\n", orderId, itemName, quantity,
						price, supplierId, expiryDate));
			}

			// Show the order table using JOptionPane
			JTextArea orderTableArea = new JTextArea();
			orderTableArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
			orderTableArea.setEditable(false);
			orderTableArea.setText(orderResult.toString());

			JScrollPane scrollPane = new JScrollPane(orderTableArea);
			scrollPane.setPreferredSize(new Dimension(600, 400));

			JOptionPane.showMessageDialog(this, scrollPane, "Order Table", JOptionPane.PLAIN_MESSAGE);

		} catch (SQLException ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(this, "Error retrieving order records: " + ex.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}
}
