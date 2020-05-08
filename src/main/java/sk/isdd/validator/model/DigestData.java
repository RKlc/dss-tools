package sk.isdd.validator.model;

import javafx.beans.property.SimpleStringProperty;

/**
 * Simple data class represents message digest results
 */
public class DigestData {

    private final SimpleStringProperty algorithm;
    private final SimpleStringProperty digest;

    public DigestData(String algorithm, String digest) {
        this.algorithm = new SimpleStringProperty(algorithm);
        this.digest = new SimpleStringProperty(digest);
    }

    public String getAlgorithm() {
        return algorithm.get();
    }
    public void setAlgorithm(String algorithm) {
        this.algorithm.set(algorithm);
    }

    public String getDigest() {
        return digest.get();
    }
    public void setDigest(String digest) {
        this.digest.set(digest);
    }

}
