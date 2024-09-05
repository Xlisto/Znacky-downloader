package cz.xlisto.znacky;


import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Properties;

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
    private static final String SETTINGS_FILE = "settings.ini";
    private static final String DIRECTORY_KEY = "defaultDirectory";
    /**
     * Logger pro zaznamenávání chyb a informací během načítání webových stránek.
     * <p>
     * Tento logger je inicializován pomocí třídy LoggerFactory a je použit pro logování
     * událostí a chyb v rámci třídy ZnackyController.
     */
    private static final Logger logger = LoggerFactory.getLogger(ZnackyController.class);
    /**
     * ListView pro zobrazení nalezených URL adres.
     * <p>
     * Tato komponenta je inicializována pomocí FXML a slouží k zobrazení seznamu URL adres
     * nalezených na webové stránce s dopravními značkami.
     */
    @FXML
    public ListView<String[]> listView;
    /**
     * Label pro zobrazení uvítacího textu.
     * <p>
     * Tato komponenta je inicializována pomocí FXML a slouží k zobrazení uvítacího textu
     * nebo stavu načítání obsahu webové stránky.
     */
    @FXML
    private Label welcomeText;
    /**
     * Label pro zobrazení průběhu načítání.
     * <p>
     * Tato komponenta je inicializována pomocí FXML a slouží k zobrazení aktuálního stavu
     * načítání obsahu webové stránky.
     */
    @FXML
    private Label progress;
    /**
     * Instance třídy WebLoader pro načítání obsahu webové stránky.
     * <p>
     * Tato proměnná je použita k inicializaci a spuštění procesu načítání URL adres
     * z webové stránky s dopravními značkami.
     */
    private WebLoader webLoader;
    /**
     * URL adresa webové stránky s databází dopravních značek.
     * <p>
     * Tato proměnná obsahuje URL adresu, která je použita pro načítání obsahu
     * webové stránky s dopravními značkami.
     */
    private final String url = "http://www.celysvet.cz/test-znalosti-dopravnich-znacek-databaze";

    /**
     * Prázdný konstruktor třídy ZnackyController.
     * <p>
     * Tento konstruktor nevykonává žádné akce a je zde pouze pro případné budoucí rozšíření.
     */
    public ZnackyController() {
        // Prázdný konstruktor
    }

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

    /**
     * Metoda pro zpracování události kliknutí na tlačítko pro výběr složky.
     * <p>
     * Tato metoda otevře dialogové okno pro výběr složky a nastaví výchozí složku
     * na základě hodnoty uložené v souboru settings.ini. Pokud není výchozí složka
     * nastavena, použije se uživatelská složka.
     * <p>
     * Po výběru složky se její cesta uloží do souboru settings.ini.
     */
    @FXML
    protected void onSelectFolderButtonClick() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Vyberte složku");

        // Načtení výchozí složky ze souboru settings.ini
        File defaultDirectory = loadDefaultDirectory();
        if (defaultDirectory != null && defaultDirectory.exists()) {
            directoryChooser.setInitialDirectory(defaultDirectory);
        } else {
            // Nastavení výchozí složky na uživatelskou složku
            File userDirectory = new File(System.getProperty("user.home"));
            if (userDirectory.exists()) {
                directoryChooser.setInitialDirectory(userDirectory);
            }
        }

        // Získání reference na hlavní okno aplikace
        Stage primaryStage = (Stage) welcomeText.getScene().getWindow();

        // Zobrazení dialogového okna pro výběr složky
        File selectedDirectory = directoryChooser.showDialog(primaryStage);

        if (selectedDirectory != null) {
            // Zpracování vybrané složky (např. zobrazení cesty v konzoli)
            System.out.println("Vybraná složka: " + selectedDirectory.getAbsolutePath());
            // Uložení vybrané složky do souboru settings.ini
            saveDefaultDirectory(selectedDirectory);
        }
    }

    /**
     * Načte výchozí složku z konfiguračního souboru settings.ini.
     * <p>
     * Tato metoda načte hodnotu klíče defaultDirectory ze souboru settings.ini
     * a vrátí ji jako instanci třídy File. Pokud soubor settings.ini neexistuje
     * nebo klíč defaultDirectory není nastaven, vrátí null.
     *
     * @return Výchozí složka jako instance třídy File nebo null, pokud není nastavena.
     */
    private File loadDefaultDirectory() {
        Properties properties = new Properties();
        File settingsFile = new File(SETTINGS_FILE);

        // Zkontrolovat, zda soubor existuje
        if (!settingsFile.exists()) {
            return null;
        }

        try (InputStream input = new FileInputStream(settingsFile)) {
            properties.load(input);
            String directoryPath = properties.getProperty(DIRECTORY_KEY);
            if (directoryPath != null) {
                return new File(directoryPath);
            }
        } catch (IOException e) {
            logger.error("Chyba při načítání souboru settings.ini {}", e.getMessage());
        }
        return null;
    }

    /**
     * Uloží výchozí složku do konfiguračního souboru settings.ini.
     * <p>
     * Tato metoda uloží cestu k zadané složce jako hodnotu klíče defaultDirectory
     * do souboru settings.ini.
     *
     * @param directory Složka, která má být uložena jako výchozí.
     */
    private void saveDefaultDirectory(File directory) {
        Properties properties = new Properties();
        properties.setProperty(DIRECTORY_KEY, directory.getAbsolutePath());
        try (OutputStream output = new FileOutputStream(SETTINGS_FILE)) {
            properties.store(output, null);
        } catch (IOException e) {
            logger.error("Chyba při ukládání souboru settings.ini {}", e.getMessage());
        }
    }

    /**
     * Inicializuje komponenty uživatelského rozhraní.
     * <p>
     * Tato metoda je volána automaticky po načtení FXML souboru a slouží k nastavení
     * vlastností a chování komponent uživatelského rozhraní.
     * <p>
     * V tomto případě nastavuje továrnu buněk pro ListView na instanci CustomListCell.
     */
    @FXML
    public void initialize() {
        listView.setCellFactory(param -> new CustomListCell());
    }
}