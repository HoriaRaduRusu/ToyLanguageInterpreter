package View;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader1 = new FXMLLoader(MainApplication.class.getResource("main-application.fxml"));
        Scene scene1 = new Scene(fxmlLoader1.load(), 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene1);
        stage.show();

    }

    public static void main(String[] args) {
        launch();
    }


}