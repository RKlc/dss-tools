package sk.isdd.validator.enumerations;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

/**
 * Enumeration of supported Message Digest algorithms.
 * <p>
 * Support for {@link java.security.MessageDigest} algorithms such as SHA256, SHA256, MD5 etc.
 */
public enum DigestAlgorithm implements UriBasedEnum, OidBasedEnum {

    // @formatter:off
    SHA1(     "SHA1",      "SHA-1",     "1.3.14.3.2.26",           "http://www.w3.org/2000/09/xmldsig#sha1"),
    SHA224(   "SHA224",    "SHA-224",   "2.16.840.1.101.3.4.2.4",  "http://www.w3.org/2001/04/xmldsig-more#sha224"),
    SHA256(   "SHA256",    "SHA-256",   "2.16.840.1.101.3.4.2.1",  "http://www.w3.org/2001/04/xmlenc#sha256"),
    SHA384(   "SHA384",    "SHA-384",   "2.16.840.1.101.3.4.2.2",  "http://www.w3.org/2001/04/xmldsig-more#sha384"),
    SHA512(   "SHA512",    "SHA-512",   "2.16.840.1.101.3.4.2.3",  "http://www.w3.org/2001/04/xmlenc#sha512"),
    SHA3_224( "SHA3-224",  "SHA3-224",  "2.16.840.1.101.3.4.2.7",  "http://www.w3.org/2007/05/xmldsig-more#sha3-224"),
    SHA3_256( "SHA3-256",  "SHA3-256",  "2.16.840.1.101.3.4.2.8",  "http://www.w3.org/2007/05/xmldsig-more#sha3-256"),
    SHA3_384( "SHA3-384",  "SHA3-384",  "2.16.840.1.101.3.4.2.9",  "http://www.w3.org/2007/05/xmldsig-more#sha3-384"),
    SHA3_512( "SHA3-512",  "SHA3-512",  "2.16.840.1.101.3.4.2.10", "http://www.w3.org/2007/05/xmldsig-more#sha3-512"),
    RIPEMD160("RIPEMD160", "RIPEMD160", "1.3.36.3.2.1",            "http://www.w3.org/2001/04/xmlenc#ripemd160"),
    MD2(      "MD2",       "MD2",       "1.2.840.113549.2.2",      "http://www.w3.org/2001/04/xmldsig-more#md2"),
    MD5(      "MD5",       "MD5",       "1.2.840.113549.2.5",      "http://www.w3.org/2001/04/xmldsig-more#md5"),
    WHIRLPOOL("WHIRLPOOL", "WHIRLPOOL", "1.0.10118.3.0.55",        "http://www.w3.org/2007/05/xmldsig-more#whirlpool");
    // @formatter:on

    private final String name;
    private final String javaName;
    private final String oid;
    private final String uri;

    /**
     * Enumeration constructor.
     *
     * @param name      the string representation of enumeration's name (e.g. for SHA256 enum it must be "SHA256")
     * @param javaName  the string as used by java java.security.MessageDigest library
     * @param oid       ISO/ITU object identifier (e.g. "2.16.840.1.101.3.4.2.1" for SHA256)
     * @param uri       URI identifier (e.g. "http://www.w3.org/2001/04/xmlenc#sha256" for SHA256)
     */
    DigestAlgorithm(final String name, final String javaName, final String oid, final String uri) {
        this.name = name;
        this.javaName = javaName;
        this.oid = oid;
        this.uri = uri;
    }

    /**
     * Self initializing static registry for reverse lookups of enums.
     */
    private static class Registry {

        private static final Map<String, DigestAlgorithm> ALGORITHMS = registerAlgorithms();
        private static final Map<String, DigestAlgorithm> JAVA_ALGORITHMS = registerJavaAlgorithms();
        private static final Map<String, DigestAlgorithm> OID_ALGORITHMS = registerOidAlgorithms();
        private static final Map<String, DigestAlgorithm> URI_ALGORITHMS = registerUriAlgorithms();

        private static Map<String, DigestAlgorithm> registerAlgorithms() {

            final Map<String, DigestAlgorithm> map = new HashMap<>();
            for (final DigestAlgorithm digestAlgorithm : values()) {
                map.put(digestAlgorithm.name, digestAlgorithm);
            }
            return map;
        }

        private static Map<String, DigestAlgorithm> registerJavaAlgorithms() {

            final Map<String, DigestAlgorithm> map = new HashMap<>();
            for (final DigestAlgorithm digestAlgorithm : values()) {
                map.put(digestAlgorithm.javaName, digestAlgorithm);
            }
            return map;
        }

        private static Map<String, DigestAlgorithm> registerOidAlgorithms() {

            final Map<String, DigestAlgorithm> map = new HashMap<>();
            for (final DigestAlgorithm digestAlgorithm : values()) {
                map.put(digestAlgorithm.oid, digestAlgorithm);
            }
            return map;
        }

        private static Map<String, DigestAlgorithm> registerUriAlgorithms() {

            final Map<String, DigestAlgorithm> map = new HashMap<>();
            for (final DigestAlgorithm digestAlgorithm : values()) {
                map.put(digestAlgorithm.uri, digestAlgorithm);
            }
            return map;
        }

    }

    /**
     * Returns the digest algorithm associated to the given name.
     *
     * @param name the algorithm name
     * @return the digest algorithm linked to the given name
     * @throws IllegalArgumentException if the given name doesn't match any algorithm
     */
    public static DigestAlgorithm forName(final String name) {

        final DigestAlgorithm algorithm = Registry.ALGORITHMS.get(name);
        if (algorithm == null) {
            throw new IllegalArgumentException("Unsupported algorithm: " + name);
        }
        return algorithm;
    }

    /**
     * Returns the digest algorithm associated to the given name.
     *
     * @param name         the algorithm name
     * @param defaultValue the default value for the {@code DigestAlgorithm}
     * @return the corresponding {@code DigestAlgorithm} or the default value
     */
    public static DigestAlgorithm forName(final String name, final DigestAlgorithm defaultValue) {

        final DigestAlgorithm algorithm = Registry.ALGORITHMS.get(name);
        if (algorithm == null) {
            return defaultValue;
        }
        return algorithm;
    }

    /**
     * Returns indication if the algorithm with given {@code name} is supported
     *
     * @param name {@link String} target algorithm's name
     * @return TRUE if the algorithm is supported, FALSE otherwise
     */
    public static boolean isSupportedAlgorithm(final String name) {
        return Registry.ALGORITHMS.get(name) != null;
    }

    /**
     * Returns the digest algorithm associated to the given JCE name.
     *
     * @param javaName the JCE algorithm name
     * @return the digest algorithm linked to the given name
     * @throws IllegalArgumentException if the given name doesn't match any algorithm
     */
    public static DigestAlgorithm forJavaName(final String javaName) {

        final DigestAlgorithm algorithm = Registry.JAVA_ALGORITHMS.get(javaName);
        if (algorithm == null) {
            throw new IllegalArgumentException("Unsupported algorithm: " + javaName);
        }
        return algorithm;
    }

    /**
     * Returns the digest algorithm associated to the given OID.
     *
     * @param oid the algorithm OID
     * @return the digest algorithm linked to the oid
     * @throws IllegalArgumentException if the oid doesn't match any digest algorithm
     */
    public static DigestAlgorithm forOID(final String oid) {

        final DigestAlgorithm algorithm = Registry.OID_ALGORITHMS.get(oid);
        if (algorithm == null) {
            throw new IllegalArgumentException("Unsupported algorithm: " + oid);
        }
        return algorithm;
    }

    /**
     * Returns the digest algorithm associated to the given URI.
     *
     * @param uriName the algorithm URI
     * @return the digest algorithm linked to the given URI
     * @throws IllegalArgumentException if the uri doesn't match any digest algorithm
     */
    public static DigestAlgorithm forUri(final String uriName) {

        final DigestAlgorithm algorithm = Registry.URI_ALGORITHMS.get(uriName);
        if (algorithm == null) {
            throw new IllegalArgumentException("Unsupported algorithm: " + uriName);
        }
        return algorithm;
    }

    /**
     * Returns the digest algorithm associated to the given XML url or the default one if the algorithm does not exist.
     *
     * @param uriName      the W3C URI specification of the digest algorithm
     * @param defaultValue the default value for the {@code DigestAlgorithm}
     * @return the corresponding {@code DigestAlgorithm} or the default value
     */
    public static DigestAlgorithm forUri(final String uriName, final DigestAlgorithm defaultValue) {

        final DigestAlgorithm algorithm = Registry.URI_ALGORITHMS.get(uriName);
        if (algorithm == null) {
            return defaultValue;
        }
        return algorithm;
    }

    /**
     * Get the algorithm name.
     *
     * @return the algorithm name
     */
    public String getName() {
        return name;
    }

    /**
     * Get the JCE algorithm name.
     *
     * @return the java algorithm name
     */
    public String getJavaName() {
        return javaName;
    }

    /**
     * Get the algorithm OID.
     *
     * @return the ASN1 algorithm OID
     */
    public String getOid() {
        return oid;
    }

    /**
     * Get the algorithm URI.
     *
     * @return the algorithm URI
     */
    public String getUri() {
        return uri;
    }

    /**
     * Get a new instance of MessageDigest for the current digest algorithm.
     *
     * @return an instance of MessageDigest
     * @throws NoSuchAlgorithmException if the algorithm is not supported
     */
    public MessageDigest getMessageDigest() throws NoSuchAlgorithmException {
        return MessageDigest.getInstance(javaName);
    }

}
