package comp231.master;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class InventoryPage extends JFrame {

	private JTextField searchBar;
	private JButton searchButton;
	private JTextArea resultArea;

	public InventoryPage() {
		// Set up the JFrame for Inventory Management
		setTitle("Inventory Management");
		setSize(800, 600);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		InventoryUpdatePanel updatePanel = new InventoryUpdatePanel();

		// Initialize search bar UI
		searchBar = new JTextField();
		searchButton = new JButton("Search");

		// Set layout manager for Inventory Management frame
		setLayout(new BorderLayout());

		// Create panel for search bar and button
		JTextField searchBar = new JTextField();
		searchBar.setPreferredSize(new Dimension(300, 30));

		JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
		searchPanel.add(searchBar);
		searchPanel.add(searchButton);

		// Create result display area
		resultArea = new JTextArea();
		resultArea.setEditable(false);

		// Create buttons for viewing, updating, and discarding inventory
		JButton viewInventoryButton = new JButton("View Inventory Levels");
		JButton updateInventoryButton = new JButton("Update Inventory");
		JButton discardInventoryButton = new JButton("Discard Inventory");
		JButton viewDisposalButton = new JButton("View Disposal Records");

		
		// Create panel for inventory actions
		JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
		actionPanel.add(viewInventoryButton);
		actionPanel.add(updateInventoryButton);
		actionPanel.add(discardInventoryButton);
		actionPanel.add(viewDisposalButton);

		// Add components to the frame
		add(searchPanel, BorderLayout.NORTH);
		add(actionPanel, BorderLayout.SOUTH);
		add(new JScrollPane(resultArea), BorderLayout.CENTER);

		// Add action listeners for the buttons
		searchButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String query = searchBar.getText();
				performSearch(query);
			}
		});

		viewInventoryButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Placeholder for viewing inventory levels
				resultArea.setText("Viewing Inventory Levels...");
			}
		});

		updateInventoryButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Remove the current panel (if any)
				getContentPane().removeAll();
				revalidate();
				repaint();

				// Create a new InventoryUpdatePanel
				InventoryUpdatePanel updatePanel = new InventoryUpdatePanel();

				// Add the update panel to the frame
				add(updatePanel, BorderLayout.CENTER);

				// Repaint the frame to reflect the changes
				revalidate();
				repaint();
			}
		});

		discardInventoryButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Create a new dialog to input item ID and quantity
				JPanel inputPanel = new JPanel(new GridLayout(2, 2, 10, 5));
				JTextField itemIdField = new JTextField();
				JTextField quantityField = new JTextField();

				inputPanel.add(new JLabel("Item ID:"));
				inputPanel.add(itemIdField);
				inputPanel.add(new JLabel("Quantity:"));
				inputPanel.add(quantityField);

				int option = JOptionPane.showConfirmDialog(null, inputPanel, "Discard Inventory",
						JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

				if (option == JOptionPane.OK_OPTION) {
					// User clicked OK, process the input
					try {
						int itemId = Integer.parseInt(itemIdField.getText());
						int quantity = Integer.parseInt(quantityField.getText());
						discardInventory(itemId, quantity);
					} catch (NumberFormatException ex) {
						// Handle invalid input (non-integer values)
						JOptionPane.showMessageDialog(null, "Invalid input. Please enter valid numbers.", "Error",
								JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		
		viewDisposalButton.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		        viewDisposalRecords();
		    }
		});

	}
	
	

	private void discardInventory(int itemId, int quantity) {

	    // SQL query to update the quantity in the Inventory table
	    String updateSql = "UPDATE Inventory SET Quantity = Quantity - ? WHERE ItemID = ?";
	    // SQL query to fetch the details of the discarded item
	    String selectSql = "SELECT * FROM Inventory WHERE ItemID = ?";

	    try (Connection connection = DatabaseManager.getConnection();
	         PreparedStatement updateStatement = connection.prepareStatement(updateSql);
	         PreparedStatement selectStatement = connection.prepareStatement(selectSql)) {

		// Check current inventory level before attempting to discard
	        selectStatement.setInt(1, itemId);
	        ResultSet resultSet = selectStatement.executeQuery();
	        if (resultSet.next()) {
	            int currentQuantity = resultSet.getInt("Quantity");
	            if (quantity > currentQuantity) {
	                // If discard quantity is greater than current stock, show an error and return
	                JOptionPane.showMessageDialog(null, "Cannot discard more items than are in stock.", "Error",
	                        JOptionPane.ERROR_MESSAGE);
	                return; // Prevents the rest of the code from running
	            }
	        } else {
	            // If item is not found, show an error and return
	            JOptionPane.showMessageDialog(null, "Item not found. Please enter a valid Item ID.", "Error",
	                    JOptionPane.ERROR_MESSAGE);
	            return;
	        } 

	        // Set parameters for the update statement
	        updateStatement.setInt(1, quantity);
	        updateStatement.setInt(2, itemId);

	        // Execute the update query
	        int rowsAffected = updateStatement.executeUpdate();

	        if (rowsAffected > 0) {
	            // The update was successful
	            // Fetch the details of the discarded item
	            selectStatement.setInt(1, itemId);
		    resultSet = selectStatement.executeQuery();
	            if (resultSet.next()) {
	                // Display the details in the resultArea
	                resultArea.append("\n\nDetails of Inventory after discard:\n");
	                resultArea.append(String.format("%-10s%-10s%-12s%-10s%-15s%-15s%-50s\n", "Item ID", "Item", "Quantity",
	                        "Price", "Supplier ID", "Expiry Date", "Product Details"));

	                int discardedItemId = resultSet.getInt("ItemID");
	                String itemName = resultSet.getString("ItemName");
	                int discardedQuantity = resultSet.getInt("Quantity");
	                double price = resultSet.getDouble("Price");
	                int supplierId = resultSet.getInt("SupplierID");
	                String expiryDate = resultSet.getString("ExpiryDate");
	                String productDetails = resultSet.getString("ProductDetails");

	                resultArea.append(String.format("%-10d%-20s%-10d%-10.2f%-15d%-15s%-50s\n", discardedItemId, itemName,
	                        discardedQuantity, price, supplierId, expiryDate, productDetails));
	                
	                // Display success message
	                String successMessage = "Inventory discarded successfully. Discarded Quantity: " + quantity;
	                JOptionPane.showMessageDialog(null, successMessage, "Success", JOptionPane.INFORMATION_MESSAGE);
	            } else {
	                // No rows were affected, meaning the item with the specified ItemID was not found
	                JOptionPane.showMessageDialog(null, "Item not found. Please enter a valid Item ID.", "Error",
	                        JOptionPane.ERROR_MESSAGE);
	            }
	        } else {
	            // No rows were affected, meaning the item with the specified ItemID was not found
	            JOptionPane.showMessageDialog(null, "Item not found. Please enter a valid Item ID.", "Error",
	                    JOptionPane.ERROR_MESSAGE);
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	        JOptionPane.showMessageDialog(null, "Error discarding inventory: " + e.getMessage(), "Error",
	                JOptionPane.ERROR_MESSAGE);
	    }
	}



	private void performSearch(String query) {
		// SQL query
		String sql = "SELECT * FROM Inventory WHERE ItemName LIKE ?";

		try (Connection connection = DatabaseManager.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

			// Set parameter for the prepared statement
			preparedStatement.setString(1, "%" + query + "%");

			// Execute the query
			ResultSet resultSet = preparedStatement.executeQuery();

			// Process the result set and update the UI
			StringBuilder searchResult = new StringBuilder("Search results:\n");
			searchResult.append(String.format("%-10s%-10s%-12s%-10s%-15s%-15s%-50s\n", "Item ID", "Item", "Quantity",
					"Price", "Supplier ID", "Expiry Date", "Product Details"));

			boolean resultsFound = false;

			while (resultSet.next()) {
				resultsFound = true;
				int itemId = resultSet.getInt("ItemID");
				String itemName = resultSet.getString("ItemName");
				int quantity = resultSet.getInt("Quantity");
				double price = resultSet.getDouble("Price");
				int supplierId = resultSet.getInt("SupplierID");
				String expiryDate = resultSet.getString("ExpiryDate");
				String productDetails = resultSet.getString("ProductDetails");

				searchResult.append(String.format("%-10d%-20s%-10d%-10.2f%-15d%-15s%-50s\n", itemId, itemName, quantity,
						price, supplierId, expiryDate, productDetails));
			}

			if (!resultsFound) {
				searchResult.append(String.format("No results found for: %s", query));
			}

			resultArea.setText(searchResult.toString());

		} catch (SQLException e) {
			e.printStackTrace();
			// Handle exceptions (log or display an error message)
		}
	}
	
	private void viewDisposalRecords() {
	    // SQL query to fetch disposal records
	    String disposalSql = "SELECT * FROM Disposal";

	    try (Connection connection = DatabaseManager.getConnection();
	         PreparedStatement disposalStatement = connection.prepareStatement(disposalSql)) {

	        // Execute the query
	        ResultSet disposalResultSet = disposalStatement.executeQuery();

	        // Process the result set and update the UI
	        StringBuilder disposalResult = new StringBuilder("Disposal Records:\n");
	        disposalResult.append(String.format("%-10s%-20s%-15s%-20s\n", "Disposal ID", "Item Name", "Quantity Disposed",
	                "Disposal Reason"));

	        while (disposalResultSet.next()) {
	            int disposalId = disposalResultSet.getInt("DisposalID");
	            String itemName = disposalResultSet.getString("ItemName");
	            int itemId = disposalResultSet.getInt("ItemID");
	            int quantityDisposed = disposalResultSet.getInt("QuantityDisposed");
	            String disposalReason = disposalResultSet.getString("DisposalReason");

	            disposalResult.append(String.format("%-10d%-20s%-15d%-20s\n", disposalId, itemName, quantityDisposed,
	                    disposalReason));
	        }

	        resultArea.setText(disposalResult.toString());

	    } catch (SQLException ex) {
	        ex.printStackTrace();
	        // Handle exceptions (log or display an error message)
	        JOptionPane.showMessageDialog(null, "Error retrieving disposal records: " + ex.getMessage(), "Error",
	                JOptionPane.ERROR_MESSAGE);
	    }
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new InventoryPage().setVisible(true);
			}
		});
	}
}
