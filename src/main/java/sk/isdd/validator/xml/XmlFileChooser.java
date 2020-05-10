package sk.isdd.validator.xml;

import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.commons.io.FilenameUtils;
import sk.isdd.validator.fx.I18nMsg;

import java.io.File;


/**
 * FileChooser extension
 */
public class XmlFileChooser {

    /**
     * Used to reopen dialog box at the last open file.
     */
    File lastOpenedDir;

    /**
     * Call customized file open dialog and internally remember last open file.
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

    /**
     * Call customized file save dialog. Internally using last open dir.
     *
     * @param stage the FX stage where to output dialog box
     * @param file preset filename if not null
     * @param suffix distinguishable string for preset filename
     * @return the selected file or null if no file has been selected
     */
    public File showXmlSaveDialog(Stage stage, File file, String suffix) {

        // setup new fileChooser
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(I18nMsg.getString("titleSaveAsFile"));

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

        // construct default filename for user's convenience
        if (file != null) {

            String filename = FilenameUtils.getBaseName(file.getAbsolutePath()) +
                    "." + suffix + "." +
                    FilenameUtils.getExtension(file.getAbsolutePath());

            fileChooser.setInitialFileName(filename);
        }

        // show dialog and return selected file
        return fileChooser.showSaveDialog(stage);
    }
}
