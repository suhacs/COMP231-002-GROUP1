package comp231.master;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class WastageLookUpPage extends JFrame {

	private JTextArea disposalTableArea;

	public WastageLookUpPage() {
		setTitle("Disposal Table");
		setSize(800, 600);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		// Initialize UI components
		disposalTableArea = new JTextArea();
		disposalTableArea.setEditable(false);

		// Set monospaced font for better alignment
		disposalTableArea.setFont(new Font("Monospaced", Font.PLAIN, 12));

		// Clear the existing data in the disposalTableArea
		disposalTableArea.setText("");

		// Set layout manager
		setLayout(new BorderLayout());

		// Add the disposal table area to the frame
		add(new JScrollPane(disposalTableArea), BorderLayout.CENTER);

		// Display the disposal table
		displayDisposalTable();
	}

	private void displayDisposalTable() {
		// SQL query to fetch disposal records
		String disposalSql = "SELECT * FROM Disposal";

		try (Connection connection = DatabaseManager.getConnection();
				PreparedStatement disposalStatement = connection.prepareStatement(disposalSql)) {

			// Execute the query
			ResultSet disposalResultSet = disposalStatement.executeQuery();

			// Process the result set and update the UI
			StringBuilder disposalResult = new StringBuilder("Disposal Table:\n");
			disposalResult.append(String.format("%-10s%-20s%-15s%-20s\n", "Disposal ID", "Item Name",
					"Quantity Disposed", "Disposal Date"));
		    // Iterate through the sales result set
			while (disposalResultSet.next()) {
		        // Retrieve information for each sale
				int disposalId = disposalResultSet.getInt("DisposalID");
				String itemName = disposalResultSet.getString("ItemName");
				int quantityDisposed = disposalResultSet.getInt("QuantityDisposed");
				String disposalDate = disposalResultSet.getString("DisposalDate");
		        // Format the sale information and append to the sales result
				disposalResult.append(
						String.format("%-10d%-20s%-15d%-20s\n", disposalId, itemName, quantityDisposed, disposalDate));
			}
		    // Set the text of the sales lookup area with the formatted sales result
			disposalTableArea.setText(disposalResult.toString());
		} catch (SQLException ex) {
			// Handle exceptions (log or display an error message)
			ex.printStackTrace();
			JOptionPane.showMessageDialog(this, "Error retrieving disposal records: " + ex.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}
}
