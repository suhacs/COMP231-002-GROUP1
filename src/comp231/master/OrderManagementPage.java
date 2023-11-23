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

	private JTextField itemNameField;
	private JTextField quantityField;
	private JTextField priceField;
	private JTextField supplierIdField;
	private JTextField expiryDateField;
	private JTextField productDetailsField;
	private JTextField optimumLevelField; // Added for OptimumLevel input
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
		// Parse input values
		String itemName = itemNameField.getText();
		int quantity = Integer.parseInt(quantityField.getText());
		double price = Double.parseDouble(priceField.getText());
		int supplierId = Integer.parseInt(supplierIdField.getText());
		String expiryDate = expiryDateField.getText();
		String productDetails = productDetailsField.getText();
		int optimumLevel = Integer.parseInt(optimumLevelField.getText()); // Added for OptimumLevel input

		// Validate non-negative quantity, price, and optimum level
		if (quantity < 0 || price < 0 || optimumLevel < 0) {
			JOptionPane.showMessageDialog(this, "Quantity, price, and optimum level must be non-negative.", "Error",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		// SQL query to insert order into the Order table
		String orderSql = "INSERT INTO `Order` (ItemName, Quantity, Price, SupplierID, ExpiryDate, ProductDetails) "
				+ "VALUES (?, ?, ?, ?, ?, ?)";

		try (Connection connection = DatabaseManager.getConnection();
				PreparedStatement orderStatement = connection.prepareStatement(orderSql,
						PreparedStatement.RETURN_GENERATED_KEYS)) {

			// Set parameters for the order statement
			orderStatement.setString(1, itemName);
			orderStatement.setInt(2, quantity);
			orderStatement.setDouble(3, price);
			orderStatement.setInt(4, supplierId);
			orderStatement.setString(5, expiryDate);
			orderStatement.setString(6, productDetails);

			// Execute the order insertion query
			int rowsAffected = orderStatement.executeUpdate();

			if (rowsAffected > 0) {
				// Get the generated order ID
				ResultSet generatedKeys = orderStatement.getGeneratedKeys();
				int orderId = -1;
				if (generatedKeys.next()) {
					orderId = generatedKeys.getInt(1);
				}

				// Insert the details into the Inventory table as a new row
				String inventorySql = "INSERT INTO Inventory (ItemName, Quantity, OptimumLevel, Price, SupplierID, ExpiryDate, ProductDetails) "
						+ "VALUES (?, ?, ?, ?, ?, ?, ?)";

				try (PreparedStatement inventoryStatement = connection.prepareStatement(inventorySql)) {
					// Set parameters for the inventory statement
					inventoryStatement.setString(1, itemName);
					inventoryStatement.setInt(2, quantity);
					inventoryStatement.setInt(3, optimumLevel);
					inventoryStatement.setDouble(4, price);
					inventoryStatement.setInt(5, supplierId);
					inventoryStatement.setString(6, expiryDate);
					inventoryStatement.setString(7, productDetails);

					// Execute the inventory insertion query
					inventoryStatement.executeUpdate();

					// Display success message with a button to view order table
					String successMessage = "Order placed successfully! Order ID: " + orderId;
					int option = JOptionPane.showOptionDialog(this, successMessage, "Success",
							JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, new Object[] {}, null);

					// If the user clicks the "View Order Table" button, display the order table
					if (option == 0) {
						displayOrderTable();
					}
				}
			} else {
				JOptionPane.showMessageDialog(this, "Failed to place order.", "Error", JOptionPane.ERROR_MESSAGE);
			}

		} catch (SQLException ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(this, "Error placing order: " + ex.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
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
            orderResult.append(
                    String.format("%-10s%-20s%-15s%-10s%-20s%-20s\n",
                            "Order ID", "Item Name", "Quantity", "Price", "Supplier ID", "Expiry Date"));

            while (orderResultSet.next()) {
                int orderId = orderResultSet.getInt("OrderID");
                String itemName = orderResultSet.getString("ItemName");
                int quantity = orderResultSet.getInt("Quantity");
                double price = orderResultSet.getDouble("Price");
                int supplierId = orderResultSet.getInt("SupplierID");
                String expiryDate = orderResultSet.getString("ExpiryDate");

                orderResult.append(String.format("%-10d%-20s%-15d%-10.2f%-20d%-20s\n",
                        orderId, itemName, quantity, price, supplierId, expiryDate));
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
