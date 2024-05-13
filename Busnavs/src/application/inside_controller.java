package application;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class inside_controller {
	
	  public DBConnectionManager dbManager = new DBConnectionManager();
	  private List<String> selectedRouteNames = new ArrayList<String>();
	  private List<Button> selectedRouteButtons = new ArrayList<Button>();


    
    @FXML
    private Label driverNameLabel;
    @FXML 
    private HBox hbox_route;
    
    @FXML
    private Label today;
    
    @FXML 
    private VBox vbox_route;
    // Constructor
    public inside_controller() {
        // Constructor logic, if any
    }
    
    // Method to set driver name
    public void setDriverName(String driverName) {
        if (driverNameLabel != null) {
            driverNameLabel.setText(driverName);
        }
    }
  
    public void initialize() {
        loadRouteButtons();
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
                VBox buttonContainer = new VBox(routeButton, fareLabel);
                buttonContainer.setAlignment(null);
                routeButton.getStyleClass().add("route-button");
                fareLabel.getStyleClass().add("route-button");

                routeButton.setOnAction(e -> handleRouteSelection(routeName));
                


                HBox hbox = new HBox(buttonContainer);
                buttonContainers.add(hbox);
            }
            Platform.runLater(() -> {
                hbox_route.getChildren().clear();
                hbox_route.getChildren().addAll(buttonContainers);
            });
        } catch (SQLException ex) {
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Error loading routes: " + ex.getMessage());
                alert.showAndWait();
            });
        }
    }
    
    private void UpdateRoute(String fare) {
    	
    }
    private void handleRouteSelection(String routeName) {
      	today.setVisible(true);

    	 if (!selectedRouteNames.contains(routeName)) {
             selectedRouteNames.add(routeName);

             // Create a new button for the selected route
             Button s_route = new Button(routeName);
             s_route.getStyleClass().add("route-button");
             s_route.setOnAction(e -> handleRouteDeselection(routeName, s_route));
             selectedRouteButtons.add(s_route);

             
             // Update the VBox with the new set of buttons
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
    private void handleRouteDeselection(String routeName, Button s_route) {
    	
    	selectedRouteNames.remove(routeName);
        selectedRouteButtons.remove(s_route);

        // Update the VBox to reflect the current state of selected routes
        Platform.runLater(() -> {
            vbox_route.getChildren().clear();
            vbox_route.getChildren().addAll(selectedRouteButtons);
        });
    }
    
    
}
