module view.interpretorImplementationGUI {
    requires javafx.controls;
    requires javafx.fxml;


    opens View to javafx.fxml;
    exports View;
    exports Controller;
    opens Controller to javafx.fxml;
}