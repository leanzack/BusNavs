package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnectionManager {
    private String url = "jdbc:mariadb://localhost:3306/busnavs";
    private String user = "root";
    private String pwd = "root";

    public DBConnectionManager() {
        try {
            // Ensure the driver is registered
            Class.forName("org.mariadb.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        // Create and return a new connection every time this method is called
        try {
            return DriverManager.getConnection(url, user, pwd);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;  // Return null or throw a custom exception to handle connection failure
        }
    }

    // Method to explicitly close a connection if needed
    public void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
                System.out.println("Database connection closed");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
