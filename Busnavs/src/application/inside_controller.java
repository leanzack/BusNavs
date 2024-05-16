package application;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class inside_controller {
	
	  public DBConnectionManager dbManager = new DBConnectionManager();
	  private List<String> selectedRouteNames = new ArrayList<String>();
	  private List<Button> selectedRouteButtons = new ArrayList<Button>();
	  private Map<String, Button> fareLabels = new HashMap<>();

	@FXML
	private String driverName; 

    @FXML
    private Label driverNameLabel;
    @FXML 
    private HBox hbox_route;
    
    @FXML
    private Label today;
    
    @FXML 
    private VBox vbox_route;

    @FXML 
    private Button addRouteButton;
 
    @FXML 
    private Button selected;
    

    // Constructor
    public inside_controller() {
    }
    
    // Method to set driver name
    public void setDriverName(String driverName) {
        this.driverName = driverName;

        if (driverNameLabel != null) {
            driverNameLabel.setText(driverName);
        }


    }
  
    
    public void initialize() {
        loadRouteButtons();
     


    }

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

                fareLabel.setOnAction(e -> fareUps(routeName, fare));

                routeButton.setOnAction(e -> {
                    handleRouteSelection(routeName, fare);
                });

                VBox buttonContainer = new VBox(routeButton, fareLabel);
                buttonContainer.setAlignment(null);
                routeButton.getStyleClass().add("route-button");

                fareLabel.getStyleClass().add("fare-button");
                fareLabels.put(routeName, fareLabel); // Populate the fareLabels map

                
                HBox hbox = new HBox(buttonContainer);
                buttonContainers.add(hbox);
                buttonContainer.setAlignment(Pos.CENTER); // Set alignment to center
                
                routeButton.setMaxWidth(Double.MAX_VALUE);


            }
            Platform.runLater(() -> {
                hbox_route.getChildren().clear();
                hbox_route.setAlignment(Pos.CENTER); // Ensure the alignment of HBox content

                hbox_route.getChildren().addAll(buttonContainers);

            });
        } catch (SQLException ex) {
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Error loading routes: " + ex.getMessage());
                alert.showAndWait();
            });
        }
    }
    

    private void handleRouteSelection(String routeName, double fare) {
    	
    
               
      	today.setVisible(true);


    	 if (!selectedRouteNames.contains(routeName)) {
             selectedRouteNames.add(routeName);

             Button s_route = new Button(routeName);
             s_route.getStyleClass().add("route-button");
             s_route.setWrapText(true); // Enable text wrapping

             Button fareLabel = new Button(String.format("₱%.2f", fare));
             fareLabel.getStyleClass().add("fare-button");
             selectedRouteButtons.add(fareLabel);
             
             fareLabel.setMinWidth(150); // Or any fixed width you desire

             s_route.setOnAction(e -> handleRouteDeselection(routeName, s_route, fareLabel));
             selectedRouteButtons.add(s_route);
             s_route.setMaxWidth(Double.MAX_VALUE);
             fareLabel.setOnAction(e -> fareUps(routeName, fare));

             
             Platform.runLater(() -> {
            	 
                 vbox_route.getChildren().clear();
                 vbox_route.getChildren().addAll(selectedRouteButtons);
             });
             

         }

         // Display an alert for the selected route
         Platform.runLater(() -> {
             Alert alert = new Alert(Alert.AlertType.INFORMATION, "Chosen route: " + routeName);
             alert.showAndWait();
         });
         
         selected.setVisible(true);

    }
    


	private void handleRouteDeselection(String routeName, Button s_route, Button fareLabel) {
    	
    	selectedRouteNames.remove(routeName);
        selectedRouteButtons.remove(s_route);
        
        selectedRouteButtons.remove(fareLabel);
        
        
 

        // Update the VBox to reflect the current state of selected routes
        Platform.runLater(() -> {
            vbox_route.getChildren().clear();
            vbox_route.getChildren().addAll(selectedRouteButtons);
        });
    }
    
    private void fareUps(String routeName, double fare) {
    	
        TextInputDialog dialog = new TextInputDialog(String.format("%.2f", fare));
        dialog.setTitle("Update Fare");
        dialog.setHeaderText("Update the fare for " + routeName);
        dialog.setContentText("New fare:");

        Optional<String> result = dialog.showAndWait();
      
        result.ifPresent(newFareStr -> {
            try {
                double newFare = Double.parseDouble(newFareStr);
                updateFareInDatabase(routeName, newFare);
                
                Button fareLabel = fareLabels.get(routeName);
                if (fareLabel != null) {
                    fareLabel.setText(String.format("₱%.2f", newFare));
                }
            } catch (NumberFormatException ex) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Invalid fare entered: " + newFareStr);
                alert.showAndWait();
                
            }
        });
    }
    
    public void routeSelected() {
    	
        String selectedRoutesString = String.join(",", selectedRouteNames);

        String updateQuery = "UPDATE driver SET selected_route = ? WHERE driver_name = ?";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pst = conn.prepareStatement(updateQuery)) {

            pst.setString(1, selectedRoutesString);
            pst.setString(2, driverName);
            int rowsAffected = pst.executeUpdate();

            if (rowsAffected > 0) {
                // If rows were affected, display an alert
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Current routes: " + selectedRoutesString);
                alert.showAndWait();
            } else {
                // If no rows were affected, you can handle this case accordingly
                // For example, display a different alert or perform some other action
                Alert alert = new Alert(Alert.AlertType.WARNING, "No rows were affected.");
                alert.showAndWait();
            }
        } catch (SQLException e) {
            // Handle SQLException appropriately
            e.printStackTrace(); // You can replace this with logging or other error handling
        }
    }
   	  
    
    private void updateFareInDatabase(String routeName, double newFare) {
    	
 	   String sql = "UPDATE routes SET fare = ? WHERE route_name = ?";
 	    
 	    try (Connection conn = dbManager.getConnection();
 	            PreparedStatement pst = conn.prepareStatement(sql)) {
 	           
 	           pst.setDouble(1, newFare);
 	           pst.setString(2, routeName);
 	           
 	           int rowsAffected = pst.executeUpdate();
 	           
 	           if (rowsAffected > 0) {
 	                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Fare updated successfully for route: " + routeName);
 	                alert.showAndWait();

 	           } else {
 	                Alert alert = new Alert(Alert.AlertType.ERROR, "No records found for route: " + routeName);
 	                alert.showAndWait();

 	               System.out.println("No records found for route: " + routeName);
 	           }
 	       } catch (SQLException e) {
 	    	   Alert alert = new Alert(Alert.AlertType.ERROR, "Error updating fare for route: " + routeName);
	                alert.showAndWait();
 	           e.printStackTrace();
 	       }
 	   }
    
    
    public void addRouteAction() {
        TextInputDialog routeDialog = new TextInputDialog();
        routeDialog.setTitle("Add Route");
        routeDialog.setHeaderText("Enter Route Name");
        routeDialog.setContentText("Route:");

        // Show the dialog and wait for the user's input
        routeDialog.showAndWait().ifPresent(routeName -> {
            // Create a TextInputDialog for the fare
            TextInputDialog fareDialog = new TextInputDialog();
            fareDialog.setTitle("Add Route");
            fareDialog.setHeaderText("Enter Fare");
            fareDialog.setContentText("Fare:");

            // Show the dialog and wait for the user's input
            fareDialog.showAndWait().ifPresent(fareStr -> {
                try {
                    // Parse the fare input as a double
                    double fare = Double.parseDouble(fareStr);
                    
                    insertTODB(routeName, fare);
                                   

                    // Call handleRouteSelection with the provided route name and fare
                } catch (NumberFormatException e) {
                    // Handle invalid fare input
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Invalid Fare");
                    alert.setContentText("Please enter a valid fare (numeric value).");
                    alert.showAndWait();
                }
            });
        });
    }
  
    
private void insertTODB(String routeName, double fare) {
		
		String qry = "INSERT INTO daily_route (route_name, fare) VALUES (?, ?)";
		
		  try (Connection conn = dbManager.getConnection();
			         PreparedStatement pst = conn.prepareStatement(qry)) {
		
		       	pst.setString(1, routeName);
		        pst.setDouble(2, fare);
		        
		        int rowsAffected = pst.executeUpdate();
		        if (rowsAffected > 0) {
		            Alert alert = new Alert(Alert.AlertType.INFORMATION);
		            alert.setTitle("Success");
		            alert.setHeaderText("Route Added");
		            alert.setContentText("The route has been successfully added.");
		            alert.showAndWait();
		        } else {
		            Alert alert = new Alert(Alert.AlertType.ERROR);
		            alert.setTitle("Error");
		            alert.setHeaderText("Route Not Added");
		            alert.setContentText("Failed to add the route to the database.");
		            alert.showAndWait();
		        }
		    } catch (SQLException ex) {
		        Alert alert = new Alert(Alert.AlertType.ERROR);
		        alert.setTitle("Error");
		        alert.setHeaderText("Database Error");
		        alert.setContentText("An error occurred while adding the route to the database: " + ex.getMessage());
		        alert.showAndWait();
		    }
		}
    
}
