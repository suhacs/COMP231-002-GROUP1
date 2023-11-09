package comp231.master;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainApp extends JFrame {

    public MainApp() {
        // Set up the JFrame
        setTitle("Inventory Management App");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create navigation links
        JButton homeButton = new JButton("Home");
        JButton inventoryButton = new JButton("Inventory Management");
        JButton orderButton = new JButton("Order Management");
        JButton salesButton = new JButton("Sales Lookup");
        JButton wastageButton = new JButton("Wastage Update");

        // Set layout manager
        setLayout(new GridLayout(5, 1));

        // Add buttons to the layout
        add(homeButton);
        add(inventoryButton);
        add(orderButton);
        add(salesButton);
        add(wastageButton);

        // Add action listeners to handle button clicks
        homeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(MainApp.this, "You are on the Home page");
            }
        });

        inventoryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openInventoryManagement();
            }
        });

        orderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openOrderManagement();
            }
        });

        salesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openSalesLookup();
            }
        });

        wastageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openWastageUpdate();
            }
        });


    }

    private void openInventoryManagement() {
        // Create and display the InventoryPage JFrame
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new InventoryPage().setVisible(true);
            }
        });
    }
    
    private void openOrderManagement() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new OrderManagementPage().setVisible(true);
            }
        });
    }

    private void openSalesLookup() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new SalesLookupPage().setVisible(true);
            }
        });
    }

    private void openWastageUpdate() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new WastageUpdatePage().setVisible(true);
            }
        });
    }

    public static void main(String[] args) {
        // Create and display the JFrame
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MainApp().setVisible(true);
            }
        });
    }
    
    
}
