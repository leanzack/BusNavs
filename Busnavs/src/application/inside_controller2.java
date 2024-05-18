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
        loaddriverbutton();

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

              //    fareLabel.setOnAction(e -> fareUps(routeName, fare));

                  routeButton.setOnAction(e -> {
                      handleRouteSelection(routeName, fare);
                  });

                  VBox buttonContainer = new VBox(routeButton, fareLabel);
                  buttonContainer.setAlignment(null);
                  routeButton.getStyleClass().add("route-button");

                  fareLabel.getStyleClass().add("fare-button-passenger");
                  fareLabels.put(routeName, fareLabel); // Populate the fareLabels map

                  
                  HBox hbox = new HBox(buttonContainer);
                  buttonContainers.add(hbox);
                  buttonContainer.setAlignment(Pos.CENTER); // Set alignment to center
                  
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
    
    public void loaddriverbutton() {

    	String query = "SELECT driver_name FROM driver";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pst = conn.prepareStatement(query);
             ResultSet rs = pst.executeQuery()) {

            List<HBox> buttonContainers = new ArrayList<>();
            while (rs.next()) {
            	
                String driver = rs.getString("driver_name");
               
                Button driverButton = new Button(driver);

            //    fareLabel.setOnAction(e -> fareUps(routeName, fare));

                driverButton.setOnAction(e -> {
                    handleRouteSelectionforDriver(driver);
                });

                VBox buttonContainer2 = new VBox(driverButton);
                buttonContainer2.setAlignment(null);
                driverButton.getStyleClass().add("route-button");


                
                HBox hbox = new HBox(buttonContainer2);
                buttonContainers.add(hbox);
                buttonContainer2.setAlignment(Pos.CENTER); // Set alignment to center
                
                driverButton.setMaxWidth(Double.MAX_VALUE);


            }
            Platform.runLater(() -> {
            	h_box_fordriver.getChildren().clear();
            	h_box_fordriver.setAlignment(Pos.CENTER); // Ensure the alignment of HBox content

            	h_box_fordriver.getChildren().addAll(buttonContainers);

            });
        } catch (SQLException ex) {
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Error loading routes: " + ex.getMessage());
                alert.showAndWait();
            });
        }
    }
  
  
    

    private void handleRouteSelectionforDriver(String driver) {
    	
        Button s_driver = new Button(driver);
        s_driver.getStyleClass().add("vbox_routebutton");
        s_driver.setWrapText(true); // Enable text wrapping

     
        selectedRouteButtons.add(s_driver);

       

        s_driver.setOnAction(e -> handleRouteDeselection_forDriver(s_driver));
        s_driver.setMaxWidth(Double.MAX_VALUE);
     //   fareLabel.setOnAction(e -> fareUps_insideDriver(routeName, fare));
//
        
     Platform.runLater(() -> {
       	 
    	 vbox.getChildren().clear();
    	 vbox.getChildren().addAll(selectedRouteButtons);
        });
        
   
    

    // Display an alert for the selected route
    Platform.runLater(() -> {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Chosen route: " + s_driver);
        alert.showAndWait();
    });
    

}

	private void handleRouteDeselection_forDriver(Button s_driver) {
			selectedRouteButtons.remove(s_driver);
      
        
        // Update the VBox to reflect the current state of selected routes
        Platform.runLater(() -> {
        	vbox.getChildren().clear();
        	vbox.getChildren().addAll(selectedRouteButtons);
        });
    }
	

	private void handleRouteSelection(String routeName, double fare) {
    	
        Button s_route = new Button(routeName);
        s_route.getStyleClass().add("vbox_routebutton");
        s_route.setWrapText(true); // Enable text wrapping

        Button fareLabel = new Button(String.format("₱%.2f", fare));
        fareLabel.getStyleClass().add("fare-button-passenger");
        selectedRouteButtons.add(s_route);

        selectedRouteButtons.add(fareLabel);
        
        fareLabel.setMinWidth(150); // Or any fixed width you desire

        s_route.setOnAction(e -> handleRouteDeselection( s_route, fareLabel));
        s_route.setMaxWidth(Double.MAX_VALUE);
     //   fareLabel.setOnAction(e -> fareUps_insideDriver(routeName, fare));
//
        
     Platform.runLater(() -> {
       	 
    	 vbox.getChildren().clear();
    	 vbox.getChildren().addAll(selectedRouteButtons);
        });
        
   
    

    // Display an alert for the selected route
    Platform.runLater(() -> {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Chosen route: " + routeName);
        alert.showAndWait();
    });
    

}
    
private void handleRouteDeselection( Button s_route, Button fareLabel) {
    	
        selectedRouteButtons.remove(s_route);
        
        selectedRouteButtons.remove(fareLabel);
        
        
 

        // Update the VBox to reflect the current state of selected routes
        Platform.runLater(() -> {
        	vbox.getChildren().clear();
        	vbox.getChildren().addAll(selectedRouteButtons);
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
  
