package application;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class inside_controller {
    
    @FXML
    private Label driverNameLabel;
    private VBox routesVBox;

    // Constructor
    public inside_controller() {
        // Constructor logic, if any
    }
    
    // Method to set driver name
    public void setDriverName(String driverName) {
        driverNameLabel.setText(driverName);
    }
  
    
    // Other methods if needed
}
