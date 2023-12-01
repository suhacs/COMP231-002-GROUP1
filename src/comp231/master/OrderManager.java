package comp231.master;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderManager {

    // Result object to hold the status and order ID
    public static class OrderResult {
        public final boolean success;
        public final int orderId;

        public OrderResult(boolean success, int orderId) {
            this.success = success;
            this.orderId = orderId;
        }
    }

    public OrderResult placeOrder(String itemName, int quantity, double price, int supplierId, String expiryDate, String productDetails, int optimumLevel) throws SQLException {
        if (quantity < 0 || price < 0 || optimumLevel < 0) {
            throw new IllegalArgumentException("Quantity, price, and optimum level must be non-negative.");
        }

        String orderSql = "INSERT INTO `Order` (ItemName, Quantity, Price, SupplierID, ExpiryDate, ProductDetails) VALUES (?, ?, ?, ?, ?, ?)";
        int orderId = -1;

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement orderStatement = connection.prepareStatement(orderSql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            orderStatement.setString(1, itemName);
            orderStatement.setInt(2, quantity);
            orderStatement.setDouble(3, price);
            orderStatement.setInt(4, supplierId);
            orderStatement.setString(5, expiryDate);
            orderStatement.setString(6, productDetails);

            int rowsAffected = orderStatement.executeUpdate();
            if (rowsAffected > 0) {
                ResultSet generatedKeys = orderStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    orderId = generatedKeys.getInt(1);
                }
                return new OrderResult(true, orderId);
            }
        }
        return new OrderResult(false, orderId);
    }
}
