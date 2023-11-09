package comp231.master;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InventoryPage extends JFrame {

    public InventoryPage() {
        // Set up the JFrame for Inventory Management
        setTitle("Inventory Management");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        InventoryUpdatePanel updatePanel = new InventoryUpdatePanel();

        
        // Create search bar UI
        JTextField searchBar = new JTextField();
        searchBar.setPreferredSize(new Dimension(300, 30));

        JButton searchButton = new JButton("Search");

        // Create result display area
        JTextArea resultArea = new JTextArea();
        resultArea.setEditable(false);

        // Create buttons for viewing, updating, and discarding inventory
        JButton viewInventoryButton = new JButton("View Inventory Levels");
        JButton updateInventoryButton = new JButton("Update Inventory");
        JButton discardInventoryButton = new JButton("Discard Inventory");

        // Set layout manager for Inventory Management frame
        setLayout(new BorderLayout());

        // Create panel for search bar and button
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        searchPanel.add(searchBar);
        searchPanel.add(searchButton);

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
                String searchResult = performSearch(query);
                resultArea.setText(searchResult);
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

    private String performSearch(String query) {
        // Implement your search logic here (replace this with your actual search logic)
        // This is a placeholder, replace it with your actual search logic.
        return "Search results for: " + query;
    }

}
