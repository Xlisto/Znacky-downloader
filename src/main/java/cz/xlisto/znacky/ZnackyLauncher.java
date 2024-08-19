package cz.xlisto.znacky;

/**
 * Třída ZnackyLauncher slouží jako spouštěcí třída pro aplikaci ZnackyApplication.
 * <p>
 * Tato třída obsahuje hlavní metodu, která volá hlavní metodu třídy ZnackyApplication.
 * Je užitečná pro oddělení spouštěcí logiky od hlavní aplikační třídy.
 */
public class ZnackyLauncher {
    /**
     * Prázdný konstruktor třídy ZnackyLauncher.
     * <p>
     * Tento konstruktor nevykonává žádné akce a je zde pouze pro případné budoucí rozšíření.
     */
    public ZnackyLauncher() {
        // Prázdný konstruktor
    }

    /**
     * Hlavní metoda aplikace.
     * <p>
     * Tato metoda slouží jako vstupní bod pro spuštění aplikace ZnackyApplication.
     * Volá hlavní metodu třídy ZnackyApplication s argumenty příkazového řádku.
     *
     * @param args argumenty příkazového řádku (nejsou použity).
     */
    public static void main(String[] args) {
        ZnackyApplication.main(args);
    }


}
