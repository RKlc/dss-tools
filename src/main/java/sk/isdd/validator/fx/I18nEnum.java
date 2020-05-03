package sk.isdd.validator.fx;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Supports localization of enums via dedicated resource bundle.
 */
public class I18nEnum {

    /**
     * Resource bundle for enumerations.
     */
    private static ResourceBundle lang;

    /**
     * Translates named enumeration or returns it back untranslated
     *
     * @param text the enum codename or any text to be translated
     * @return translation text or text itself
     */
    public static String getString(String text) {

        if (lang == null) {
            lang = ResourceBundle.getBundle("lang.enumerations", Locale.getDefault());
        }

        if (lang.containsKey(text)) {
            return lang.getString(text);
        }

        return text;
    }
}
