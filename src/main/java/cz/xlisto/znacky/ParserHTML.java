package cz.xlisto.znacky;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Třída ParserHTML slouží k parsování HTML obsahu a extrahování URL adres z &lt;img&gt; tagů.
 * <p>
 * Tato třída poskytuje metodu pro parsování HTML obsahu a získání seznamu URL adres
 * nalezených v &lt;img&gt; tagách. Dále umožňuje zjistit, zda existuje další stránka k načtení.
 * <p>
 * Hlavní funkce třídy:
 * <ul>
 *   <li>Parsování HTML obsahu pomocí metody parseHTML.</li>
 *   <li>Získání seznamu URL adres nalezených v &lt;img&gt; tagách pomocí metody getUrlList.</li>
 *   <li>Aktualizace průběhu načítání voláním metody updateProgress na instanci ZnackyController.</li>
 *   <li>Vyhledání odkazu na další stránku pomocí textu "další :".</li>
 * </ul>
 * <p>
 * Třída využívá následující knihovny:
 * <ul>
 *   <li>org.jsoup.Jsoup pro parsování HTML obsahu.</li>
 *   <li>javafx.collections.FXCollections a javafx.collections.ObservableList pro práci se seznamem URL adres.</li>
 * </ul>
 */
public class ParserHTML {
    private final ZnackyController znackyController;

    // Seznam všech nalezených URL adres
    private final ObservableList<String> urlList = FXCollections.observableArrayList();
    /**
     * Konstruktor třídy ParserHTML.
     * <p>
     * Inicializuje instanci třídy ParserHTML s referencí na instanci ZnackyController.
     * Tato reference je použita pro aktualizaci průběhu načítání během parsování HTML obsahu.
     *
     * @param znackyController instance třídy ZnackyController, která je použita pro aktualizaci průběhu načítání.
     */
    public ParserHTML(ZnackyController znackyController) {
        this.znackyController = znackyController;
    }

    // Základní URL
    String baseUrl = "http://www.celysvet.cz/";

    /**
     * Metoda pro parsování HTML obsahu a extrahování URL adres z &lt;img&gt; tagů.
     *
     * @param html HTML obsah, který má být parsován.
     * @return URL adresa další stránky k načtení, pokud existuje, jinak null.
     * <p>
     * Metoda provede následující kroky:
     * <ol>
     *   <li>Parsování HTML obsahu pomocí knihovny Jsoup.</li>
     *   <li>Výběr všech &lt;img&gt; tagů a extrahování hodnot atributu src.</li>
     *   <li>Přidání nalezených URL adres do seznamu urlList.</li>
     *   <li>Aktualizace průběhu načítání voláním metody updateProgress na instanci ZnackyController.</li>
     *   <li>Vyhledání odkazu na další stránku pomocí textu "další :".</li>
     *   <li>Pokud je nalezen odkaz na další stránku, vrátí jeho URL adresu, jinak vrátí null.</li>
     * </ol>
     */
    public String parseHTML(String html) {
        // Parsování HTML obsahu
        Document doc = Jsoup.parse(html);

        // Výběr všech <img> tagů
        Elements imgTags = doc.select("img");

        // Přidání všech URL do ObservableList
        for (Element imgTag : imgTags) {
            urlList.add(imgTag.attr("src"));
            // Aktualizace průběhu
            znackyController.updateProgress();
        }

        // Najde odkaz s textem "další :", pokud je, existuje další stránka
        Element nextLink = doc.selectFirst("a:contains(další :)");
        if (nextLink != null) {
            // Získání hodnoty atributu href a nahrazení ampersand entity
            String href = nextLink.attr("href").replace("&amp;", "&");

            // Odstranění nepotřebných částí URL (././)
            if (href.startsWith("././")) {
                href = href.substring(4);  // Odstraní "././" z začátku řetězce
            }

            // Kombinace základní URL a upravené cesty
            return baseUrl + href;
        } else {

            return null;
        }
    }


    /**
     * Metoda pro získání seznamu URL adres nalezených v &lt;img&gt; tagách.
     *
     * @return ObservableList obsahující URL adresy nalezené v &lt;img&gt; tagách.
     */
    public ObservableList<String> getUrlList() {
        return urlList;
    }
}
