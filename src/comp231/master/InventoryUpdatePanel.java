package comp231.master;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

        // Set layout manager
        setLayout(new GridLayout(4, 2));

        // Add components to the panel
        add(new JLabel("Item Name:"));
        add(itemNameField);
        add(new JLabel("Quantity:"));
        add(quantityField);
        add(new JLabel("Price:"));
        add(priceField);
        add(updateButton);
        

        // Add action listener for update button
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String itemName = itemNameField.getText();
                int newQuantity = Integer.parseInt(quantityField.getText());
                double newPrice = Double.parseDouble(priceField.getText());

                // Validate inputs (add more validation as needed)
                if (newQuantity < 0 || newPrice < 0) {
                    JOptionPane.showMessageDialog(InventoryUpdatePanel.this, "Quantity and price must be non-negative.");
                    return;
                }

                // Notify listener (MainApp) about update request
                if (listener != null) {
                    listener.onUpdateRequest(itemName, newQuantity, newPrice);
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
}
