package cz.xlisto.znacky;


import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Třída CustomListCell rozšiřuje ListCell pro zobrazení pole řetězců.
 * <p>
 * Tato třída je určena pro použití v ListView a umožňuje přizpůsobit vzhled a obsah jednotlivých buněk.
 */
public class CustomListCell extends ListCell<String[]> {
    /**
     * Logger pro zaznamenávání chyb a informací během načítání webových stránek.
     * <p>
     * Tento logger je inicializován pomocí třídy LoggerFactory a je použit pro logování
     * událostí a chyb v rámci třídy CustomListCell.
     */
    private static final Logger logger = LoggerFactory.getLogger(CustomListCell.class);

    /**
     * VBox obsahující obsah buňky ListView.
     * <p>
     * Tato proměnná je inicializována v konstruktoru třídy CustomListCell
     * načtením FXML souboru a slouží k zobrazení obsahu buňky.
     */
    private VBox content;

    /**
     * Konstruktor třídy CustomListCell.
     * <p>
     * Tento konstruktor načítá FXML soubor `list_item.fxml` a nastavuje kontroler na aktuální instanci.
     * Inicializuje proměnnou `content` načtením obsahu FXML souboru.
     * V případě chyby při načítání FXML souboru je vyhozena výjimka IOException a její stack trace je vypsán.
     */
    public CustomListCell() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("list_item.fxml"));
            loader.setController(this);
            content = loader.load();
        } catch (IOException e) {
            logger.error("Chyba při načítání FXML souboru", e);
        }
    }

    /**
     * Aktualizuje obsah buňky ListView.
     * <p>
     * Tato metoda je volána při každé změně obsahu buňky. Pokud je buňka prázdná nebo je položka null,
     * nastaví text a grafiku buňky na null. Jinak nastaví texty labelů `text1` a `text2` na hodnoty
     * z pole `item` a nastaví grafiku buňky na obsah VBoxu `content`.
     *
     * @param item  Pole řetězců obsahující data pro buňku.
     * @param empty Boolean hodnota indikující, zda je buňka prázdná.
     */
    @Override
    protected void updateItem(String[] item, boolean empty) {
        super.updateItem(item, empty);
        Label text1 = (Label) content.lookup("#text1");
        Label text2 = (Label) content.lookup("#text2");
        if (empty || item == null) {
            setText(null);
            setGraphic(null);
        } else {
            text1.setText(item[0]);
            text2.setText(item[1]);
            setGraphic(content);
        }
    }
}