module Busnavs {
	requires javafx.controls;
	requires javafx.graphics;
	requires javafx.fxml;
	requires java.desktop;
	requires java.sql;

	
	opens application to javafx.graphics, javafx.fxml;
}
