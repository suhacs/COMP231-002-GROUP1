package comp231.master;

import javax.swing.*;
import java.awt.*;

public class OrderManagementPage extends JFrame {

    public OrderManagementPage() {
        setTitle("Order Management");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Dispose on close to only close this window

        // Add components to the Order Management page
        // You can customize this based on your requirements
        JLabel label = new JLabel("Order Management Page");
        label.setHorizontalAlignment(JLabel.CENTER);

        getContentPane().add(label, BorderLayout.CENTER);
    }
}
