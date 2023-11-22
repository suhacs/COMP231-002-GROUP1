package comp231.master;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class InventoryUpdatePanel extends JPanel {
    private JTextField itemNameField;
    private JTextField quantityField;
    private JTextField priceField;
    private JButton updateButton;
    private JButton cancelButton;

    public InventoryUpdatePanel() {
        // Initialize UI components
        itemNameField = new JTextField();
        quantityField = new JTextField();
        priceField = new JTextField();
        updateButton = new JButton("Update Inventory");
        cancelButton = new JButton("Cancel");

        // Set layout manager to GridBagLayout for better flexibility
        setLayout(new GridBagLayout());

        // Use GridBagConstraints for better component placement
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Add some padding

        // Increase font size for labels
        Font labelFont = new Font("Arial", Font.PLAIN, 16);

        // Add components to the panel with GridBagConstraints
        gbc.anchor = GridBagConstraints.WEST;

        JLabel itemNameLabel = new JLabel("Item Name:");
        itemNameLabel.setFont(labelFont);
        add(itemNameLabel, gbc);

        gbc.gridx = 1;
        itemNameField.setPreferredSize(new Dimension(200, 30)); // Set preferred size for text field
        add(itemNameField, gbc);

        gbc.gridy = 1;
        gbc.gridx = 0;
        JLabel quantityLabel = new JLabel("Quantity:");
        quantityLabel.setFont(labelFont);
        add(quantityLabel, gbc);

        gbc.gridx = 1;
        quantityField.setPreferredSize(new Dimension(200, 30)); // Set preferred size for text field
        add(quantityField, gbc);

        gbc.gridy = 2;
        gbc.gridx = 0;
        JLabel priceLabel = new JLabel("Price:");
        priceLabel.setFont(labelFont);
        add(priceLabel, gbc);

        gbc.gridx = 1;
        priceField.setPreferredSize(new Dimension(200, 30)); // Set preferred size for text field
        add(priceField, gbc);

        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.gridwidth = 2; // Span two columns
        add(updateButton, gbc);

        gbc.gridy = 4;
        add(cancelButton, gbc);

        // Add action listener for cancel button
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Add any necessary logic for cancel button
                Window window = SwingUtilities.getWindowAncestor(InventoryUpdatePanel.this);
                if (window != null) {
                    window.dispose();
                }
            }
        });

        // Add action listener for update button
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String itemName = itemNameField.getText();
                    int newQuantity = Integer.parseInt(quantityField.getText());
                    double newPrice = Double.parseDouble(priceField.getText());

                    // Validate inputs (add more validation as needed)
                    if (itemName.isEmpty() || newQuantity < 0 || newPrice < 0) {
                        JOptionPane.showMessageDialog(InventoryUpdatePanel.this, "Please enter valid values for all fields.");
                        return;
                    }

                    // Perform SQL function to update inventory
                    updateInventory(itemName, newQuantity, newPrice);

                    // Notify listener (MainApp) about update request
                    if (listener != null) {
                        listener.onUpdateRequest(itemName, newQuantity, newPrice);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(InventoryUpdatePanel.this, "Please enter valid numeric values for quantity and price.");
                }
            }
        });
    }

    // Listener interface for update events
    public interface UpdateListener {
        void onUpdateRequest(String itemName, int newQuantity, double newPrice);
    }

    private UpdateListener listener;

    public void setUpdateListener(UpdateListener listener) {
        this.listener = listener;
    }

    // Method to perform the SQL function to update inventory
    private void updateInventory(String itemName, int newQuantity, double newPrice) {
        try {
        	Connection connection = DatabaseManager.getConnection();

            // SQL update statement
            String sql = "UPDATE Inventory SET Quantity = ?, Price = ? WHERE ItemName = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, newQuantity);
            statement.setDouble(2, newPrice);
            statement.setString(3, itemName);


            // Execute the update
            int rowsAffected = statement.executeUpdate();

            // Close resources
            statement.close();
            connection.close();

            // Display a success message if the update is successful
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(InventoryUpdatePanel.this, "Inventory updated successfully.");
            } else {
                JOptionPane.showMessageDialog(InventoryUpdatePanel.this, "No records updated. Please check the item name.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(InventoryUpdatePanel.this, "Error updating inventory: " + ex.getMessage());
        }
    }
}

