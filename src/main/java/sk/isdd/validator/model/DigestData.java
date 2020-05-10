package sk.isdd.validator.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import sk.isdd.validator.enumerations.DigestAlgorithm;

import java.util.Base64;

/**
 * Simple data class represents message digest results.
 */
public class DigestData {

    private final ObjectProperty<DigestAlgorithm> algorithm;
    private final ObjectProperty<byte[]> digest;

    /**
     * Create object out of algorithm and its calculated digest pair
     * @param algorithm enumeration object representing used algorithm
     * @param digest the message digest created from source by given algorithm
     */
    public DigestData(DigestAlgorithm algorithm, byte[] digest) {

        this.algorithm = new SimpleObjectProperty<>(algorithm);
        this.digest = new SimpleObjectProperty<>(digest);
    }

    /**
     * Return digest algorithm by its name (e.g. "SHA-256").
     */
    public String getAlgorithmName() {
        return algorithm.get().getJavaName();
    }

    /**
     * Return Base 64 encoded message digest for given algorithm.
     */
    public String getDigestBase64() {
        return Base64.getEncoder().encodeToString(digest.get());
    }

    /**
     * Return algorithm as whole enumeration object.
     */
    public DigestAlgorithm getAlgorithm() {
        return algorithm.get();
    }

    /**
     * Set algorithm by its enumeration object.
     */
    public void setAlgorithm(DigestAlgorithm algorithm) {
        this.algorithm.set(algorithm);
    }

    /**
     * Get raw digest message.
     */
    public byte[] getDigest() {
        return digest.get();
    }

    /**
     * Set raw digest message.
     */
    public void setDigest(byte[] digest) {
        this.digest.set(digest);
    }

}
