package cz.xlisto.znacky;


import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class ZnackyController {
    // Inicializace loggeru pro tuto třídu
    private static final Logger logger = LoggerFactory.getLogger(ZnackyController.class);
    @FXML
    public ListView<String> listView;
    @FXML
    private Label welcomeText;
    private WebLoader webLoader;
    private final String url = "http://www.celysvet.cz/test-znalosti-dopravnich-znacek-databaze";


    @FXML
    protected void onLoadImgButtonClick() {
        webLoader = new WebLoader();
        welcomeText.setText("Načítám obsah webu");

        // Vytvoří nové okno pro zobrazení načítání
        Stage loaderStage = new Stage();
        loaderStage.initStyle(StageStyle.UNDECORATED);
        loaderStage.initModality(Modality.APPLICATION_MODAL);

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("loader-dialog.fxml"));
            Scene scene = new Scene(loader.load());
            loaderStage.setScene(scene);
        } catch (IOException e) {
            logger.error("Chyba při načítání loader-dialog.fxml {}",e.getMessage());
            return;
        }

        // Vycentrování načítacího dialogu
        Stage primaryStage = (Stage) welcomeText.getScene().getWindow();
        loaderStage.setX(primaryStage.getX() + primaryStage.getWidth() / 2 - 75); // 75 is half of the loader stage width
        loaderStage.setY(primaryStage.getY() + primaryStage.getHeight() / 2 - 75); // 75 is half of the loader stage height


        // Zobrazí načítací dialog
        loaderStage.show();


        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                webLoader.loadWeb(url);
                return null;
            }

            @Override
            protected void succeeded() {
                Platform.runLater(() -> {
                    loaderStage.close();
                    welcomeText.setText("Načítání dokončeno");
                    listView.setItems(webLoader.getUrlList());
                });
            }

            @Override
            protected void failed() {
                Platform.runLater(() -> {
                    loaderStage.close();
                    welcomeText.setText("Načítání selhalo");
                });
            }
        };

        new Thread(task).start();
    }
}