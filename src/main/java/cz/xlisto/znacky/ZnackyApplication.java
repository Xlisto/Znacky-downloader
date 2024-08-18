package cz.xlisto.znacky;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ZnackyApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ZnackyApplication.class.getResource("znacky-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Dopravní značky - downloader");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}