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

        // Create panel for inventory actions
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        actionPanel.add(viewInventoryButton);
        actionPanel.add(updateInventoryButton);
        actionPanel.add(discardInventoryButton);

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
                // Placeholder for discarding inventory
                resultArea.setText("Discarding Inventory...");
            }
        });
    }

    private void performSearch(String query) {
        // JDBC URL, username, and password of MySQL server
        String url = "jdbc:mysql://localhost:3306/comp231";
        String user = "root";
        String password = "12345678";

     // SQL query
        String sql = "SELECT * FROM Inventory WHERE ItemName LIKE ?";

        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            // Set parameter for the prepared statement
            preparedStatement.setString(1, "%" + query + "%");

            // Execute the query
            ResultSet resultSet = preparedStatement.executeQuery();

            // Process the result set and update the UI
            StringBuilder searchResult = new StringBuilder("Search results:\n");
            searchResult.append(String.format("%-10s%-10s%-12s%-10s%-15s%-15s%-50s\n",
                    "Item ID", "Item", "Quantity", "Price", "Supplier ID", "Expiry Date", "Product Details"));

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

                searchResult.append(String.format("%-10d%-20s%-10d%-10.2f%-15d%-15s%-50s\n",
                        itemId, itemName, quantity, price, supplierId, expiryDate, productDetails));
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new InventoryPage().setVisible(true);
            }
        });
    }
}
