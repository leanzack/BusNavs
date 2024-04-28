package application;
import javafx.event.ActionEvent;  

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javafx.fxml.FXML;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class SceneController {
	

	  //   //   private DBConnectionManager dbManager = new DBConnectionManager();
	    
	private Stage stage;
	private Scene scene;
	private Parent root;
	
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
	 public void BacktoMain(ActionEvent event) throws IOException {
		
		        Parent root = FXMLLoader.load(getClass().getResource("/edit.fxml"));
		        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		        Scene scene = new Scene(root);
		        stage.setScene(scene);
		        stage.show();
		   
		}
	 
	 @FXML
	    public void Driver_main(ActionEvent event) throws IOException {
	     //   		String driverID = "someDriverID"; // Assume this comes from an input field

		  //   //     if (dbManager.driver(driverID)) {
	            Parent root = FXMLLoader.load(getClass().getResource("/MainScene.fxml"));
	            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
	            scene = new Scene(root);
	            stage.setScene(scene);
	            stage.show();	
	        } 
	    }
//  }


	


