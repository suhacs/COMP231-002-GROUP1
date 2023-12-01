package comp231.master;
import java.sql.*;

public class InventorySearcher {
	public String performSearch(String query) throws SQLException {
        StringBuilder searchResult = new StringBuilder("Search results:\n");
        String sql = "SELECT * FROM Inventory WHERE ItemName LIKE ?";

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, "%" + query + "%");
            ResultSet resultSet = preparedStatement.executeQuery();

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

                searchResult.append(String.format("%-10d%-20s%-12d%-10.2f%-15d%-15s%-50s\n", itemId, itemName, quantity,
                        price, supplierId, expiryDate, productDetails));
            }

            if (!resultsFound) {
                searchResult.append(String.format("No results found for: %s", query));
            }

        } catch (SQLException e) {
            throw e; 
        }

        return searchResult.toString();
    }
}