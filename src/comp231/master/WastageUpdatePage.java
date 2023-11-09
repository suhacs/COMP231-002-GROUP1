package comp231.master;

import javax.swing.*;
import java.awt.*;

public class WastageUpdatePage extends JFrame {

    public WastageUpdatePage() {
        setTitle("Wastage Update");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Add components to the Wastage Update page
        // Customize as needed
        JLabel label = new JLabel("Wastage Update Page");
        label.setHorizontalAlignment(JLabel.CENTER);

        getContentPane().add(label, BorderLayout.CENTER);
    }
}
