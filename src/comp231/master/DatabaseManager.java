package comp231.master;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {
	private static final String URL = "jdbc:mysql://localhost:3306/comp231";
	private static final String USER = "root";
	private static final String PASSWORD = System.getenv("DB_PASSWORD");

	public static Connection getConnection() throws SQLException {
		return DriverManager.getConnection(URL, USER, PASSWORD);
	}

}