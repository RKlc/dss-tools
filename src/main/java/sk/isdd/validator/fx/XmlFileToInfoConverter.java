package sk.isdd.validator.fx;

import javafx.util.StringConverter;
import sk.isdd.validator.model.XmlFile;

/**
 * Helper to convert selected information about state of XmlFile into string.
 *
 * <p> Used for bidirectional bindings between XmlFile object and visual FX controls.
 */
public class XmlFileToInfoConverter extends StringConverter<XmlFile> {

    @Override
    public String toString(XmlFile object) {
        if (object == null) {
            return I18nMsg.getString("xmlInfoFileNotSelected");
        }

        if (!object.isReadableFile()) {
            return I18nMsg.getString("xmlInfoCannotRead");
        }

        StringBuilder message = new StringBuilder();

        if (object.isXmlDocument()) {
            message.append(I18nMsg.getString("xmlInfoIsXmlDocument"));
        } else {
            message.append(I18nMsg.getString("xmlInfoIsCommonFile"));
        }

        return message.toString();
    }

    @Override
    public XmlFile fromString(String string) {
        return null;
    }

}
