package application;
import javafx.event.ActionEvent;  

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javafx.fxml.FXML;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class SceneController {
	

	  public DBConnectionManager dbManager = new DBConnectionManager();
	    
	private Stage stage;
	private Scene scene;
	private Parent root;
	 @FXML public TextField driver_id;
	 @FXML public TextField driver_name;
	 @FXML public TextField login;


	
	 @FXML
	public void switchToScene2(ActionEvent event) throws IOException {
         Parent root = FXMLLoader.load(getClass().getResource("/Scene2.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();	
	}
	 @FXML
	public void Logout(ActionEvent event) throws IOException {
		    Alert alert = new Alert(AlertType.CONFIRMATION);
		    alert.setTitle("Logout");
		    alert.setHeaderText("You're about to go back to Main Menu!");
		    
	if (alert.showAndWait().get() == ButtonType.OK) {
        Parent root = FXMLLoader.load(getClass().getResource("/edit.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();	
	}
	 }
	 @FXML
	 public void BacktoMain(ActionEvent event) throws IOException {
		
		        Parent root = FXMLLoader.load(getClass().getResource("/edit.fxml"));
		        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		        Scene scene = new Scene(root);
		        stage.setScene(scene);
		        stage.show();
		   
		}
	 
	 @FXML
	    public void Driver_main(ActionEvent event) throws IOException {

	            Parent root = FXMLLoader.load(getClass().getResource("/MainScene.fxml"));
	            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
	            scene = new Scene(root);
	            stage.setScene(scene);
	            stage.show();	
	        }
	 @FXML
	 public void Driver_inside() throws IOException {
		 
		 if (login != null && !login.getText().trim().isEmpty()) {
		        String driverLOGIN = login.getText().trim(); 
		        
		        String query = "SELECT INTO driver (driver_id) VALUES (?);";


		        try (Connection conn = dbManager.getConnection();
		             PreparedStatement pst = conn.prepareStatement(query)) {

		            pst.setString(1, driverLOGIN);
		            pst.executeUpdate();

		        } catch (SQLException e) {
		            e.printStackTrace();
		            Alert alert3 = new Alert(AlertType.CONFIRMATION);
		          
		            alert3.setContentText("Welcome " + driverLOGIN );
		            alert3.showAndWait();
		        }
		    }
		}
	 
	 @FXML
	 public void register() throws IOException{
		 if (driver_id != null && !driver_id.getText().trim().isEmpty()) {
		        String driverID = driver_id.getText().trim(); 
		        String driverNAME = driver_name.getText().trim();

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

//  }


	


