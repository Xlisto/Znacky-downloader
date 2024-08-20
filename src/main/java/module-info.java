/**
 * Modul cz.xlisto.znacky je hlavním modulem aplikace pro stahování obrázků dopravních značek.
 * <p>
 * Tento modul vyžaduje následující moduly:
 * <ul>
 *   <li>javafx.controls - pro základní ovládací prvky JavaFX.</li>
 *   <li>javafx.fxml - pro načítání FXML souborů.</li>
 *   <li>org.jsoup - pro práci s HTML a stahování obsahu z webu.</li>
 *   <li>java.desktop - pro integraci s desktopovými funkcemi.</li>
 *   <li>ch.qos.logback.classic - pro logování aplikace.</li>
 *   <li>org.slf4j - pro rozhraní logování.</li>
 * </ul>
 * <p>
 * Modul také otevírá balíček `cz.xlisto.znacky` pro modul javafx.fxml a exportuje tento balíček.
 */
module cz.xlisto.znacky {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.jsoup;
    requires java.desktop;
    requires ch.qos.logback.classic;
    requires org.slf4j;


    opens cz.xlisto.znacky to javafx.fxml;
    exports cz.xlisto.znacky;
}