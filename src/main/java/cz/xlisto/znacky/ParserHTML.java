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
    /**
     * Reference na instanci třídy ZnackyController, která je použita pro aktualizaci průběhu načítání.
     */
    private final ZnackyController znackyController;

    /**
     * Seznam všech nalezených URL adres.
     * <p>
     * Tato proměnná obsahuje seznam URL adres, které byly nalezeny v &lt;img&gt; tagách
     * během parsování HTML obsahu. Seznam je implementován jako ObservableList, což
     * umožňuje sledování změn v seznamu.
     */
    private final ObservableList<String[]> urlList = FXCollections.observableArrayList();

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

    /**
     * Základní URL pro načítání stránek.
     * <p>
     * Tato proměnná obsahuje základní URL adresu, která je použita jako výchozí bod
     * pro kombinaci s relativními cestami nalezenými během parsování HTML obsahu.
     */
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
        // Pro každý <img> tag v seznamu imgTags:
        // 1. Získá hodnotu atributu "alt" a odstraní z ní text "Dopravní značka:", poté ořízne bílé znaky.
        // 2. Přidá do seznamu urlList nový řetězec obsahující formátovaný popis a URL adresu obrázku s nahrazením "low" za "hi".
        // 3. Aktualizuje průběh načítání voláním metody updateProgress na instanci znackyController.

        for (Element imgTag : imgTags) {
            String description = imgTag.attr("alt").replace("Dopravní značka:", "").trim();
            urlList.add(new String[]{formatText(description), imgTag.attr("src").replace("low", "hi")});
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
     * Formátuje vstupní text odstraněním mezer z části kódu a kombinací s popisem.
     *
     * @param input vstupní řetězec obsahující kód a popis oddělené mezerou
     * @return formátovaný řetězec s kombinovaným kódem a popisem
     */
    public static String formatText(String input) {
        // Rozdělení vstupního řetězce na kód a popis
        String[] parts = input.split(" ", 2);
        String code = parts[0].replace(" ", "");
        String description = parts[1];

        // Kombinace formátovaného kódu a popisu
        return code + description;
    }


    /**
     * Metoda pro získání seznamu URL adres nalezených v &lt;img&gt; tagách.
     *
     * @return ObservableList obsahující URL adresy nalezené v &lt;img&gt; tagách.
     */
    public ObservableList<String[]> getUrlList() {
        return urlList;
    }
}
