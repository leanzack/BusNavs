package application;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class inside_controller2 {
    
    @FXML
    private Label passengerNameLabel;
    
    // Constructor
    public inside_controller2() {
        // Constructor logic, if any
    }
    
    // Method to set driver name
    public void setPassengerName(String passengername) {
    	passengerNameLabel.setText(passengername);
    }
  
    
    // Other methods if needed
}
