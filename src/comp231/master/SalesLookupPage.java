package comp231.master;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SalesLookupPage extends JFrame {

	private JTextArea salesLookupArea;

	public SalesLookupPage() {
		// Set up the JFrame for Sales Lookup
		setTitle("Sales Lookup");
		setSize(800, 600);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		// Initialize UI components
		salesLookupArea = new JTextArea();
		salesLookupArea.setEditable(false);

		// Set layout manager
		setLayout(new BorderLayout());

		// Add the sales lookup area to the frame
		add(new JScrollPane(salesLookupArea), BorderLayout.CENTER);

		// Set monospaced font for better alignment
		salesLookupArea.setFont(new Font("Monospaced", Font.PLAIN, 12));

		// Clear the existing data in the result area
		salesLookupArea.setText("");
	}

	public void displaySalesRecords() {

		// SQL query to fetch sales records
		String salesSql = "SELECT * FROM Sales";

		try (Connection connection = DatabaseManager.getConnection();
				PreparedStatement salesStatement = connection.prepareStatement(salesSql)) {

			// Execute the query
			ResultSet salesResultSet = salesStatement.executeQuery();

			// Process the result set and update the UI
			StringBuilder salesResult = new StringBuilder("Sales Records:\n");
			salesResult.append(
					String.format("%-10s%-20s%-15s%-10s\n", "Sale ID", "Item Name", "Quantity Sold", "Sale Date"));

			while (salesResultSet.next()) {
				int saleId = salesResultSet.getInt("SaleID");
				String itemName = salesResultSet.getString("ItemName");
				int quantitySold = salesResultSet.getInt("QuantitySold");
				String saleDate = salesResultSet.getString("SaleDate");

				salesResult.append(String.format("%-10d%-20s%-15d%-10s\n", saleId, itemName, quantitySold, saleDate));
			}

			salesLookupArea.setText(salesResult.toString());

		} catch (SQLException ex) {
			ex.printStackTrace();
			// Handle exceptions (log or display an error message)
			JOptionPane.showMessageDialog(null, "Error retrieving sales records: " + ex.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

}
