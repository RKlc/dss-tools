package sk.isdd.validator.fx;

import javafx.util.StringConverter;
import sk.isdd.validator.model.XmlFile;

/**
 * Helper for File name conversion to string.
 *
 * <p> Used for bidirectional bindings between File object and visual FX controls (like buttons).
 * Supports default string message if File is not selected.
 */
public class XmlFileToStringConverter extends StringConverter<XmlFile> {

    @Override
    public String toString(XmlFile object) {
        if (object == null) {
            return I18nMsg.getString("emptySelectFile");
        }
        return object.getAbsolutePath();
    }

    @Override
    public XmlFile fromString(String string) {
        return null;
    }

}
