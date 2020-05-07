package sk.isdd.validator.xml;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import sk.isdd.validator.ValidatorApplication;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.net.URI;
import java.text.DecimalFormat;

/**
 * Extending File with basic XML functionality and XML content.
 */
public class XmlFile extends File {

    private static final Logger LOG = LoggerFactory.getLogger(XmlFile.class);

    /**
     * Document parsed from file (org.w3c.dom.Document).
     */
    Document xmlDocument = null;

    /**
     * Custom constructor supports initialization directly from File
     *
     * @param file the file where the pathname will be extracted from
     */
    public XmlFile(File file)
    {
        super((file == null)? "" : file.getAbsolutePath());
    }

    /**
     * All required constructors call super()
     */
    public XmlFile(String pathname) {
        super(pathname);
    }
    public XmlFile(String parent, String child) {
        super(parent, child);
    }
    public XmlFile(File parent, String child) {
        super(parent, child);
    }
    public XmlFile(URI uri) {
        super(uri);
    }

    /**
     * Reads and parses current file into DOM document.
     *
     * @return the DOM Document parsed from current file or null, no exception is thrown
     */
    public Document parseXml() {

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db;

        try {
            db = dbf.newDocumentBuilder();
            xmlDocument = db.parse(this);

        } catch (Throwable t) {
            LOG.debug("XML parser stopped: " + t.getMessage(), t);
            xmlDocument = null;
        }

        return xmlDocument;
    }

    /**
     * Test if file is normal file and has reading permissions.
     *
     * @return <code>true</code> if file is readable
     */
    public boolean isReadableFile() {

        try {
            if (this.isFile() && this.canRead()) {
                return true;
            }
        } catch (Exception e) {
            LOG.debug("File is not readable: " + e.getMessage(), e);
        }

        return false;
    }

    /**
     * Test if file is XML document. If true, document is internally parsed and can
     * be retrieved by {@code getXMLDocument()}.
     *
     * @return TRUE if document is readable file and parses as XML, otherwise FALSE
     */
    public boolean isXmlDocument() {

        return (isReadableFile() && (parseXml() != null));
    }

    /**
     * Getter of internally parsed XML document. File needs to be parsed by {@code parseXml()} beforehand.
     *
     * @return the DOM Document parsed from current file or null, no exception is thrown
     */
    public Document getXmlDocument() {
        return xmlDocument;
    }

    /**
     * Returns the length of file in human readable format including appropriate
     * unit (kB, MB, etc) depending on its size.
     *
     * @return formatted file size including unit
     */
    public String humanFileSize() {

        if (length() <= 0) {
            return "0 B";
        }
        final String[] units = new String[] { "B", "kB", "MB", "GB", "TB", "PB"};
        int digitGroups = (int) (Math.log10(length()) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(length() / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }

}