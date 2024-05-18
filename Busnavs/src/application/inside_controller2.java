package application;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class inside_controller2 {

  
	  public DBConnectionManager dbManager = new DBConnectionManager();
	  private List<Button> selectedRouteButtons = new ArrayList<Button>();
	  private Map<String, Button> fareLabels = new HashMap<>();


    @FXML
    private Label passengerNameLabel;
    @FXML
    private String PassengerName;
    @FXML
    private VBox routesVBox;  
    @FXML
    private HBox h_box; 
    @FXML
    private HBox h_box_fordriver; 
    @FXML
    private VBox vbox; 
    

    @FXML
    private Label selectedDriverLabel; 
    @FXML
    private Label selectedRouteLabel; 
    @FXML
    private Label fixed_fare;
    

    @FXML
    public void initialize() {
        loadRouteButtons();

    }
    
    public void setPassemgerName(String PassengerName) {
        this.PassengerName = PassengerName;

        if (passengerNameLabel != null) {
        	passengerNameLabel.setText(PassengerName);
        }


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

            List<HBox> buttonContainers = new ArrayList<>();
            while (rs.next()) {
                String routeName = rs.getString("route_name");
                double fare = rs.getDouble("fare");

                Button routeButton = new Button(routeName);
                Button fareLabel = new Button(String.format("₱%.2f", fare));

                routeButton.setOnAction(e -> {
                    handleRouteSelection(routeButton); // Pass the button itself
                });

                VBox buttonContainer = new VBox(routeButton, fareLabel);
                buttonContainer.setAlignment(Pos.CENTER); // Set alignment to center
                routeButton.getStyleClass().add("route-button");
                fareLabel.getStyleClass().add("fare-button-passenger");

                HBox hbox = new HBox(buttonContainer);
                buttonContainers.add(hbox);
                routeButton.setMaxWidth(Double.MAX_VALUE);
            }
            Platform.runLater(() -> {
                h_box.getChildren().clear();
                h_box.setAlignment(Pos.CENTER); // Ensure the alignment of HBox content
                h_box.getChildren().addAll(buttonContainers);
            });
        } catch (SQLException ex) {
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Error loading routes: " + ex.getMessage());
                alert.showAndWait();
            });
        }
    }
    


  
    
    private void updateDriverList(List<String> drivers) {
    	
        Platform.runLater(() -> {
            // Clear the existing driver list (if any)
        	h_box_fordriver.getChildren().clear(); // Assuming h_box is used for displaying drivers as well

            // Add new driver buttons to the container
            for (String driver : drivers) {
                Button driverButton = new Button(driver);
                driverButton.getStyleClass().add("driver-button");
                h_box_fordriver.getChildren().add(driverButton);

                // Optionally, add an action for each driver button
                driverButton.setOnAction(e -> {
                	
                    handleDriverSelection(driver);
                });
            }
        });
    }


 

    // Method to update the UI with the list of drivers
    private void handleRouteSelection(Button routeButton) {
        String routeName = routeButton.getText(); // Extract the text from the button
        String query = "SELECT driver_name FROM selectedroutes WHERE selected_route = ?";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pst = conn.prepareStatement(query)) {

            pst.setString(1, routeName);

            try (ResultSet rs = pst.executeQuery()) {
                List<String> drivers = new ArrayList<>();
                while (rs.next()) {
                    String driverName = rs.getString("driver_name");
                    drivers.add(driverName);
                }
                // Call method to update UI with the list of drivers
                updateDriverList(drivers);
            }
        } catch (SQLException ex) {
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Error loading drivers: " + ex.getMessage());
                alert.showAndWait();
            });
        }
    }
    
    
    private void handleDriverSelection(String driverName) {
        // Implement what should happen when a driver is selected
    	
    	 Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Selected driver: " + driverName);
         alert.showAndWait();
         
         Button dr_route = new Button(driverName);
         dr_route.getStyleClass().add("fare-button-passenger");
         dr_route.setWrapText(true); // Enable text wrapping

 
         selectedRouteButtons.add(dr_route);

         

         dr_route.setOnAction(e -> handleRouteDeselection(dr_route));
         dr_route.setMaxWidth(Double.MAX_VALUE);
      //   fareLabel.setOnAction(e -> fareUps_insideDriver(routeName, fare));
 //
         
      Platform.runLater(() -> {
        	 
     	 vbox.getChildren().clear();
     	 vbox.getChildren().addAll(selectedRouteButtons);
         });
         
    }

  
  
	
	private void handleRouteDeselection(Button dr_route) {
		selectedRouteButtons.remove(dr_route);
  
    
    // Update the VBox to reflect the current state of selected routes
    Platform.runLater(() -> {
    	vbox.getChildren().clear();
    	vbox.getChildren().addAll(selectedRouteButtons);
    });
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
  
