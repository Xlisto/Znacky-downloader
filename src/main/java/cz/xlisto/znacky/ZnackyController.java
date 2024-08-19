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

/**
 * Třída ZnackyController slouží jako kontroler pro hlavní okno aplikace.
 * <p>
 * Tato třída zajišťuje načítání obsahu webové stránky s dopravními značkami a zobrazení
 * nalezených URL adres v ListView. Obsahuje metody pro inicializaci komponent a
 * zpracování událostí uživatelského rozhraní.
 * <p>
 * Hlavní funkce třídy:
 * <ul>
 *   <li>Načítání obsahu webové stránky pomocí metody onLoadImgButtonClick.</li>
 *   <li>Zobrazení načítacího dialogu během načítání obsahu.</li>
 *   <li>Aktualizace ListView s nalezenými URL adresami po dokončení načítání.</li>
 *   <li>Aktualizace průběhu načítání v labelu progress.</li>
 * </ul>
 * <p>
 * Třída využívá následující knihovny:
 * <ul>
 *   <li>javafx.application.Platform pro práci s vlákny v JavaFX.</li>
 *   <li>javafx.concurrent.Task pro asynchronní načítání obsahu.</li>
 *   <li>javafx.fxml.FXMLLoader pro načítání FXML souborů.</li>
 *   <li>javafx.scene.Scene a javafx.stage.Stage pro práci s okny a scénami.</li>
 *   <li>javafx.scene.control.Label a javafx.scene.control.ListView pro práci s GUI komponentami.</li>
 *   <li>org.slf4j.Logger a org.slf4j.LoggerFactory pro logování.</li>
 * </ul>
 */
public class ZnackyController {
    // Inicializace loggeru pro tuto třídu
    private static final Logger logger = LoggerFactory.getLogger(ZnackyController.class);
    @FXML
    public ListView<String> listView;
    @FXML
    private Label welcomeText;
    @FXML
    private Label progress;
    private WebLoader webLoader;
    private final String url = "http://www.celysvet.cz/test-znalosti-dopravnich-znacek-databaze";

    /**
     * Aktualizuje text v labelu progress s počtem načtených URL adres.
     * <p>
     * Tato metoda je volána z asynchronní úlohy, která načítá obsah webové stránky.
     * Používá Platform.runLater() pro zajištění, že aktualizace GUI proběhne na JavaFX Application Thread.
     * <p>
     * Metoda získá aktuální počet URL adres v seznamu urlList z instance WebLoader
     * a nastaví tento počet jako text v labelu progress.
     */
    public void updateProgress() {
        Platform.runLater(() -> progress.setText("Načteno " + webLoader.getUrlList().size() + " odkazů"));
    }

    /**
     * Metoda pro načtení obsahu webové stránky s dopravními značkami po kliknutí na tlačítko.
     * <p>
     * Tato metoda provádí následující kroky:
     * <ol>
     *   <li>Inicializuje instanci třídy WebLoader.</li>
     *   <li>Nastaví text uvítacího labelu na "Načítám obsah webu".</li>
     *   <li>Vytvoří nové okno pro zobrazení načítacího dialogu.</li>
     *   <li>Načte FXML soubor pro načítací dialog a nastaví scénu pro nové okno.</li>
     *   <li>Získá referenci na label progress pro zobrazení průběhu načítání.</li>
     *   <li>Vycentruje načítací dialog na hlavní okno aplikace.</li>
     *   <li>Zobrazí načítací dialog.</li>
     *   <li>Vytvoří a spustí asynchronní úlohu pro načítání obsahu webové stránky.</li>
     *   <li>Po úspěšném načtení obsahu zavře načítací dialog, aktualizuje text uvítacího labelu a nastaví položky ListView.</li>
     *   <li>V případě selhání načítání zavře načítací dialog a aktualizuje text uvítacího labelu na "Načítání selhalo".</li>
     * </ol>
     */
    @FXML
    protected void onLoadImgButtonClick() {
        webLoader = new WebLoader(this);
        welcomeText.setText("Načítám obsah webu");

        // Vytvoří nové okno pro zobrazení načítání
        Stage loaderStage = new Stage();
        loaderStage.initStyle(StageStyle.UNDECORATED);
        loaderStage.initModality(Modality.APPLICATION_MODAL);

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("loader-dialog.fxml"));
            Scene scene = new Scene(loader.load());
            loaderStage.setScene(scene);

            // Získat referenci na progress label
            this.progress = (Label) scene.lookup("#progress");
        } catch (IOException e) {
            logger.error("Chyba při načítání loader-dialog.fxml {}", e.getMessage());
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