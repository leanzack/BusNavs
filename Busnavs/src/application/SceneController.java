package application;

import javafx.event.ActionEvent;  

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.fxml.FXML;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.control.Alert.AlertType;

import javafx.stage.Stage;

public class SceneController {
	



	  public DBConnectionManager dbManager = new DBConnectionManager();
	  
	  private void applyStylesheet(Scene scene) {
	        String cssPath = "application.css"; // Adjust this path as needed
	        scene.getStylesheets().add(getClass().getResource(cssPath).toExternalForm());
	    }
	    public String driverName; 
	    public String passengername; 
	    public ImageView for_logo; 


	private Stage stage;
	private Scene scene;

	 @FXML public TextField driver_id;
	 @FXML public TextField driver_name;
	 @FXML public TextField login;
	 @FXML public TextField login2;
	
	 @FXML public TextField passenger_name;


	
	 @FXML
	public void switchToScene2(ActionEvent event) throws IOException {
         Parent root = FXMLLoader.load(getClass().getResource("/fxml/Scene2.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();	
        applyStylesheet(scene); 
	}
	 @FXML
	public void switchToScene3(ActionEvent event) throws IOException {
         Parent root = FXMLLoader.load(getClass().getResource("/fxml/Scene3.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();	
        applyStylesheet(scene); 
	}
	 
	 @FXML
	public void Logout(ActionEvent event) throws IOException {
		    Alert alert = new Alert(AlertType.CONFIRMATION);
		    alert.setTitle("Logout");
		    alert.setHeaderText("You're about to go back to Main Menu!");
		    
	if (alert.showAndWait().get() == ButtonType.OK) {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/edit.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();	
        applyStylesheet(scene); 
        
	}
	 }
	 @FXML
	 public void BacktoMain(ActionEvent event) throws IOException {
	     Parent root = FXMLLoader.load(getClass().getResource("/fxml/edit.fxml"));
	     Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
	     Scene scene = new Scene(root);
	     stage.setScene(scene);
	     stage.show();
         applyStylesheet(scene); 

	     
	     
	 }
	 @FXML
	    public void Driver_main(ActionEvent event) throws IOException {

	            Parent root = FXMLLoader.load(getClass().getResource("/fxml/MainScene.fxml"));
	            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
	            scene = new Scene(root);
	            stage.setScene(scene);
	            stage.show();	
	            applyStylesheet(scene); 
	        }
	 
	 @FXML
	    public void Passenger_main(ActionEvent event) throws IOException {

	            Parent root = FXMLLoader.load(getClass().getResource("/fxml/MainScene2.fxml"));
	            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
	            scene = new Scene(root);
	            stage.setScene(scene);
	            stage.show();	
                applyStylesheet(scene); 
                
          
	        }
	

	 @FXML
	 public void Driver_inside(ActionEvent event) throws IOException {
	     if (login != null && !login.getText().trim().isEmpty()) {
	         String driverLogin = login.getText().trim();
	         String query = "SELECT driver_name FROM driver WHERE driver_id = ?";
	         
	         try (Connection conn = dbManager.getConnection();
	              PreparedStatement pst = conn.prepareStatement(query)) {

	             pst.setString(1, driverLogin);
	             try (ResultSet rs = pst.executeQuery()) {
	                 if (rs.next()) {
	                     String driverName = rs.getString("driver_name");

	                     FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/inside.fxml"));
	                     inside_controller controller = new inside_controller();
	                     loader.setController(controller);
	                     Parent root = loader.load();

	                     controller.setDriverName(driverName);

	                     this.driverName = driverName;

	                     Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
	                     Scene scene = new Scene(root);
	                     stage.setScene(scene);
	                     stage.alwaysOnTopProperty();

	                     stage.show();
	                  
	                     applyStylesheet(scene); 

	                     // Show welcome message
	                     Alert alert = new Alert(Alert.AlertType.INFORMATION);
	                     alert.setContentText("Welcome " + driverName);
	                     alert.showAndWait();
	                 } else {
	                     Alert alert = new Alert(Alert.AlertType.ERROR);
	                     alert.setContentText("No driver found with ID: " + driverLogin);
	                     alert.showAndWait();
	                 }
	             }
	         } catch (SQLException e) {
	             e.printStackTrace();
	             Alert alert = new Alert(Alert.AlertType.ERROR);
	             alert.setContentText("Database error: " + e.getMessage());
	             alert.showAndWait();
	         }
	     } else {
	         Alert alert = new Alert(Alert.AlertType.WARNING);
	         alert.setContentText("Please enter your driver ID.");
	         alert.showAndWait();
	     }
	 }
	 
	 @FXML
	 public void passenger_inside(ActionEvent event) throws IOException {
		
		   if (login2 != null && !login2.getText().trim().isEmpty()) {
		         String passNAME = login2.getText().trim();
		         String query = "SELECT passenger_name FROM passenger WHERE passenger_name = ?";
		         
		         try (Connection conn = dbManager.getConnection();
		              PreparedStatement pst = conn.prepareStatement(query)) {

		             pst.setString(1, passNAME);
		             try (ResultSet rs = pst.executeQuery()) {
		                 if (rs.next()) {
		                     String passengername = rs.getString("passenger_name");

		                     FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/passenger_inside.fxml"));
		                     inside_controller2 controller2 = new inside_controller2();
		                     loader.setController(controller2);
		                     Parent root = loader.load();

		                     controller2.setPassengerName(passengername);

		                     this.passengername = passengername;
		                     
		                     Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		                     Scene scene = new Scene(root);
		                     stage.setScene(scene);
		                     stage.alwaysOnTopProperty();
		                     stage.show();
		                     
		                     applyStylesheet(scene); 
		                     // Show welcome message
		                     Alert alert = new Alert(Alert.AlertType.INFORMATION);
		                     alert.setContentText("Welcome " + passengername);
		                     alert.showAndWait();
		                 } else {
		                     Alert alert = new Alert(Alert.AlertType.ERROR);
		                     alert.setContentText("No passenger found with ID: " + passNAME);
		                     alert.showAndWait();
		                 }
		             }
		         } catch (SQLException e) {
		             e.printStackTrace();
		             Alert alert = new Alert(Alert.AlertType.ERROR);
		             alert.setContentText("Database error: " + e.getMessage());
		             alert.showAndWait();
		         }
		     } else {
		         Alert alert = new Alert(Alert.AlertType.WARNING);
		         alert.setContentText("Please enter your passenger ID.");
		         alert.showAndWait();
		     }
		 }
		 
	
		 
		 
	 
	   
	 @FXML
	 public void pass_register() throws IOException {
		  if (passenger_name != null && !passenger_name.getText().trim().isEmpty()) {
		        String passNAME = passenger_name.getText().trim();

		        String query = "INSERT INTO passenger (passenger_name) VALUES (?);";

		        try (Connection conn = dbManager.getConnection();
		             PreparedStatement pst = conn.prepareStatement(query)) {

		            

		            
		            pst.setString(1, passNAME);
		            int result = pst.executeUpdate();

		            

		            // Create and show the confirmation alert
		            Alert alert = new Alert(AlertType.CONFIRMATION);
		            alert.setTitle("Registration Successful");
		            alert.setContentText("Successfully registered " + result + " record(s).");
		            alert.showAndWait();
		            
		            passenger_name.setText("");

		            
		        } catch (SQLException e) {
		            e.printStackTrace();

		            // Create and show the SQL error alert
		            Alert alert = new Alert(AlertType.ERROR);
		            alert.setTitle("Database Error");
		            alert.setHeaderText("Failed to register the passenger.");
		            alert.setContentText("A database error occurred: " + e.getMessage());
		            alert.showAndWait();
		        }
		    } else {
		        // Create and show the error alert for empty driver ID
		        Alert alert = new Alert(AlertType.ERROR);
		        alert.setTitle("Validation Error");
		        alert.setContentText("Error: Passenger ID is required and cannot be empty.");
		        alert.showAndWait();
		    }
		}
	
	 
		 
	 
	 @FXML
	 public void register() throws IOException {
	     if (driver_id != null && !driver_id.getText().trim().isEmpty()) {
	         String driverID = driver_id.getText().trim(); 
	         String driverNAME = driver_name.getText().trim();

	         // Check if driverID is an integer using a regular expression
	         if (!driverID.matches("\\d+")) {
	             // Show error alert for non-integer driver ID
	             Alert alert = new Alert(AlertType.ERROR);
	             alert.setTitle("Validation Error");
	             alert.setContentText("Error: Driver ID must be a number.");
	             alert.showAndWait();
	             return;
	         }

	         String query = "INSERT INTO driver (driver_id, driver_name) VALUES (?, ?);";

	         try (Connection conn = dbManager.getConnection();
	              PreparedStatement pst = conn.prepareStatement(query)) {

	             pst.setString(1, driverID);
	             pst.setString(2, driverNAME);
	             int result = pst.executeUpdate();

	             // Create and show the confirmation alert
	             Alert alert = new Alert(AlertType.CONFIRMATION);
	             alert.setTitle("Registration Successful");
	             alert.setContentText("Successfully registered " + result + " record(s).");
	             alert.showAndWait();
	             
	             driver_id.setText("");
	             driver_name.setText("");
	             
	         } catch (SQLException e) {
	             e.printStackTrace();

	             // Create and show the SQL error alert
	             Alert alert = new Alert(AlertType.ERROR);
	             alert.setTitle("Database Error");
	             alert.setHeaderText("Failed to register the driver.");
	             alert.setContentText("A database error occurred: " + e.getMessage());
	             alert.showAndWait();
	         }
	     } else {
	         // Create and show the error alert for empty driver ID
	         Alert alert = new Alert(AlertType.ERROR);
	         alert.setTitle("Validation Error");
	         alert.setContentText("Error: Driver ID is required and cannot be empty.");
	         alert.showAndWait();
	     }
	
	 }
}


	


