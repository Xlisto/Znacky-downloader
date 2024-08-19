package cz.xlisto.znacky;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Třída ZnackyApplication je hlavní třídou JavaFX aplikace pro stahování dopravních značek.
 * <p>
 * Tato třída rozšiřuje třídu Application a přepisuje metodu start, která je vstupním bodem JavaFX aplikace.
 * <p>
 * Hlavní funkce třídy:
 * <ul>
 *   <li>Načtení FXML souboru `znacky-view.fxml` pomocí FXMLLoader.</li>
 *   <li>Vytvoření a zobrazení hlavní scény aplikace.</li>
 * </ul>
 * <p>
 * Třída využívá následující knihovny:
 * <ul>
 *   <li>javafx.application.Application pro základní funkce JavaFX aplikace.</li>
 *   <li>javafx.fxml.FXMLLoader pro načítání FXML souborů.</li>
 *   <li>javafx.scene.Scene pro vytvoření a manipulaci s grafickou scénou.</li>
 *   <li>javafx.stage.Stage pro zobrazení hlavního okna aplikace.</li>
 * </ul>
 */
public class ZnackyApplication extends Application {
    /**
     * Prázdný konstruktor třídy ZnackyApplication.
     * <p>
     * Tento konstruktor nevykonává žádné akce a je zde pouze pro případné budoucí rozšíření.
     */
    public ZnackyApplication() {
        // Prázdný konstruktor
    }

    /**
     * Metoda start je vstupním bodem JavaFX aplikace.
     * <p>
     * Tato metoda načte FXML soubor `znacky-view.fxml` pomocí FXMLLoader, vytvoří hlavní scénu aplikace
     * a zobrazí ji v hlavním okně (Stage).
     *
     * @param stage hlavní okno aplikace, do kterého bude vložena hlavní scéna.
     * @throws IOException pokud dojde k chybě při načítání FXML souboru.
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ZnackyApplication.class.getResource("znacky-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Dopravní značky - downloader");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Hlavní metoda aplikace.
     * <p>
     * Tato metoda slouží jako vstupní bod pro spuštění JavaFX aplikace.
     * Volá metodu launch, která inicializuje a spustí JavaFX aplikaci.
     *
     * @param args argumenty příkazového řádku (nejsou použity).
     */
    public static void main(String[] args) {
        launch();
    }


}