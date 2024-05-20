package application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.application.Platform;




import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class inside_controller2 {

  
	  public DBConnectionManager dbManager = new DBConnectionManager();

	    private List<String> selectedDrivers = new ArrayList<>();
	    private List<String> selectedRouteNamesForTicket = new ArrayList<>();
	    private List<String> selectedDriversForTicket = new ArrayList<>();
	    private List<Double> selectedFaresForTicket = new ArrayList<>();
	    
	   //Current value of the ticket
	    private List<String> selectedDriverNames = new ArrayList<>();
	    private List<String> selectedRouteNames = new ArrayList<>();
	    private List<Integer> selectedTicketIds = new ArrayList<>();
	    private List<Double> selectedFares = new ArrayList<>();
	    

    @FXML
    private Label passen;
    @FXML
    private String passengername;
    @FXML
    private VBox routesVBox;  
    @FXML
    private HBox h_box; 
    @FXML
    private HBox h_box_fordriver; 
    @FXML
    private VBox vbox; 

    
    
 
    @FXML
    private ListView<String> view;
    
    @FXML
    private Button selectedRoute; 
    
    @FXML
    private Label selectedDriverLabel; 
    @FXML
    private Label selectedRouteLabel; 
    @FXML
    private Label fixed_fare;
    
    @FXML
    private Button selected;
    
    @FXML
    private Label routelist_label;
    
   


    private void applyStylesheet(Scene scene) {
        String cssPath = "application.css"; // Adjust this path as needed
        scene.getStylesheets().add(getClass().getResource(cssPath).toExternalForm());
    }

    
    
 
    
    public void setPassengerName(String passengername) {
        this.passengername = passengername;

        if (passen != null) {
        	passen.setText(passengername);
        }
    }

    @FXML
    public void initialize() throws IOException {
        loadRouteButtons();
        Logout_account2(null);
       
    }

   
    @FXML
    private void Logout_account2(MouseEvent event) throws IOException {
        if (passengername != null && !passengername.isEmpty()) {

            Parent root = FXMLLoader.load(getClass().getResource("/fxml/MainScene2.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            applyStylesheet(scene);

            System.out.println("Logging out driver: " + passengername);
        }
    }
    // Method to load route buttons from the database
    public void loadRouteButtons() {
    	String query = "SELECT route_name, fare, null as driver_name FROM routes " +
                "UNION ALL " +
                "SELECT selected_route AS route_name, fare AS fare, driver_name " +
                "FROM (SELECT DISTINCT selected_route, fare, driver_name FROM selectedroutes) AS subquery " +
                "WHERE NOT EXISTS (SELECT 1 FROM routes WHERE routes.route_name = subquery.selected_route AND routes.fare = subquery.fare)";
 	
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pst = conn.prepareStatement(query);
             ResultSet rs = pst.executeQuery()) {

            List<HBox> buttonContainers = new ArrayList<>();
            while (rs.next()) {
                String routeName = rs.getString("route_name");
                double fare = rs.getDouble("fare");

                String routeDriver = rs.getString("driver_name"); // Retrieve driver's name
                String buttonLabel;
                if (routeDriver != null) {
                    buttonLabel = routeName + " (" + routeDriver + ")";
                } else {
                    buttonLabel = routeName;
                }
                Button routeButton = new Button(buttonLabel);
                Button fareLabel = new Button(String.format("₱%.2f", fare));
                routeButton.getStyleClass().add("route-button");
                fareLabel.getStyleClass().add("fare-button-passenger");
                
                routeButton.setOnAction(e -> {
                    handleRouteSelection(routeButton, routeName, fare); // Pass the button itself
                });

                VBox buttonContainer = new VBox(routeButton, fareLabel);
                buttonContainer.setAlignment(Pos.CENTER); // Set alignment to center
               

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
    


  
    
    private void updateDriverList(List<String> drivers, String routeName, double fare ) {
    	
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
                	
                    handleDriverSelection(driver, routeName, fare);
                });
            }
        });
    }


 

    // Method to update the UI with the list of drivers
    private void handleRouteSelection(Button routeButton, String routeName, double fare) {
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
                updateDriverList(drivers, routeName, fare);
            }
        } catch (SQLException ex) {
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Error loading drivers: " + ex.getMessage());
                alert.showAndWait();
            });
        }
        loadRouteButtons();
    }
    
 
    private void handleDriverSelection(String driverName, String routeName, double fare) {
        // Implement what should happen when a driver is selected

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Selected driver: " + driverName);
        alert.showAndWait();

        selected.setVisible(true);
 
        
        routelist_label.setVisible(true);
        selectedDrivers.add(driverName);
        selectedRouteNamesForTicket.add(routeName);
        selectedDriversForTicket.add(driverName);
        selectedFaresForTicket.add(fare);
        
        // Update UI to display selected options (optional)
        Button dr_buts = new Button(driverName);
        Button routeButton = new Button(routeName);
        Button fareLabel = new Button(String.format("₱%.2f", fare));

        routeButton.getStyleClass().add("fare-button-passenger");
        fareLabel.getStyleClass().add("fare-button-passenger");
        dr_buts.getStyleClass().add("fare-button-passenger");

        // Create an HBox to contain the labels
        VBox driverInfoBox = new VBox(dr_buts, fareLabel, routeButton);
        driverInfoBox.setSpacing(10); // Set spacing between labels

        vbox.getChildren().add(driverInfoBox);
        
    }
    
    private int generateTicketId(Random random) {
        // Generate a random integer between 1000 and 9999
        return 1 + random.nextInt(1000);
    }
    
    public void routeActionselected() {
    	
        Random random = new Random();
        int identity = generateTicketId(random);

      
        String passengerName = passen.getText(); 

        if (passengerName != null && !passengerName.isEmpty()) {
        	
    	
            // Insert the current passenger name into the list
            selectedTicketIds.add(identity);
            selectedFares.add(selectedFaresForTicket.get(0)); 

            selectedDriverNames.add(selectedDriversForTicket.get(0)); 
            selectedRouteNames.add(selectedRouteNamesForTicket.get(0)); 

            refreshPassengerListView();


    	String updateQuery = "INSERT INTO ticket (route_name, driver_name, passenger_name, fare, ticket_id) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(updateQuery)) {
            for (int i = 0; i < selectedRouteNamesForTicket.size(); i++) {
                pstmt.setString(1, selectedRouteNamesForTicket.get(i));
                pstmt.setString(2, selectedDriversForTicket.get(i));
                pstmt.setString(3, passengername);
                pstmt.setDouble(4, selectedFaresForTicket.get(i));
                pstmt.setInt(5, identity);


                pstmt.executeUpdate();
            }
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Selection saved successfully!");
            alert.showAndWait();
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to save selection: " + e.getMessage());
            alert.showAndWait();
        }
        

    }
    }
    
    
    private void refreshPassengerListView() {
        Platform.runLater(() -> {
            view.getItems().clear(); // Clear the existing items
            for (int i = 0; i < selectedDriverNames.size(); i++) {
                String ticketInfo = "Driver: " + selectedDriverNames.get(i) + ", Route: " + selectedRouteNames.get(i) +
                                    ", Ticket ID: " + selectedTicketIds.get(i) + ", Fare: " + selectedFares.get(i);
                view.getItems().add(ticketInfo);
            }
        });
    }
}
    
    
    
    
    
   
    






    


  
