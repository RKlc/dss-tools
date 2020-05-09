package sk.isdd.validator.enumerations;

import sk.isdd.validator.fx.I18nEnum;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Enumeration of supported XML canonicalization (c14n) methods.
 *
 * The word "CanonicalizatioN" is often shortened to "C14N" - the letters "C" and "N" with 14 other letters in between ;)
 *
 * <p> Canonicalization is a method for generating a physical representation, the canonical form, of an XML document
 * that accounts for syntactic changes permitted by the XML specification. In other words, no matter what changes
 * could be made to a given XML document under transmission, the canonical form will always be identical, byte-for-byte.
 * This byte sequence is critical when signing an XML document or verifying its signature.
 *
 * <p> Each c14n method comes in pair. One will remove comments in the process of transforming XML and the other keeps them.
 * Hence enums have suffix OMIT_COMMENTS resp. WITH_COMMENTS.
 *
 * <p> There is also "Exclusive Canonicalization" (v 1.0 and 1.1) which better fits enveloped SOAP signatures
 * because it doesn't invalidate the signature when you wrap something that is already signed.
 * There are some key differences between Exclusive and Inclusive c14n, they are not backward compatible:
 *
 * <ul>
 *     <li> In the inclusive treatment all namespaces on a node are made explicit in the canonical form.
 *     <li> Such a behaviour is problematic for enveloped signatures, where unrelated namespace can be introduced
 *          into the already signed part of XML.
 *     <li> Exclusive treatment makes nodes explicit only for utilized namespaces.
 *     <li> Exclusive form does not copy the xml:lang attribute (inclusive form does).
 * </ul>
 *
 * <p> C14N11 stands for Canonical XML Version 1.1. It address issues with inheritance within namespaces, does not inherit xml:id
 * and also processes xml:base URI path properly, among other things.
 */
public enum XmlC14nMethod implements UriBasedEnum {

    /**
     * Canonicalization method is not used. Data stream is not to be transformed.
     */
    C14N_NONE("C14N_NONE", "no-c14n", ""),

    /**
     * Original Inclusive Canonicalization without comments method as defined in http://www.w3.org/TR/2001/REC-xml-c14n-20010315.
     */
    C14N_OMIT_COMMENTS("C14N_OMIT_COMMENTS", "incl-c14n", "http://www.w3.org/TR/2001/REC-xml-c14n-20010315"),

    /**
     * Inclusive Canonicalization that keeps comments in transformed XML. Defined in http://www.w3.org/TR/2001/REC-xml-c14n-20010315#WithComments.
     */
    C14N_WITH_COMMENTS("C14N_WITH_COMMENTS", "incl-c14n-comm", "http://www.w3.org/TR/2001/REC-xml-c14n-20010315#WithComments"),

    /**
     * Exclusive c14n, omit comments, defined in http://www.w3.org/2001/10/xml-exc-c14n#.
     */
    C14N_EXCL_OMIT_COMMENTS("C14N_EXCL_OMIT_COMMENTS", "excl-c14n", "http://www.w3.org/2001/10/xml-exc-c14n#"),

    /**
     * Exclusive c14n, with comments, defined in http://www.w3.org/2001/10/xml-exc-c14n#WithComments.
     */
    C14N_EXCL_WITH_COMMENTS("C14N_EXCL_WITH_COMMENTS", "excl-c14n-comm", "http://www.w3.org/2001/10/xml-exc-c14n#WithComments"),

    /**
     * Exclusive c14n v1.1, remove comments, defined in http://www.w3.org/2006/12/xml-c14n11.
     */
    C14N11_OMIT_COMMENTS("C14N11_OMIT_COMMENTS", "excl-c14n11", "http://www.w3.org/2006/12/xml-c14n11"),

    /**
     * Exclusive c14n v1.1, keep comments, defined in http://www.w3.org/2006/12/xml-c14n11#WithComments.
     */
    C14N11_WITH_COMMENTS("C14N11_WITH_COMMENTS", "excl-c14n11-comm", "http://www.w3.org/2006/12/xml-c14n11#WithComments");

    private final String name;
    private final String text;
    private final String uri;

    /**
     * Enumeration constructor.
     *
     * @param name the string representation of enumeration's name (e.g. for C14N_OMIT_COMMENTS enum it must be "C14N_OMIT_COMMENTS")
     * @param text unique common reference text (suitable for combo box bindings or translations)
     * @param uri  the uri identifier as specified by W3C; It is referenced within XML documents
     */
    XmlC14nMethod(final String name, final String text, final String uri) {
        this.name = name;
        this.text = text;
        this.uri = uri;
    }

    /**
     * Self initializing static registry for reverse lookups of enums.
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
     * Returns the "c14n method" enum associated with the given name. Find enum by its name.
     *
     * @param name the c14n method name (e.g. "C14N_OMIT_COMMENTS")
     * @return the corresponding {@code XmlC14nMethod} object linked to the given name
     * @throws IllegalArgumentException if the given name doesn't match any name
     */
    public static XmlC14nMethod forName(final String name) {

        final XmlC14nMethod method = Registry.METHODS.get(name);
        if (method == null) {
            throw new IllegalArgumentException("Unsupported canonicalization method: " + name);
        }
        return method;
    }

    /**
     * Returns the "c14n method" enum associated with the given name or default value.
     *
     * @param name         the c14n method name (e.g. "C14N_OMIT_COMMENTS")
     * @param defaultValue The default value for the {@code XmlC14nMethod}
     * @return the corresponding {@code XmlC14nMethod} object or the default value
     */
    public static XmlC14nMethod forName(final String name, final XmlC14nMethod defaultValue) {

        final XmlC14nMethod method = Registry.METHODS.get(name);
        if (method == null) {
            return defaultValue;
        }
        return method;
    }

    /**
     * Returns indication if the canonicalization is supported based on its name.
     *
     * @param name canonicalization name in question
     * @return TRUE if chosen canonicalization is supported, FALSE otherwise
     */
    public static boolean isSupportedName(final String name) {
        return Registry.METHODS.get(name) != null;
    }

    /**
     * Returns the c14n method associated with its common name. Find enum by its common text representation.
     *
     * <p> It can be used for combo boxes or translated by resource bundles.
     *
     * @param text common canonicalization name (e.g. "incl-c14n")
     * @return the {@code XmlC14nMethod} instance linked to the given name
     * @throws IllegalArgumentException if c14n method is not found
     */
    public static XmlC14nMethod forText(final String text) {

        final XmlC14nMethod method = Registry.TEXT_METHODS.get(text);
        if (method == null) {
            throw new IllegalArgumentException("Unsupported canonicalization method: " + text);
        }
        return method;
    }

    /**
     * Returns the c14n method associated with its common name or default value.
     *
     * @param text         common canonicalization name (e.g. "incl-c14n")
     * @param defaultValue The default value for the {@code XmlC14nMethod}
     * @return the {@code XmlC14nMethod} instance linked to the given name
     */
    public static XmlC14nMethod forText(final String text, final XmlC14nMethod defaultValue) {

        final XmlC14nMethod method = Registry.TEXT_METHODS.get(text);
        if (method == null) {
            return defaultValue;
        }
        return method;
    }

    /**
     * Returns indication if the canonicalization is supported based on its name.
     *
     * @param name canonicalization name in question
     * @return TRUE if chosen canonicalization is supported, FALSE otherwise
     */
    public static boolean isSupportedText(final String name) {
        return Registry.TEXT_METHODS.get(name) != null;
    }

    /**
     * Returns the canonicalization associated to the given W3C uri (find enum by its W3C uri).
     *
     * @param uri the canonicalization uri defined by W3C specification
     * @return the c14n method linked to the given uri
     * @throws IllegalArgumentException if the uri doesn't match any c14n method
     */
    public static XmlC14nMethod forUri(final String uri) {

        final XmlC14nMethod method = Registry.URI_METHODS.get(uri);
        if (method == null) {
            throw new IllegalArgumentException("Unsupported canonicalization method: " + uri);
        }
        return method;
    }

    /**
     * Returns the c14n method associated to the given W3C uri or the default one if the c14n
     * method does not exist.
     *
     * @param uri          the c14n method uri
     * @param defaultValue The default enum
     * @return the c14n method linked to the given uri
     * @throws IllegalArgumentException if the uri doesn't match any c14n method
     */
    public static XmlC14nMethod forUri(final String uri, final XmlC14nMethod defaultValue) {

        final XmlC14nMethod method = Registry.URI_METHODS.get(uri);
        if (method == null) {
            return defaultValue;
        }
        return method;
    }

    /**
     * Returns indication if the canonicalization is supported based on its URI.
     *
     * @param uri canonicalization uri in question
     * @return TRUE if chosen canonicalization is supported, FALSE otherwise
     */
    public static boolean isSupportedUri(final String uri) {
        return Registry.URI_METHODS.get(uri) != null;
    }

    /**
     * Get the canonicalization enum name.
     *
     * @return the c14n method name
     */
    public String getName() {
        return name;
    }

    /**
     * Get common c14n method name.
     *
     * @return common c14n method name
     */
    public String getText() {
        return text;
    }

    /**
     * Get common c14n method name as localized string
     *
     * <p> Strings will be translated from names to localized language messages if resource bundles are set.
     * Otherwise common name will be returned from common text.
     *
     * @return common translated canonicalization name (or just common name)
     */
    public String toString()
    {
        return I18nEnum.getString(text);
    }

    /**
     * Get the c14n method uri.
     *
     * @return the c14n method uri
     */
    public String getUri() {
        return uri;
    }

}
