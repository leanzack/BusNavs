package application;

import javafx.event.ActionEvent;

import javafx.fxml.FXML;


public class Controller {

    @FXML
    public void handleDriverClick(ActionEvent event) {
        System.out.println("Driver button clicked!");
        }
    
    @FXML
    public void handlePassengerClick(ActionEvent event) {
        System.out.println("Passenger button clicked!");
        }
}
