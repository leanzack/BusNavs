package application;


import java.io.IOException;
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
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.image.Image;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class inside_controller {
	
	  public DBConnectionManager dbManager = new DBConnectionManager();
	  private List<String> selectedRouteNames = new ArrayList<String>();
	  private List<Button> selectedRouteButtons = new ArrayList<Button>();
	  private Map<String, Button> fareLabels = new HashMap<>();
	  private List<String> routesToDelete = new ArrayList<>();

	@FXML
	private String driverName; 

    @FXML
    private Label driverNameLabel;
    @FXML 
    private HBox hbox_route;
    
    @FXML 
    private HBox logout_box;

    @FXML
    private Label today;
    
    @FXML 
    private VBox vbox_route;
  
    @FXML 
    private BorderPane border_visibility;
    

    @FXML 
    private Button addRouteButton;
 
    
    @FXML 
    private Button selected;
    @FXML
    private ImageView imageView;
    @FXML
    private ImageView logo;

    @FXML
    private String selectedRouteName;
    @FXML 
    private double fareLabel;
  
 
 
    
    

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
  
    
    public void initialize() throws IOException {
        loadRouteButtons();
        
        
        
        try {
            Image driverImage = new Image(getClass().getResource("/imageg/driver.png").toExternalForm());
            imageView.setImage(driverImage);
            Image logoImage = new Image(getClass().getResource("/imageg/navvs.png").toExternalForm());
            logo.setImage(logoImage);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        Logout_account(null);
    }

	  
	  private void applyStylesheet(Scene scene) {
	        String cssPath = "application.css"; // Adjust this path as needed
	        scene.getStylesheets().add(getClass().getResource(cssPath).toExternalForm());
	    }


    @FXML
    private void Logout_account(MouseEvent event) throws IOException {
		   if (driverName != null && !driverName.isEmpty()) {
			   
			   Parent root = FXMLLoader.load(getClass().getResource("/fxml/MainScene.fxml"));
		        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		        Scene scene = new Scene(root);
		        stage.setScene(scene);
		        stage.show();
		        applyStylesheet(scene);
	            	
	            System.out.println("Logging out driver: " + driverName);

	        } else {
	            System.out.println("No driver is logged in.");
	        }
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
                Button deleteRouteAction = new Button("Delete");

                fareLabel.setOnAction(e -> fareUps(routeName, fare));
                routeButton.setOnAction(e -> handleRouteSelection(routeName, fare));
                deleteRouteAction.setOnAction(e -> toggleRouteForDeletion(routeName, fare));

                VBox buttonContainer = new VBox(routeButton, fareLabel, deleteRouteAction);
                buttonContainer.setAlignment(Pos.CENTER);
                buttonContainer.setSpacing(10); 

                
                routeButton.getStyleClass().add("route-button");
                fareLabel.getStyleClass().add("fare-button");
                deleteRouteAction.getStyleClass().add("delete");

                fareLabels.put(routeName, fareLabel);

                HBox hbox = new HBox(buttonContainer);
                hbox.setAlignment(Pos.CENTER);
                buttonContainers.add(hbox);
            }

            Platform.runLater(() -> {
                hbox_route.getChildren().clear();
                hbox_route.getChildren().addAll(buttonContainers);
            });

        } catch (SQLException ex) {
            showErrorAlert("Error loading routes: " + ex.getMessage());
        }
    }
    

	private void handleRouteSelection(String routeName, double fare) {
    	
    
    	border_visibility.setVisible(true);
      	today.setVisible(true);
      	selected.setVisible(true);

    	 if (!selectedRouteNames.contains(routeName)) {
             selectedRouteNames.add(routeName);

             Button s_route = new Button(routeName);
             s_route.getStyleClass().add("vbox_routebutton");
             s_route.setWrapText(true); // Enable text wrapping

             Button fareLabel = new Button(String.format("₱%.2f", fare));
             fareLabel.getStyleClass().add("fare-button");
             selectedRouteButtons.add(s_route);

             selectedRouteButtons.add(fareLabel);
             
             fareLabel.setMinWidth(150); // Or any fixed width you desire

             s_route.setOnAction(e -> handleRouteDeselection(routeName, s_route, fareLabel));
             s_route.setMaxWidth(Double.MAX_VALUE);
             fareLabel.setOnAction(e -> fareUps_insideDriver(routeName, fare));


             
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
    
	private void fareUps_insideDriver(String routeName, double fare) {
		
		 TextInputDialog dialog = new TextInputDialog(String.format("%.2f", fare));
		    dialog.setTitle("Update Fare");
		    dialog.setHeaderText("Update the fare for " + routeName);
		    dialog.setContentText("New fare:");

		    Optional<String> result = dialog.showAndWait();
		    result.ifPresent(newFareStr -> {
		        try {
		            double newFare = Double.parseDouble(newFareStr);
		            updateFareInDriver(routeName, newFare);

		            Button fareLabel = selectedRouteButtons.stream()
		                .filter(btn -> btn.getText().contains(String.format("₱%.2f", fare)))
		                .findFirst()
		                .orElse(null);
		            
		            if (fareLabel != null) {
		                fareLabel.setText(String.format("₱%.2f", newFare));
		            }
		        } catch (NumberFormatException ex) {
		            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Invalid fare entered: " + newFareStr);
		            alert.showAndWait();
		        }
		    });
		}
	private void updateFareInDriver(String routeName, double newFare) {
	    String sql = "UPDATE SelectedRoutes SET fare = ? WHERE selected_route = ?";
	    try (Connection conn = dbManager.getConnection();
	         PreparedStatement pst = conn.prepareStatement(sql)) {
	        
	        pst.setDouble(1, newFare);
	        pst.setString(2, routeName);

	        int rowsAffected = pst.executeUpdate();
	        if (rowsAffected > 0) {
	            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Fare updated successfully for route: " + routeName);
	            alert.showAndWait();
	        } 
	    } catch (SQLException e) {
	        Alert alert = new Alert(Alert.AlertType.ERROR, "Error updating fare for route: " + routeName);
	        alert.showAndWait();
	        e.printStackTrace();
	    }
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
   
       String updateQuery = "INSERT INTO SelectedRoutes (driver_name, selected_route, fare) VALUES (?, ?, ?);";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pst = conn.prepareStatement(updateQuery)) {
            
            for (String routeName : selectedRouteNames) {
                // Query the fare for the selected route from the database
                double fare = queryFareForRoute(routeName);
                
                pst.setString(1, driverName);
                pst.setString(2, routeName);
                pst.setDouble(3, fare); // Set the fare obtained from the database
                pst.addBatch(); // Add the current route to the batch
            }

            int[] rowsAffected = pst.executeBatch(); // Execute the batch insert
            
            // Check if any rows were affected
            boolean success = false;
            for (int row : rowsAffected) {
                if (row > 0) {
                    success = true;
                    break;
                }
            }

            if (success) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Driver data updated successfully. Routes: " + String.join(", ", selectedRouteNames));
                alert.showAndWait();
            }
            
        } catch (SQLException e) {
            e.printStackTrace(); // You can replace this with logging or other error handling
        }
    }
    

    private double queryFareForRoute(String routeName) throws SQLException {
        String fareQuery = "SELECT fare FROM routes WHERE route_name = ?";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pst = conn.prepareStatement(fareQuery)) {
            pst.setString(1, routeName);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("fare"); // Return the fare from the query result
                }
            }
        }
        throw new SQLException("Fare not found for route: " + routeName);
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
        loadRouteButtons();
    }
  
    
private void insertTODB(String routeName, double fare) {
		
		String qry = "INSERT INTO routes (route_name, fare) VALUES (?, ?)";
		
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




			
			private void toggleRouteForDeletion(String routeName, double fare) {
			    Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
			    confirmationAlert.setTitle("Confirm Deletion");
			    confirmationAlert.setHeaderText("Delete Route");
			    confirmationAlert.setContentText("Are you sure you want to delete the route: " + routeName + "?");

			    Optional<ButtonType> result = confirmationAlert.showAndWait();
			    if (result.isPresent() && result.get() == ButtonType.OK) {
			        // User confirmed deletion, proceed with deletion
			        deleteRoute(routeName, fare);
			    } else {
			        // User canceled deletion, do nothing
			    }
			}

			private void deleteRoute(String routeName, double fare) {
			    String deleteQuery = "DELETE FROM routes WHERE route_name = ?";
			    
			    try (Connection conn = dbManager.getConnection();
			         PreparedStatement pst = conn.prepareStatement(deleteQuery)) {
			         
			        pst.setString(1, routeName);
			        int rowsAffected = pst.executeUpdate();
			        
			        if (rowsAffected > 0) {
			            // Route successfully deleted from the database
			            routesToDelete.remove(routeName);

			            // Provide visual feedback for the user (optional)
			            Alert alert = new Alert(Alert.AlertType.INFORMATION);
			            alert.setTitle("Route Deletion");
			            alert.setHeaderText(null);
			            alert.setContentText("Route " + routeName + " has been successfully deleted from the database.");
			            alert.showAndWait();
			        } else {
			            // Route not found in the database
			            Alert alert = new Alert(Alert.AlertType.ERROR);
			            alert.setTitle("Error");
			            alert.setHeaderText(null);
			            alert.setContentText("Route " + routeName + " was not found in the database.");
			            alert.showAndWait();
			        }
			    } catch (SQLException e) {
			        // Database error occurred
			        Alert alert = new Alert(Alert.AlertType.ERROR);
			        alert.setTitle("Database Error");
			        alert.setHeaderText(null);
			        alert.setContentText("An error occurred while deleting route " + routeName + " from the database: " + e.getMessage());
			        alert.showAndWait();
			    }
			    loadRouteButtons();
			}
			
			private void showErrorAlert(String string) {
				   Alert alert = new Alert(Alert.AlertType.ERROR);
				    alert.setTitle("Error");
				    alert.setHeaderText("Error");
				    alert.showAndWait();		
			}


    
}