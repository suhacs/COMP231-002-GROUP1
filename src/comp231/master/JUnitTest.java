package comp231.master;
import org.junit.*;



import static org.junit.Assert.*;

import java.lang.reflect.Method;
import java.sql.*;

	public class JUnitTest {
		
		private InventorySearcher searcher;
	    private InventoryDataFetcher dataFetcher;
	    private SalesLookupPage salesLookupPage;	    

	    @Before
	    public void setUp() {
	        salesLookupPage = new SalesLookupPage();
	        
	    }

	    
	    @Test
	    public void testAccessWastageLookup() throws Exception {
	        // Create an instance of WastageLookUpPage
	        WastageLookUpPage wastageLookUpPage = new WastageLookUpPage();

	        // Use reflection to invoke the private method
	        Method displayDisposalTable = WastageLookUpPage.class.getDeclaredMethod("displayDisposalTable");
	        displayDisposalTable.setAccessible(true);
	        displayDisposalTable.invoke(wastageLookUpPage);

	        // Fetch the actual result from the database
	        String actualResult = wastageLookUpPage.getDisposalTableText();

	        // Fetch the expected result from the database
	        String expectedResult = getExpectedResultFromDatabase();

	        // Print the results to the console for manual inspection
	        System.out.println("Expected Result:");
	        System.out.println(expectedResult);

	        System.out.println("\nActual Result:");
	        System.out.println(actualResult);

	        // Perform the assertion
	        assertEquals(expectedResult, actualResult);
	    }

	    private String getExpectedResultFromDatabase() {
	        // Use JDBC to fetch data from the database and format it into a string
	        StringBuilder expectedResult = new StringBuilder("Disposal Table:\n");
	        expectedResult.append("Disposal IDItem Name           Quantity DisposedDisposal Date       \n");

	        try (Connection connection = DatabaseManager.getConnection();
	             PreparedStatement statement = connection.prepareStatement("SELECT * FROM Disposal");
	             ResultSet resultSet = statement.executeQuery()) {

	            while (resultSet.next()) {
	                int disposalId = resultSet.getInt("DisposalID");
	                String itemName = resultSet.getString("ItemName");
	                int quantityDisposed = resultSet.getInt("QuantityDisposed");
	                String disposalDate = resultSet.getString("DisposalDate");

	                expectedResult.append(String.format("%-10d%-20s%-15d%-20s\n", disposalId, itemName, quantityDisposed, disposalDate));
	            }

	        } catch (SQLException ex) {
	            ex.printStackTrace();
	            // Handle the exception as needed
	        }

	        return expectedResult.toString();
	    }
	    
	    private String generateExpectedSalesRecords() {
	    	 StringBuilder expectedRecords = new StringBuilder("Sales Records:\n");
	    	    expectedRecords.append(String.format("%-10s%-20s%-15s%-10s\n", "Sale ID", "Item Name", "Quantity Sold", "Sale Date"));
	        try {
	            // Get a connection using the DatabaseManager
	            Connection connection = DatabaseManager.getConnection();
	            Statement statement = connection.createStatement();

	            // TODO: Adjust the SQL query based on your database schema
	            String query = "SELECT SaleID, ItemName, QuantitySold, SaleDate FROM Sales";
	            ResultSet resultSet = statement.executeQuery(query);

	            // Append each row to the expectedRecords StringBuilder
	            while (resultSet.next()) {
	                int saleID = resultSet.getInt("SaleID");
	                String itemName = resultSet.getString("ItemName");
	                int quantitySold = resultSet.getInt("QuantitySold");
	                String saleDate = resultSet.getString("SaleDate");

	                expectedRecords.append(String.format("%-10s%-20s%-15s%-10s\n", saleID, itemName, quantitySold, saleDate));
	            }

	            // Close resources
	            resultSet.close();
	            statement.close();
	            connection.close();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }

	        return expectedRecords.toString();
	    }

	    
	    @Test
	    public void testAccessSalesLookup() {
	        // Perform the action (access sales lookup)
	        salesLookupPage.displaySalesRecords();

	        // Get the displayed sales records from the JTextArea
	        String displayedSalesRecords = salesLookupPage.getSalesLookupArea().getText();

	        // Print the displayed records for comparison
	        System.out.println("Displayed Records:\n" + displayedSalesRecords);

	        // Define the expected outcome based on the data in the Sales table
	        String expectedSalesRecords = generateExpectedSalesRecords();

	        // Print the expected records for comparison
	        System.out.println("Expected Records:\n" + expectedSalesRecords);

	        // Perform the assertion
	        assertEquals("Sales records are not displayed as expected.", expectedSalesRecords, displayedSalesRecords);
	    }

		
	    @Test
	    public void test_ProductSearch() {
	        // Test 1
			String expectedResultForSearchApple = "Search results:\n" +
    		String.format("%-10d%-20s%-12d%-10.2f%-15d%-15s%-50s\n", 
            3, "Apple", 150, 0.50, 3, "2023-11-30", "Crisp and sweet apples from organic orchards.");

	        String result1 = performProductSearch("apple");
	        assertEquals(expectedResultForSearchApple, result1);

	        // Test 2
			String expectedResultForSearchZ = "Search results:\n" +
					String.format("No results found for: %s", "z");
			
	        String result2 = performProductSearch("z");
	        assertEquals(expectedResultForSearchZ, result2);
	    }

	    @Test
	    public void testInventoryLookup() {
	        // Test 1
	    	dataFetcher = new InventoryDataFetcher();
	    	String actualInventoryData = "";
	        try {
	            actualInventoryData = dataFetcher.fetchInventoryData();
	        } catch (SQLException e) {
	            fail("SQL exception should not have been thrown: " + e.getMessage());
	        }

	        // Define the expected outcome (this should match the format from your fetchInventoryData method)
	        String expectedInventoryData = "Inventory Levels:\n" +
	                                       String.format("%-10s%-20s%-10s%-15s%-15s%-20s\n", "Item ID", "Item Name", 
	                                       "Quantity", "Optimum Level", "Supplier ID", "Expiry Date") +
	                                       // The rest of the expected data here should match the actual state of your test database
	                                       String.format("%-10d%-20s%-10d%-15d%-15d%-20s\n", 1, "Chicken Thigh", 100, 150, 1, "2023-12-31") +
	                                       String.format("%-10d%-20s%-10d%-15d%-15d%-20s\n", 2, "Cookies", 190, 250, 2, "2024-01-31") +
	                                       String.format("%-10d%-20s%-10d%-15d%-15d%-20s\n", 3, "Apple", 150, 200, 3, "2023-11-30") +
	                                       String.format("%-10d%-20s%-10d%-15d%-15d%-20s\n", 4, "Avocado", 120, 180, 4, "2023-11-15") +
	                                       String.format("%-10d%-20s%-10d%-15d%-15d%-20s\n", 5, "Milk", 80, 120, 5, "2023-11-10") +
	                                       // Add more lines as needed based on your test database
	                                       "";

	        // Perform the assertion
	        assertEquals("Inventory data fetched does not match the expected format.", expectedInventoryData.trim(), actualInventoryData.trim());
	   
	    }
//
	    private String performProductSearch(String input) {
	        // Implement the logic for product search
	    	searcher = new InventorySearcher();
	    	
	        String result = null;
	        try {
	            result = searcher.performSearch(input);
	        } catch (SQLException e) {
	            fail("SQL exception should not have been thrown");
	        }

	        return result; // Replace with actual result
	    }

	    
	    private final OrderManager orderManager = new OrderManager();

	    @Test(expected = IllegalArgumentException.class)
	    public void testPlaceOrderWithNegativeQuantity() throws SQLException {
	        orderManager.placeOrder("Test Item", -1, 20.0, 1, "2023-12-31", "Test Details", 100);
	    }


	    
	   
	    
}

