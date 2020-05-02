package sk.isdd.validator.enumerations;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

/**
 * Supported XML canonicalization (c14n) methods
 *
 */
public enum XmlC14nMethod implements UriBasedEnum {

    // @formatter:off
    NONE(                    "NONE",                    "No XML canonicalization",          ""),

    C14N_OMIT_COMMENTS(      "C14N_OMIT_COMMENTS",      "XML c14n omit comments",           "http://www.w3.org/TR/2001/REC-xml-c14n-20010315"),

    C14N_WITH_COMMENTS(      "C14N_WITH_COMMENTS",      "XML c14n with comments",           "http://www.w3.org/TR/2001/REC-xml-c14n-20010315#WithComments"),

    C14N_EXCL_OMIT_COMMENTS( "C14N_EXCL_OMIT_COMMENTS", "XML exclusive c14n omit comments", "http://www.w3.org/2001/10/xml-exc-c14n#"),

    C14N_EXCL_WITH_COMMENTS( "C14N_EXCL_WITH_COMMENTS", "XML exclusive c14n with comments", "http://www.w3.org/2001/10/xml-exc-c14n#WithComments"),

    C14N11_OMIT_COMMENTS(    "C14N11_OMIT_COMMENTS",    "XML c14n11 omit comments",         "http://www.w3.org/2006/12/xml-c14n11"),

    C14N11_WITH_COMMENTS(    "C14N11_WITH_COMMENTS",    "XML c14n11 with comments",         "http://www.w3.org/2006/12/xml-c14n11#WithComments");
    // @formatter:on

    private final String name;
    private final String text;
    private final String uri;

    /**
     * Self initializing static registry for reverse lookups
     *
     */
    private static class Registry {

        private static final Map<String, XmlC14nMethod> METHODS = registerMethods();
        private static final Map<String, XmlC14nMethod> TEXT_METHODS = registerTextMethods();
        private static final Map<String, XmlC14nMethod> URI_METHODS = registerUriMethods();

        private static Map<String, XmlC14nMethod> registerMethods() {

            final Map<String, XmlC14nMethod> map = new HashMap<>();

            for (final XmlC14nMethod method : values()) {
                map.put(method.name, method);
            }
            return map;
        }

        private static Map<String, XmlC14nMethod> registerTextMethods() {

            final Map<String, XmlC14nMethod> map = new HashMap<>();

            for (final XmlC14nMethod method : values()) {
                map.put(method.text, method);
            }
            return map;
        }

        private static Map<String, XmlC14nMethod> registerUriMethods() {

            final Map<String, XmlC14nMethod> map = new HashMap<>();

            for (final XmlC14nMethod method : values()) {
                map.put(method.uri, method);
            }
            return map;
        }

    }

    /**
     * Returns the c14n method associated to the given name.
     *
     * @param name
     * 				the c14n method name
     * @return the c14n method linked to the given name
     * @throws IllegalArgumentException
     *				if the given name doesn't match any c14n method
     */
    public static XmlC14nMethod forName(final String name) {

        final XmlC14nMethod method = Registry.METHODS.get(name);
        if (method == null) {
            throw new IllegalArgumentException("Unsupported canonicalization method: " + name);
        }
        return method;
    }

    /**
     * Returns the c14n method associated to the given name or default value.
     *
     * @param name
     *				the c14n method name
     * @param defaultValue
     *				The default value for the {@code XmlC14nMethod}
     * @return the corresponding {@code XmlC14nMethod} or the default value
     */
    public static XmlC14nMethod forName(final String name, final XmlC14nMethod defaultValue) {

        final XmlC14nMethod method = Registry.METHODS.get(name);
        if (method == null) {
            return defaultValue;
        }
        return method;
    }

    /**
     * Returns indication if the c14n method with given {@code name} is supported
     *
     * @param name
     *              target c14n method's name
     * @return TRUE if the c14n method is supported, FALSE otherwise
     */
    public static boolean isSupportedName(final String name) {
        return Registry.METHODS.get(name) != null;
    }

    /**
     * Returns the c14n method associated with text representation.
     *
     * @param text
     *              common c14n method name
     * @return the c14n method linked to the given name
     * @throws IllegalArgumentException
     *              if the given name doesn't match any c14n method
     */
    public static XmlC14nMethod forText(final String text) {

        final XmlC14nMethod method = Registry.TEXT_METHODS.get(text);
        if (method == null) {
            throw new IllegalArgumentException("Unsupported canonicalization method: " + text);
        }
        return method;
    }

    /**
     * Returns the c14n method associated to the given W3C url.
     *
     * @param uri
     *              the c14n method uri
     * @return the c14n method linked to the given uri
     * @throws IllegalArgumentException
     *              if the uri doesn't match any c14n method
     */
    public static XmlC14nMethod forUri(final String uri) {

        final XmlC14nMethod method = Registry.URI_METHODS.get(uri);
        if (method == null) {
            throw new IllegalArgumentException("Unsupported canonicalization method: " + uri);
        }
        return method;
    }

    /**
     * Returns the c14n method associated to the given W3C url or the default one if the c14n
     * method does not exist.
     *
     * @param uri
     *              the c14n method uri
     * @param defaultValue
     *              The default value for the {@code XmlC14nMethod}
     * @return the c14n method linked to the given uri
     * @throws IllegalArgumentException
     *              if the uri doesn't match any c14n method
     */
    public static XmlC14nMethod forUri(final String uri, final XmlC14nMethod defaultValue) {

        final XmlC14nMethod method = Registry.URI_METHODS.get(uri);
        if (method == null) {
            return defaultValue;
        }
        return method;
    }

    XmlC14nMethod(final String name, final String text, final String uri) {
        this.name = name;
        this.text = text;
        this.uri = uri;
    }

    /**
     * Get the c14n method name
     *
     * @return the c14n method name
     */
    public String getName() {
        return name;
    }

    /**
     * Get common c14n method name
     *
     * @return common c14n method name
     */
    public String getText() {
        return text;
    }

    /**
     * Get common c14n method name as string representation
     *
     * @return common c14n method name
     */
    public String toString() {
        return text;
    }

    /**
     * Get the c14n method uri
     *
     * @return the c14n method uri
     */
    public String getUri() {
        return uri;
    }

    // TODO: Implement Canonicalizer class
    /*
     * Get a new instance of Canonicalizer for the current c14n method
     *
     * @return an instance of MessageDigest
     * @throws NoSuchAlgorithmException
     *                                  if the c14n method is not supported
     */
    /*
    public Canonicalizer getCanonicalizer() throws NoSuchAlgorithmException {
        return Canonicalizer.getInstance(name);
    }
    */
}
