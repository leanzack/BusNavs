package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBConnectionManager {
    private final String url = "jdbc:mariadb://localhost:3306/busnavs";
    private final String user = "root";
    private final String pwd = "root";

    public Connection getConnection() throws SQLException, ClassNotFoundException {
        Class.forName("org.mariadb.jdbc.Driver");
        return DriverManager.getConnection(url, this.user, this.pwd);
    }

    
    public boolean driver(String driverID) {
        String sql = "SELECT COUNT(*) FROM driver WHERE driver_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, driverID);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                return true;  // Driver ID found
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;  // Driver ID not found or error
    }
}
