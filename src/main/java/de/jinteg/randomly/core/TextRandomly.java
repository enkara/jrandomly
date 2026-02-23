package de.jinteg.randomly.core;

import de.jinteg.randomly.JRandomly;
import de.jinteg.randomly.internal.catalog.NumberedPropertiesCatalog;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

/**
 * Deterministic generators for text and string values.
 * <p>
 * A utility class for generating random text strings based on given criteria.
 * Provides methods for generating alphanumeric, numeric, and hexadecimal strings
 * with customizable length ranges.
 */
public final class TextRandomly {

    private static final String ALPHA_LOWER = "abcdefghijklmnopqrstuvwxyz";
    private static final String ALPHA_UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String NUMERIC = "0123456789";
    private static final String ALPHA = ALPHA_UPPER + ALPHA_LOWER;
    private static final String ALPHA_NUMERIC = NUMERIC + ALPHA;

    private final JRandomly randomly;

    /**
     * Constructs a TextRandomly instance with the specified JRandomly instance.
     *
     * @param randomly JRandomly instance for generating random values
     */
    public TextRandomly(JRandomly randomly) {
        this.randomly = randomly;
    }

    /**
     * Returns a random alphanumeric string of the specified length.
     *
     * @param length length of the generated string
     * @return random alphanumeric string
     */
    public String alpha(int length) {
        return alpha(length, length);
    }

    /**
     * Returns a random alphanumeric string of the specified length range.
     *
     * @param minLength minimum length of the generated string
     * @param maxLength maximum length of the generated string
     * @return random alphanumeric string
     */
    public String alpha(int minLength, int maxLength) {
        return stringFromAlphabet(ALPHA, minLength, maxLength);
    }

    /**
     * Returns a random lowercase alphanumeric string of the specified length.
     *
     * @param length length of the generated string
     * @return random lowercase alphanumeric string
     */
    public String alphaLower(int length) {
        return stringFromAlphabet(ALPHA_LOWER, length, length);
    }

    /**
     * Returns a random lowercase alphanumeric string of the specified length range.
     *
     * @param minLength minimum length of the generated string
     * @param maxLength maximum length of the generated string
     * @return random lowercase alphanumeric string
     */
    public String alphaLower(int minLength, int maxLength) {
        return stringFromAlphabet(ALPHA_LOWER, minLength, maxLength);
    }

    /**
     * Returns a random uppercase alphanumeric string of the specified length.
     *
     * @param length length of the generated string
     * @return random uppercase alphanumeric string
     */
    public String alphaUpper(int length) {
        return stringFromAlphabet(ALPHA_UPPER, length, length);
    }

    /**
     * Returns a random uppercase alphanumeric string of the specified length range.
     *
     * @param minLength minimum length of the generated string
     * @param maxLength maximum length of the generated string
     * @return random uppercase alphanumeric string
     */
    public String alphaUpper(int minLength, int maxLength) {
        return stringFromAlphabet(ALPHA_UPPER, minLength, maxLength);
    }

    /**
     * Returns a random alphanumeric string of the specified length range.
     *
     * @param minLength minimum length of the generated string
     * @param maxLength maximum length of the generated string
     * @return random alphanumeric string
     */
    public String alphaNumeric(int minLength, int maxLength) {
        return stringFromAlphabet(ALPHA_NUMERIC, minLength, maxLength);
    }

    /**
     * Returns a random alphanumeric string of the specified length.
     *
     * @param length length of the generated string
     * @return random alphanumeric string
     */
    public String alphaNumeric(int length) {
        return alphaNumeric(length, length);
    }

    /**
     * Returns a random numeric string of the specified length.
     *
     * @param length length of the generated string
     * @return random numeric string
     */
    public String numericString(int length) {
        return numericString(length, length);
    }

    /**
     * Returns a random numeric string of the specified length range.
     *
     * @param minLength minimum length of the generated string
     * @param maxLength maximum length of the generated string
     * @return random numeric string
     */
    public String numericString(int minLength, int maxLength) {
        return stringFromAlphabet(NUMERIC, minLength, maxLength);
    }

    /**
     * Returns a random hexadecimal string of the specified length.
     *
     * @param length length of the generated string
     * @return random hexadecimal string
     */
    public String hexString(int length) {
        return stringFromAlphabet("0123456789abcdef", length, length);
    }

    /**
     * Returns a random alphanumeric string with a specified prefix and length range.
     *
     * @param prefix prefix to prepend to the generated string
     * @param length minimum and maximum length of the generated string
     * @return random alphanumeric string with the specified prefix and length range
     */
    public String prefixedAlphaNumeric(String prefix, int length) {
        return prefixedAlphaNumeric(prefix, length, length);
    }

    /**
     * Returns a random alphanumeric string with a specified prefix and length range.
     *
     * @param prefix         prefix to prepend to the generated string
     * @param minTotalLength minimum length of the generated string
     * @param maxTotalLength maximum length of the generated string
     * @return random alphanumeric string with the specified prefix and length range
     */
    public String prefixedAlphaNumeric(String prefix, int minTotalLength, int maxTotalLength) {
        Objects.requireNonNull(prefix, "prefix");
        if (minTotalLength < 0 || maxTotalLength < 0 || minTotalLength > maxTotalLength) {
            throw new IllegalArgumentException("Invalid length range");
        }
        if (prefix.length() > maxTotalLength) {
            throw new IllegalArgumentException("prefix is longer than maxTotalLength");
        }

        int remainingMin = Math.max(0, minTotalLength - prefix.length());
        int remainingMax = maxTotalLength - prefix.length();

        return prefix + alphaNumeric(remainingMin, remainingMax);
    }

    // --- Internal helpers ---

    private String stringFromAlphabet(String alphabet, int minLength, int maxLength) {
        Objects.requireNonNull(alphabet, "alphabet");
        if (minLength < 0 || maxLength < 0 || minLength > maxLength) {
            throw new IllegalArgumentException("Invalid length range");
        }
        if (alphabet.isEmpty()) {
            throw new IllegalArgumentException("alphabet must not be empty");
        }

        int len = (minLength == maxLength) ? minLength : randomly.intBetween(minLength, maxLength);
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            int idx = randomly.index(alphabet.length());
            sb.append(alphabet.charAt(idx));
        }
        return sb.toString();
    }

    /**
     * Returns a single random sentence from the UDHR corpus using the instance locale.
     *
     * @return random sentence
     */
    public String sentence() {
        return sentence(randomly.getLocale());
    }

    /**
     * Returns a single random sentence from the UDHR corpus using the given locale.
     *
     * @param locale locale to use for catalog selection
     * @return random sentence
     */
    public String sentence(Locale locale) {
        Objects.requireNonNull(locale, "locale");
        List<String> sentences = loadUdhrSentences(locale);
        return randomly.elementOf(sentences);
    }

    /**
     * Returns a paragraph of 3 to 6 random sentences from the UDHR corpus.
     *
     * @return random paragraph
     */
    public String paragraph() {
        return paragraph(randomly.getLocale());
    }

    /**
     * Returns a paragraph of 3 to 6 random sentences from the UDHR corpus using the given locale.
     *
     * @param locale locale to use for catalog selection
     * @return random paragraph
     */
    public String paragraph(Locale locale) {
        Objects.requireNonNull(locale, "locale");
        List<String> sentences = loadUdhrSentences(locale);
        int count = Math.min(6, sentences.size());
        int[] indices = randomly.uniqueIndices(count, sentences.size());
        List<String> uniqueSentences = Arrays.stream(indices)
                .mapToObj(sentences::get)
                .toList();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < uniqueSentences.size(); i++) {
            if (i > 0) sb.append(' ');
            sb.append(uniqueSentences.get(i));
        }
        return sb.toString();
    }

    private List<String> loadUdhrSentences(Locale locale) {
        return NumberedPropertiesCatalog.loadList(
                "de/jinteg/randomly/catalog/text/udhr", locale);
    }

    // -- Nouns, Verbs, Adjectives --

    /**
     * Returns a random noun from the word catalog.
     *
     * @return random noun
     */
    public String noun() {
        return noun(randomly.getLocale());
    }

    /**
     * Returns a random noun from the word catalog.
     *
     * @param locale locale to use for catalog selection
     * @return random noun
     */
    public String noun(Locale locale) {
        return loadWord("nouns", locale);
    }

    /**
     * Returns a random verb from the word catalog.
     *
     * @return random verb
     */
    public String verb() {
        return verb(randomly.getLocale());
    }

    /**
     * Returns a random verb from the word catalog.
     *
     * @param locale locale to use for catalog selection
     * @return random verb
     */
    public String verb(Locale locale) {
        return loadWord("verbs", locale);
    }

    /**
     * Returns a random adjective from the word catalog.
     *
     * @return random adjective
     */
    public String adjective() {
        return adjective(randomly.getLocale());
    }

    /**
     * Returns a random adjective from the word catalog.
     *
     * @param locale locale to use for catalog selection
     * @return random adjective
     */
    public String adjective(Locale locale) {
        return loadWord("adjectives", locale);
    }

    /**
     * Returns a compound name combining a random adjective and noun in CamelCase.
     * Example: "BrightDolphin", "MutigerWolf"
     *
     * @return random compound name
     */
    public String compoundName() {
        return compoundName(randomly.getLocale());
    }

    /**
     * Returns a compound name combining a random adjective and noun in CamelCase.
     *
     * @param locale locale to use for catalog selection
     * @return random compound name
     */
    public String compoundName(Locale locale) {
        String adj = adjective(locale);
        String noun = noun(locale);
        return capitalize(adj) + capitalize(noun);
    }

    /**
     * Returns a slug combining a random adjective and noun in kebab-case.
     * Example: "calm-river", "stolzer-turm"
     *
     * @return random slug
     */
    public String slug() {
        return slug(randomly.getLocale());
    }

    /**
     * Returns a slug combining a random adjective and noun in kebab-case.
     *
     * @param locale locale to use for catalog selection
     * @return random slug
     */
    public String slug(Locale locale) {
        return adjective(locale).toLowerCase(locale) + "-" + noun(locale).toLowerCase(locale);
    }

    private String loadWord(String catalog, Locale locale) {
        Objects.requireNonNull(locale, "locale");
        List<String> words = NumberedPropertiesCatalog.loadList(
                "de/jinteg/randomly/catalog/text/" + catalog + "/" + catalog, locale);
        return randomly.elementOf(words);
    }

    private static String capitalize(String s) {
        if (s == null || s.isEmpty()) return s;
        return Character.toUpperCase(s.charAt(0)) + s.substring(1);
    }

}
