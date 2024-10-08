package cz.xlisto.znacky;

import javafx.collections.ObservableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Třída WebLoader slouží k načítání obsahu webových stránek z daných URL adres.
 * <p>
 * Tato třída poskytuje metody pro načtení obsahu webové stránky a získání seznamu URL adres
 * nalezených parserem. Používá knihovny pro zpracování HTTP požadavků a logování.
 * <p>
 * Hlavní funkce třídy:
 * <ul>
 *   <li>Načítání obsahu webové stránky z dané URL pomocí metody loadWeb.</li>
 *   <li>Získání seznamu URL adres nalezených parserem pomocí metody getUrlList.</li>
 * </ul>
 * <p>
 * Třída využívá následující knihovny:
 * <ul>
 *   <li>java.net.HttpURLConnection pro zpracování HTTP požadavků.</li>
 *   <li>org.slf4j.Logger pro logování chyb a informací.</li>
 *   <li>javafx.collections.ObservableList pro práci se seznamem URL adres.</li>
 * </ul>
 */
public class WebLoader {
    /**
     * Logger pro zaznamenávání chyb a informací během načítání webových stránek.
     * <p>
     * Tento logger je inicializován pomocí třídy LoggerFactory a je použit pro logování
     * událostí a chyb v rámci třídy WebLoader.
     */
    private static final Logger logger = LoggerFactory.getLogger(WebLoader.class);
    /**
     * Instance třídy ParserHTML pro parsování HTML obsahu.
     * <p>
     * Tato proměnná je inicializována v konstruktoru třídy WebLoader a je použita
     * pro parsování HTML obsahu načteného z webových stránek.
     */
    private final ParserHTML parser;

    /**
     * Konstruktor třídy WebLoader.
     * <p>
     * Inicializuje instanci třídy WebLoader s referencí na instanci ZnackyController.
     * Tato reference je použita pro vytvoření instance ParserHTML, která je zodpovědná za parsování HTML obsahu.
     *
     * @param znackyController instance třídy ZnackyController, která je použita pro aktualizaci průběhu načítání.
     */
    public WebLoader(ZnackyController znackyController) {
        this.parser = new ParserHTML(znackyController);
    }

    /**
     * Metoda pro načtení obsahu webové stránky z dané URL.
     * <p>
     * Tato metoda provede následující kroky:
     * <ol>
     *   <li>Vytvoří objekt URL a otevře spojení.</li>
     *   <li>Nastaví metodu požadavku na GET.</li>
     *   <li>Načte odpověď z webové stránky pomocí BufferedReader.</li>
     *   <li>Obsah stránky uloží do StringBuilderu.</li>
     *   <li>Zavolá metodu parseHTML třídy ParserHTML s načteným obsahem stránky.</li>
     *   <li>Pokud metoda parseHTML vrátí další URL, rekurzivně zavolá loadWeb s touto URL.</li>
     *   <li>V případě chyby při načítání webu zapíše chybu do logu.</li>
     * </ol>
     *
     * @param url URL adresa webové stránky, kterou chceme načíst.
     */
    public void loadWeb(String url) {
        try {
            // Vytvoření objektu URL a otevření spojení
            URL obj = new URI(url).toURL();
            HttpURLConnection connection = (HttpURLConnection) obj.openConnection();

            // Nastavení metody požadavku (GET nebo POST)
            connection.setRequestMethod("GET");

            // Načtení odpovědi z webové stránky
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // Zavolání metody parseHTML s načteným obsahem stránky
            String nextUrl = parser.parseHTML(response.toString());
            if (nextUrl != null) {
                loadWeb(nextUrl);
            }


        } catch (Exception e) {
            logger.error("Chyba při načítání webu: {}", e.getMessage());
        }

    }

    /**
     * Metoda pro získání seznamu URL adres z parseru.
     *
     * @return ObservableList obsahující URL adresy, které byly nalezeny parserem.
     */
    public ObservableList<String[]> getUrlList() {
        return parser.getUrlList();
    }
}
