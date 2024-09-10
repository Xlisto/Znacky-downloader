package cz.xlisto.znacky;

import javafx.collections.ObservableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * Třída ImageSaver poskytuje funkce pro ukládání obrázků z daných URL
 * do výchozí složky specifikované v konfiguračním souboru.
 */
public class ImageSaver {
    private static final String SETTINGS_FILE = "settings.ini";
    private static final String DIRECTORY_KEY = "defaultDirectory";
    private static final Logger logger = LoggerFactory.getLogger(ImageSaver.class);

    /**
     * Načte výchozí složku z konfiguračního souboru settings.ini.
     *
     * @return Výchozí složka jako instance třídy File nebo null, pokud není nastavena.
     */
    private File loadDefaultDirectory() {
        Properties properties = new Properties();
        File settingsFile = new File(SETTINGS_FILE);

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
     * Uloží obrázky z daných URL do výchozí složky.
     *
     * @param urlList Seznam URL odkazů pro stažení obrázků.
     */
    public void saveImages(ObservableList<String[]> urlList) {
        File directory = loadDefaultDirectory();
        if (directory == null || !directory.exists()) {
            logger.error("Výchozí složka není nastavena nebo neexistuje.");
            return;
        }

        for (String[] urlArray : urlList) {
            String urlString = urlArray[1];
            String encodedURL;
            encodedURL = URLEncoder.encode(urlString, StandardCharsets.UTF_8);

            // Vrátí znaky, které mají v URL zůstat nezakódované (např. ':', '/', 'http')
            encodedURL = encodedURL.replace("%3A", ":").replace("%2F", "/");

            try {
                URI uri = new URI(encodedURL);
                URL url = uri.toURL();
                String fileName = Paths.get(url.getPath().replace("%3Ca-hre-", "")).getFileName().toString(); // + odstranění specifické chyby u zastávky tramvaje
                File outputFile = new File(directory, fileName);

                try (InputStream in = url.openStream()) {
                    Files.copy(in, outputFile.toPath());
                    logger.info("Obrázek uložen: {}", outputFile.getAbsolutePath());
                }
            } catch (IOException e) {
                logger.error("Chyba při ukládání obrázku z URL: {}", urlArray[1], e);
            } catch (URISyntaxException e) {
                logger.error("Chyba při sestavení URL: {}", urlArray[1], e);
            }
        }
    }
}