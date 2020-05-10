package sk.isdd.validator.xml;

import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.commons.io.FilenameUtils;
import sk.isdd.validator.fx.I18nMsg;

import java.io.File;


/**
 * FileChooser helper. Contains pre-configured ready to use dialogs for opening and saving file.
 *
 * <p> Original FileChooser cannot be extended.
 *
 * TODO: Extension filters should be localized or configurable.
 */
public class XmlFileChooser {

    /**
     * Used to reopen dialog box at the last open file.
     */
    File lastOpenedDir;

    /**
     * Used to reopen dialog box at the last saved file.
     */
    File lastSavedDir;

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

        // custom XML extension filters
        fileChooser.getExtensionFilters().setAll(
                new FileChooser.ExtensionFilter(
                        "XML files (*.xml, *.xsl, *.xsd, *.xslt, *.wsdl, *.xlf, *.xliff)",
                        "*.xml", "*.xsl", "*.xsd", "*.xslt", "*.wsdl", "*.xlf", "*.xliff"),
                new FileChooser.ExtensionFilter("All files (*.*)", "*.*"));

        // initialize dir to last used path
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
     * @param fileRef referential file where from filename base and suffix will be preset
     * @param suffix distinguishable string for preset filename
     * @return the selected file or null if no file has been selected
     */
    public File showXmlSaveDialog(Stage stage, File fileRef, String suffix) {

        // setup new fileChooser
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(I18nMsg.getString("titleSaveAsFile"));

        // add XML extension filters
        fileChooser.getExtensionFilters().setAll(
                new FileChooser.ExtensionFilter(
                        "XML files (*.xml, *.xsl, *.xsd, *.xslt, *.wsdl, *.xlf, *.xliff)",
                        "*.xml", "*.xsl", "*.xsd", "*.xslt", "*.wsdl", "*.xlf", "*.xliff"),
                new FileChooser.ExtensionFilter("All files (*.*)", "*.*"));

        // initialize dir to last used path
        if (lastSavedDir != null) {
            fileChooser.setInitialDirectory(lastSavedDir);

        } else if (lastOpenedDir != null) {
            fileChooser.setInitialDirectory(lastOpenedDir);
        }

        // construct default filename for user's convenience
        if (fileRef != null) {

            String filename = FilenameUtils.getBaseName(fileRef.getAbsolutePath()) +
                    "." + suffix + "." +
                    FilenameUtils.getExtension(fileRef.getAbsolutePath());

            fileChooser.setInitialFileName(filename);
        }

        File fileSave = fileChooser.showSaveDialog(stage);

        // remember dir for later
        if (fileSave != null) {
            lastSavedDir = fileSave.getParentFile();
        }

        // show dialog and return selected file
        return fileSave;
    }
}
