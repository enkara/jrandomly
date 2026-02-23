package de.jinteg.randomly.internal.catalog;

/**
 * Utility methods for parsing raw catalog entries.
 */
public final class RawParserUtil {
    private static final String DELIMITER = "\\|";

    private RawParserUtil() {
        // utility class
    }

    /**
     * Parses a pipe-delimited catalog line into a parts array.
     *
     * @param raw             pipe-delimited line, e.g. "COL1|COL2|COL3"
     * @param expectedColumns minimum number of expected columns
     * @return parsed parts array with at least {@code expectedColumns} entries
     * @throws IllegalArgumentException if raw is null or has fewer columns than expected
     */
    public static String[] parse(String raw, int expectedColumns) {
        if (raw == null) {
            throw new IllegalArgumentException("Invalid raw entry: null");
        }
        if (expectedColumns < 1) {
            throw new IllegalArgumentException("Invalid expectedColumns: " + expectedColumns);
        }
        String[] parts = raw.split(DELIMITER, expectedColumns);
        if (parts.length < expectedColumns) {
            throw new IllegalArgumentException(
                    "Invalid catalog entry (expected " + expectedColumns
                            + " columns, got " + parts.length + "): " + raw);
        }
        return parts;
    }
}
