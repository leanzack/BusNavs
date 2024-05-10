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
	  private List<String> selectedRouteNames = new ArrayList();
	  private List<Button> selectedRouteButtons = new ArrayList();


    
    @FXML
    private Label driverNameLabel;
    @FXML 
    private HBox hbox_route;
    
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
        String query = "SELECT route_name FROM routes";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pst = conn.prepareStatement(query);
             ResultSet rs = pst.executeQuery()) {

            List<Button> buttons = new ArrayList<>();
            while (rs.next()) {
                String routeName = rs.getString("route_name");

                Button routeButton = new Button(routeName);
                routeButton.getStyleClass().add("route-button");
                routeButton.setOnAction(e -> handleRouteSelection(routeName));

                buttons.add(routeButton);
            }
            Platform.runLater(() -> {
                hbox_route.getChildren().clear();
                hbox_route.getChildren().addAll(buttons);
            });
        } catch (SQLException ex) {
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Error loading routes: " + ex.getMessage());
                alert.showAndWait();
            });
        }
    }

    private void handleRouteSelection(String routeName) {
    	
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
