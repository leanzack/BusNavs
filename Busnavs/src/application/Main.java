package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
        	
     
            // Load the root layout from FXML
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/edit.fxml"));

      
            Scene scene = new Scene(root);

            primaryStage.setScene(scene);
            primaryStage.show();
            primaryStage.setResizable(false);
            primaryStage.setTitle("Busnavs: Navigation and Ticketing");


        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
        
        String url = "jdbc:mariadb://localhost:3306/busnavs";
        String user = "root";
        String pwd = "root";
        
        try {            Class.forName("org.mariadb.jdbc.Driver"); 
            DriverManager.getConnection(url, user, pwd);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
 
    }
}
