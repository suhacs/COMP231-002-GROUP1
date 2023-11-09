package comp231.master;

import javax.swing.*;
import java.awt.*;

public class SalesLookupPage extends JFrame {

    public SalesLookupPage() {
        setTitle("Sales Lookup");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Add components to the Sales Lookup page
        // Customize as needed
        JLabel label = new JLabel("Sales Lookup Page");
        label.setHorizontalAlignment(JLabel.CENTER);

        getContentPane().add(label, BorderLayout.CENTER);
    }
}

