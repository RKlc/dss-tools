package sk.isdd.validator.xml;

import javafx.stage.FileChooser;
import javafx.stage.Stage;
import sk.isdd.validator.fx.I18nMsg;

import java.io.File;

public class XmlFileChooser {

    /**
     * Used to reopen dialog box at the last open file.
     */
    File lastOpenedDir;

    /**
     * Calls customized file open dialog and internally remembers last open file.
     *
     * @param stage the FX stage where to output dialog box
     * @return returns new instance of the xml file
     */
    public XmlFile showXmlOpenDialog(Stage stage) {

        // setup new fileChooser
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(I18nMsg.getString("titleSelectSourceFile"));

        // add XML extension filters
        fileChooser.getExtensionFilters().setAll(
                new FileChooser.ExtensionFilter(
                        "XML files (*.xml, *.xsl, *.xsd, *.xslt, *.wsdl, *.xlf, *.xliff)",
                        "*.xml", "*.xsl", "*.xsd", "*.xslt", "*.wsdl", "*.xlf", "*.xliff"),
                new FileChooser.ExtensionFilter("All files (*.*)", "*.*"));

        // reset dir to last used path
        if (lastOpenedDir != null) {
            fileChooser.setInitialDirectory(lastOpenedDir);
        }

        // open customized dialog box
        File file = fileChooser.showOpenDialog(stage);
        if (file == null) {
            return null;
        }

        // remember dir for later
        lastOpenedDir = file.getParentFile();
        return new XmlFile(file);
    }
}
