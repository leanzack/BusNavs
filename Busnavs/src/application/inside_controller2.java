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
    private Label fixed_fare;
    

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
        String query = "SELECT route_name, fare FROM routes";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pst = conn.prepareStatement(query);
             ResultSet rs = pst.executeQuery()) {

            Platform.runLater(() -> h_box.getChildren().clear()); // Clear previous buttons if any

            while (rs.next()) {
                String routeName = rs.getString("route_name");
                double fare = rs.getDouble("fare");

                Button routeButton = new Button(routeName);
                routeButton.getStyleClass().add("route-button");
                routeButton.setOnAction(e -> handleRouteSelection(routeName, fare));

                Platform.runLater(() -> h_box.getChildren().add(routeButton));
            }
        } catch (SQLException ex) {
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Error loading routes: " + ex.getMessage());
                alert.showAndWait();
            });
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
    

    private void handleRouteSelection(String routeName, double fare) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Chosen route: " + routeName + ", Fare: " + String.format("%.2f", fare));
            alert.showAndWait();

            selectedRouteLabel.setText("Selected Route: " + routeName);
            fixed_fare.setText("Fare: " + String.format("%.2f", fare));
        });
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
        String fareText = fixed_fare.getText().replace("Fare: ", ""); // Remove the prefix to parse the number

        double fare = 0;
        try {
            fare = Double.parseDouble(fareText); // Attempt to parse the double
        } catch (NumberFormatException e) {
            Alert parseAlert = new Alert(Alert.AlertType.ERROR, "Invalid fare amount.");
            parseAlert.showAndWait();
            return; // Stop further execution because the fare is essential
        }

        if (routeName.equals("None") || driverName.equals("None") || routeName.isEmpty() || driverName.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Route and Driver are required.");
            alert.showAndWait();
        } else {
            saveSelectedOptions(routeName, driverName, passengerName, fare);
        }
    }

    
    private void saveSelectedOptions(String routeName, String driverName, String passengerName, double fare) {
        String sql = "INSERT INTO ticket (route_name, driver_name, passenger_name, fare) VALUES (?, ?, ?, ?)";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, routeName);
            pstmt.setString(2, driverName);
            pstmt.setString(3, passengerName);
            pstmt.setDouble(4, fare);  // Passing the passenger name
// Passing the passenger name
            pstmt.executeUpdate();
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Selection saved successfully!");
            alert.showAndWait();
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to save selection: " + e.getMessage());
            alert.showAndWait();
        }
    }

    

}
  
