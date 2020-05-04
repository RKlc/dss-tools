package sk.isdd.validator.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sk.isdd.validator.enumerations.XmlC14nMethod;
import sk.isdd.validator.xml.XmlFile;

/**
 * Data representation and transformation support for calculating message digests.
 */
public class DigestModel {

    /**
     * Default logger
     */
	private static final Logger LOG = LoggerFactory.getLogger(DigestModel.class);

    /**
     * XML source file wrapped by binder Property (bound to sourceFile button).
     */
	private ObjectProperty<XmlFile> sourceFile = new SimpleObjectProperty<>();

    /**
     * XmlC14nMethod enumeration bound to combo box.
     */
    private ObjectProperty<XmlC14nMethod> xmlC14nMethod = new SimpleObjectProperty<>();

    /**
     * Getter for source file representing selected file for transformation.
     * @return the source file object
     */
	public XmlFile getSourceFile() {
		return sourceFile.get();
	}

    /**
     * Setter for source file.
     * @param sourceFile selected source file
     */
	public void setSourceFile(XmlFile sourceFile) {
		this.sourceFile.set(sourceFile);
	}

    /**
     * Property object of as XML source file used for binding
     * @return the xml source file object wrapped in object property
     */
    public ObjectProperty<XmlFile> sourceFileProperty() {
        return sourceFile;
    }

    /**
     * Property object of "xml c14n method" used for binding
     * @return the canonicalization method enumeration wrapped in object property
     */
    public ObjectProperty<XmlC14nMethod> xmlC14nMethodProperty() {
        return xmlC14nMethod;
    }

}
