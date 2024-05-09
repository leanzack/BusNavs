package application;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.application.Platform;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class inside_controller2 {

  
	  public DBConnectionManager dbManager = new DBConnectionManager();

    @FXML
    private Label passengerNameLabel;
    @FXML
    private VBox routesVBox;  
    @FXML
    private HBox h_box; 
    @FXML
    private HBox h_box1; 
    @FXML
    private Label selectedDriverLabel; 
    @FXML
    private Label selectedRouteLabel; 
   

    @FXML
    public void initialize() {
        loadRouteButtons();
        loaddriverButton();
    }
    // Method to set passenger name
    public void setPassengerName(String passengername) {
        Platform.runLater(() -> passengerNameLabel.setText(passengername));
    }

    // Method to load route buttons from the database
    public void loadRouteButtons() {
        String query = "SELECT route_name FROM routes";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pst = conn.prepareStatement(query);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                String routeName = rs.getString("route_name");
                Button routeButton = new Button(routeName);
                routeButton.getStyleClass().add("route-button");
               routeButton.setOnAction(e -> handleRouteSelection(routeName));

                Platform.runLater(() -> h_box.getChildren().add(routeButton));
            }
        } catch (SQLException ex) {
            ex.printStackTrace(); // Implement more user-friendly error handling
        }
    }
    
    public void loaddriverButton() {
        String query = "SELECT driver_name FROM driver";
        try (Connection conn2 = dbManager.getConnection();
             PreparedStatement pst = conn2.prepareStatement(query);
             ResultSet rs2 = pst.executeQuery()) {

            while (rs2.next()) {
                String driverName = rs2.getString("driver_name");
                Button driverButton = new Button(driverName);
                driverButton.getStyleClass().add("route-button");
                driverButton.setOnAction(e -> handledriverselect(driverName));

                Platform.runLater(() -> h_box1.getChildren().add(driverButton));
            }
        } catch (SQLException ex) {
            ex.printStackTrace(); // Implement more user-friendly error handling
        }
    }
    

    private void handleRouteSelection(String routeName) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Chosen route: " + routeName);
        alert.showAndWait();
        Platform.runLater(() -> selectedRouteLabel.setText("Selected Route: " + routeName));
    }

    private void handledriverselect(String driverName) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Chosen driver: " + driverName);
        alert.showAndWait();
        Platform.runLater(() -> selectedDriverLabel.setText("Selected Driver: " + driverName));
    
    }
     
    public void handleDoneAction() {
        String routeName = selectedRouteLabel.getText().replace("Selected Route: ", "");
        String driverName = selectedDriverLabel.getText().replace("Selected Driver: ", "");
        String passengerName = passengerNameLabel.getText(); // Get the passenger name from the label

        if (routeName.equals("None") || driverName.equals("None") || routeName.isEmpty() || driverName.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Route and Driver is required.");
            alert.showAndWait();
        } else {
            saveSelectedOptions(routeName, driverName, passengerName); 
        }
    }
    
    private void saveSelectedOptions(String routeName, String driverName, String passengerName) {
        String sql = "INSERT INTO ticket (route_name, driver_name, passenger_name) VALUES (?, ?, ?)";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, routeName);
            pstmt.setString(2, driverName);
            pstmt.setString(3, passengerName);  // Passing the passenger name
            pstmt.executeUpdate();
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Selection saved successfully!");
            alert.showAndWait();
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to save selection: " + e.getMessage());
            alert.showAndWait();
        }
    }

    

}
  
