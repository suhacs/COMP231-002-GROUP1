package comp231.master;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class InventoryDataFetcher {

	public String fetchInventoryData() throws SQLException {
        StringBuilder formattedInventoryData = new StringBuilder("Inventory Levels:\n");
        formattedInventoryData.append(String.format("%-10s%-20s%-10s%-15s%-15s%-20s\n", "Item ID", "Item Name",
                "Quantity", "Optimum Level", "Supplier ID", "Expiry Date"));

        String sql = "SELECT ItemID, ItemName, Quantity, OptimumLevel, SupplierID, ExpiryDate FROM Inventory";

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int itemId = resultSet.getInt("ItemID");
                String itemName = resultSet.getString("ItemName");
                int quantity = resultSet.getInt("Quantity");
                int optimumLevel = resultSet.getInt("OptimumLevel");
                int supplierId = resultSet.getInt("SupplierID");
                String expiryDate = resultSet.getString("ExpiryDate");

                formattedInventoryData.append(String.format("%-10d%-20s%-10d%-15d%-15d%-20s\n", itemId, itemName,
                        quantity, optimumLevel, supplierId, expiryDate));
            }
        }
//        System.out.println(formattedInventoryData.toString());
        return formattedInventoryData.toString();
      }
   }

