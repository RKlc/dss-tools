package sk.isdd.validator.xml;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import org.apache.commons.io.FileUtils;
import org.apache.xml.security.c14n.CanonicalizationException;
import org.apache.xml.security.c14n.Canonicalizer;
import org.apache.xml.security.utils.JavaUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import sk.isdd.validator.enumerations.XmlC14nMethod;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.text.DecimalFormat;

/**
 * Extending {@code File} with basic XML loading and parsing ability.
 *
 * <p>Internal states of file reading and xml parsing is remembered.
 * These states are not reversible. To reread and reparse file, new object needs to be created.
 *
 * TODO: I/O streaming feature: To support large files, contents of the whole file should not be retained within arrays.
 */
public class XmlFile extends File {

    private static final Logger LOG = LoggerFactory.getLogger(XmlFile.class);

    /**
     * Raw byte content of the whole file
     */
    byte[] rawBytes = null;

    /**
     * Document parsed from file (org.w3c.dom.Document).
     */
    Document xmlDocument = null;

    /**
     * Remembers if file loading failed. If true, it is irreversibly failed.
     */
    boolean isReadingFailed = false;

    /**
     * Remembers if parsing failed. If true, it is irreversibly failed.
     */
    boolean isParsingFailed = false;

    /**
     * Cached bytes of transformed source file (transformation depends on the method).
     */
    private byte[] transformedBytes = null;

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
     * Load content of the file to internal field and return its byte array.
     *
     * <p>Byte array is cached within the instance of this object.
     * Each method is using this internal array of bytes for future processing.
     * It will load only once. If it failed once, it will always fail.
     *
     * @return the array of file bytes or null if unable to read content of the file.
     */
    public byte[] readFile() {

        // file cannot be read, if failed once
        if (isReadingFailed) {
            return null;
        }

        // file was already read, return it
        if (rawBytes != null) {
            return rawBytes;
        }

        try {
            rawBytes = JavaUtils.getBytesFromFile(getAbsolutePath());

        } catch (IOException e) {
            LOG.warn("Could not read content of the file \"" + getAbsolutePath() + "\": " + e.getMessage(), e);
            isReadingFailed = true;
            return null;
        }

        LOG.info("File \"" + getAbsolutePath() +"\" was loaded successfully.");
        return rawBytes;
    }

    /**
     * Save transformed output to chosen file.
     *
     * @param file the file where to save output
     * @return true if file was saved, false otherwise
     */
    public boolean saveTransformedFile(File file) {

        if (file == null) {
            LOG.error("Unable to save transformation to unknown file.");
            return false;
        }

        if (transformedBytes == null) {
            LOG.error("Transformed data not found.");
            return false;
        }

        try {
            FileUtils.writeByteArrayToFile(file, transformedBytes);

        } catch (IOException e) {
            LOG.error("Unable to save transformation to file \"" + file.getAbsolutePath() + "\": " + e.getMessage());
            return false;
        }

        return true;
    }

    /**
     * Parse loaded file into DOM document once.
     *
     * On subsequent calls just return already parsed xml document. All exceptions are suppressed.
     * It will parse only once. If parsing failed once, it will always fail.
     *
     * @return valid DOM document parsed from current file or null for any other possible reason, no exception is thrown
     */
    public Document parseXml() {

        // document cannot be parsed, if failed once
        if (isParsingFailed) {
            return null;
        }

        // document was already parsed, return it
        if (xmlDocument != null) {
            return xmlDocument;
        };

        // read file or fail
        if (readFile() == null) {
            isParsingFailed = true;
            return null;
        };

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;

        try {
            builder = factory.newDocumentBuilder();
            xmlDocument = builder.parse(new ByteArrayInputStream(rawBytes));

        } catch (Throwable t) {
            LOG.warn("XML parser failed: " + t.getMessage());
            isParsingFailed = true;
            return null;
        }

        LOG.info("Content of file was parsed to XML document successfully.");
        return xmlDocument;
    }

    /**
     * Perform canonicalization on internally loaded file.
     *
     * @param method Canonicalization method.
     * @return Canonical output (should be well formed XML) or null
     */
    public byte[] canonicalize(XmlC14nMethod method) {

        // read the file if not already loaded
        if (readFile() == null) {
            return null;
        }

        // return without transformation
        if (method == null || method == XmlC14nMethod.C14N_NONE) {
            transformedBytes = rawBytes;
            return rawBytes;
        }

        try {
            org.apache.xml.security.Init.init();
            Canonicalizer c14n = Canonicalizer.getInstance(method.getUri());
            transformedBytes = c14n.canonicalize(rawBytes);
            return transformedBytes;

        } catch (Exception e) {
            LOG.error("Cannot canonicalize the source file; Transformation \"" + method.getText() + "\": " + method.getUri(), e);
        }

        LOG.info("Canonicalization was successful; Transformation \"" + method.getText() + "\": " + method.getUri());
        return null;
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
     * Test if file is well formed XML document. If true, document is internally parsed and can
     * be retrieved by {@code getXMLDocument()}.
     *
     * @return true if document is readable file and parses as XML, otherwise false
     */
    public boolean isXmlDocument() {

        return (isReadableFile() && (parseXml() != null));
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

    public Document getXmlDocument() {
        return xmlDocument;
    }

    public byte[] getRawBytes() {
        return rawBytes;
    }

    public byte[] getTransformedBytes() {
        return transformedBytes;
    }

}