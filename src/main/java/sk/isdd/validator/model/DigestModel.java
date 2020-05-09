package sk.isdd.validator.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sk.isdd.validator.enumerations.DigestAlgorithm;
import sk.isdd.validator.enumerations.XmlC14nMethod;
import sk.isdd.validator.xml.XmlFile;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

/**
 * Data representation and transformation support for calculating message digests.
 */
public class DigestModel {

	private static final Logger LOG = LoggerFactory.getLogger(DigestModel.class);

    /**
     * XML source file wrapped by binder Property (bound to sourceFile button).
     */
	private ObjectProperty<XmlFile> sourceFile = new SimpleObjectProperty<>();

    /**
     * C14n transformation method (enumeration bound to combo box).
     */
    private ObjectProperty<XmlC14nMethod> method = new SimpleObjectProperty<>();

    /**
     * List of calculated message digests.
     */
    //private ArrayList<DigestData> DigestList = new ArrayList<>();

    private ObservableList<DigestData> DigestList = FXCollections.observableArrayList();


    /**
     * Bytes of transformed source file (transformation depends on the method).
     */
    private byte[] transformedBytes;

    /**
     * Calculate all available message digests and store them into DigestList.
     *
     * <p> Calculate message digests from transformed source file and store raw results within internal list for further display.
     * <ol>
     *      <li>Chosen source file will be loaded (if not done before).
     *      <li>If canonicalization is selected (and applicable), source will be transformed.
     *      <li>All enumerated and registered message digests will be calculated.
     * </ol>
     */
    public void calculateDigestData() {

        transformedBytes = sourceFile.get().canonicalize(method.get());

        if (transformedBytes == null) {
            LOG.warn("Nothing to calculate, source file is empty or transformation failed.");
            return;
        }

        DigestList.clear();

        for (DigestAlgorithm algorithm : DigestAlgorithm.values()) {
            try {
                MessageDigest md = algorithm.getMessageDigest();
                md.update(transformedBytes);
                DigestList.add(new DigestData(algorithm, md.digest()));

            } catch (NoSuchAlgorithmException e) {
                LOG.warn("Message digest algorithm \"" + algorithm.getJavaName() + "\" not provided.");
            }
        }
        LOG.info("Message digest list calculated successfully; Transformation \"" + method.get().getText() + "\": " + method.get().getUri());
    }

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
     * Property object of as XML source file used for binding.
     * @return the xml source file object wrapped in object property
     */
    public ObjectProperty<XmlFile> sourceFileProperty() {
        return sourceFile;
    }

    /**
     * Property object of "xml c14n method" used for binding.
     * @return the canonicalization method enumeration wrapped in object property
     */
    public ObjectProperty<XmlC14nMethod> methodProperty() {
        return method;
    }

    /**
     * Access to output bytes from transformation method.
     */
    public byte[] getTransformedBytes() {
        return transformedBytes;
    }

    public ObservableList<DigestData> getDigestList() {
        return DigestList;
    }

}
