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