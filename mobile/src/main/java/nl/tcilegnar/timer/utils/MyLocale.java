package nl.tcilegnar.timer.utils;

import java.util.Locale;

public class MyLocale {
    /** Locale.getDefault() is different: eg. 1st day of the week is Monday (Dutch) instead of Sunday (English) */
    private static final Locale LOCALE = new Locale("nl", "NL");

    public static Locale getLocale() {
        return LOCALE;
    }

    /**
     * Dit is de locale die gebruikt moet worden als dmv een SimpleDateFormat een tekst moet worden gegenereerd, of
     * wanneer je valuta of andere LOCALE afhankelijke symbolen gaat gebruiken. Aangezien de default Locale runtime zou
     * kunnen veranderen wordt deze niet als constante gedefinieerd
     */
    public static Locale getLocaleForTranslationAndSigns() {
        return Locale.getDefault();
    }
}
