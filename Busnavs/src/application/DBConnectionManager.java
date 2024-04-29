package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBConnectionManager {
	  private Connection connection;

	    public DBConnectionManager() {
	        String url = "jdbc:mariadb://localhost:3306/busnavs";
	        String user = "root";
	        String pwd = "root";
	        
	        try {
	            Class.forName("org.mariadb.jdbc.Driver");
	            this.connection = DriverManager.getConnection(url, user, pwd);
	        } catch (SQLException | ClassNotFoundException e) {
	            e.printStackTrace();
	        }
	    }

	    public Connection getConnection() {
	        return this.connection;
	    }

	    public void closeConnection() {
	        if (this.connection != null) {
	            try {
	                this.connection.close();
	                System.out.println("Database connection closed");
	            } catch (SQLException e) {
	                e.printStackTrace();
	            }
	        }
	    }
	}