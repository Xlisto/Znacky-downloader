package cz.xlisto.znacky;

import javafx.collections.ObservableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class WebLoader {
    private static final Logger logger = LoggerFactory.getLogger(WebLoader.class);
    // Vytvoření objektu třídy ParserHTML
    private final ParserHTML parser = new ParserHTML();

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

    // Získá ObservableList URL adres z parseru
    public ObservableList<String> getUrlList() {
        return parser.getUrlList();
    }
}
