package sk.isdd.validator.xml;

import javafx.util.StringConverter;
import sk.isdd.validator.fx.I18nMsg;

/**
 * Helper to convert selected information about state of XmlFile into string.
 *
 * <p> Used for bidirectional bindings between XmlFile object and visual FX controls.
 */
public class XmlFileToInfoConverter extends StringConverter<XmlFile> {

    /**
     * Include caution warning for large files
     */
    final long CAUTION_TRESHOLD = 10*1024*1024;

    @Override
    public String toString(XmlFile object) {

        // no file
        if (object == null) {
            return I18nMsg.getString("xmlInfoFileNotSelected");
        }

        // unreadable
        if (!object.isReadableFile()) {
            return I18nMsg.getString("xmlInfoCannotRead");
        }

        StringBuilder message = new StringBuilder();

        // include file type
        if (object.isXmlDocument()) {
            message.append(I18nMsg.getString("xmlInfoIsXmlDocument"));
        } else {
            message.append(I18nMsg.getString("xmlInfoIsCommonFile"));
        }

        // append file size
        message.append(" (").append(object.readableFileSize());

        // include warning for large files
        if (object.length() > CAUTION_TRESHOLD) {
            message.append(" - ").append(I18nMsg.getString("xmlInfoCautionTreshold"));
        }

        return message.append(")").toString();
    }

    @Override
    public XmlFile fromString(String string) {
        return null;
    }

}
