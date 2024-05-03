package application;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class inside_controller {
    
    @FXML
    private Label driverNameLabel;
    
    // Constructor
    public inside_controller() {
        // Constructor logic, if any
    }
    
    // Method to set driver name
    public void setDriverName(String driverName) {
        driverNameLabel.setText(driverName);
    }
    
    public void setIDName(String driverName) {
        driverNameLabel.setText(driverName);
    }
    
    // Other methods if needed
}
