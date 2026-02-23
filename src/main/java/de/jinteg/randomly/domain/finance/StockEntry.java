package de.jinteg.randomly.domain.finance;

import java.util.Arrays;

/**
 * Stock entry with symbol, name, market cap, and price.
 *
 * @param symbol    stock symbol
 * @param name      stock name
 * @param marketCap market capitalization
 * @param price     stock price
 */
public record StockEntry(
        String symbol,
        String name,
        long marketCap,
        double price
) {
    /**
     * Number of pipe-delimited columns expected in the raw catalog format.
     */
    static final int COLUMN_COUNT = 4;

    /**
     * Parse a pipe-delimited catalog line into a {@link StockEntry}.
     *
     * @param parts in format "SYMBOL|Name|MarketCap|Price"
     * @return parsed entry
     */
    static StockEntry parse(String[] parts) {
        if (parts == null || parts.length < COLUMN_COUNT) {
            throw new IllegalArgumentException("Invalid stock raw data: " + Arrays.toString(parts)
                    + ", expected " + COLUMN_COUNT + " parts separated by '|': SYMBOL|Name|MarketCap|Price");
        }
        return new StockEntry(
                parts[0].trim(),
                parts[1].trim(),
                Long.parseLong(parts[2].trim()),
                Double.parseDouble(parts[3].trim())
        );
    }
}
