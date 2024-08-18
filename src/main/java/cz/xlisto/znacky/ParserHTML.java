package cz.xlisto.znacky;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ParserHTML {
    // Základní URL
    String baseUrl = "http://www.celysvet.cz/";

    // ObservableList všech nalezených URL
    private final ObservableList<String> urlList = FXCollections.observableArrayList();

    public String parseHTML(String html) {

        // Parsování HTML obsahu
        Document doc = Jsoup.parse(html);

        // Výběr všech <img> tagů
        Elements imgTags = doc.select("img");

        // Přidání všech URL do ObservableList
        for (Element imgTag : imgTags) {
            urlList.add(imgTag.attr("src"));
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


    // Vrátí ObservableList URL adres
    public ObservableList<String> getUrlList() {
        return urlList;
    }
}
