package de.jinteg.randomly.domain.finance;

import de.jinteg.randomly.JRandomly;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.Currency;
import java.util.List;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FinanceRandomlyTest {

    @AfterEach
    void cleanup() {
        System.clearProperty("jrandomly.seed");
        System.clearProperty("jrandomly.locale");
    }

    @Test
    void stockSymbol_usesConfiguredLocaleByDefault() {
        System.setProperty("jrandomly.seed", "1");
        System.setProperty("jrandomly.locale", "de-DE");

        JRandomly r = JRandomly.randomly("FinanceTest#de");
        String sym = r.finance().stockSymbol();

        assertThat(sym).isNotEmpty().hasSizeBetween(3, 10);
    }

    @Test
    void stockSymbol_canBeOverriddenPerCallChainWithLocaleView() {
        System.setProperty("jrandomly.seed", "1");
        System.setProperty("jrandomly.locale", "de-DE");

        JRandomly r = JRandomly.randomly("FinanceTest#override");
        String sym = r.finance().stockSymbol(Locale.US);

        assertThat(sym).isIn("AAPL", "MSFT", "TSLA", "AMZN", "ABNB", "ADBE", "GOOGL", "META", "NVDA", "JPM");
    }

    @Test
    void stock() {
        System.setProperty("jrandomly.seed", "1");
        System.setProperty("jrandomly.locale", "de-DE");

        JRandomly r = JRandomly.randomly("FinanceTest#stockEntry");
        StockEntry stockEntry = r.finance().stock(Locale.US);
        assertThat(stockEntry).isNotNull();
        assertThat(stockEntry.symbol()).isIn("AAPL", "MSFT", "TSLA", "AMZN", "ABNB", "ADBE", "GOOGL", "META", "NVDA", "JPM");
        assertThat(stockEntry.name()).isNotBlank();
        assertThat(stockEntry.marketCap()).isPositive();
        assertThat(stockEntry.price()).isPositive();

    }

    @Test
    void stock_withLocale() {
        System.setProperty("jrandomly.seed", "1");
        System.setProperty("jrandomly.locale", "de-DE");

        JRandomly r = JRandomly.randomly("FinanceTest#stockEntryLocale");
        StockEntry stockEntry = r.finance().stock(Locale.GERMANY);
        assertThat(stockEntry).isNotNull();
        assertThat(stockEntry.symbol()).isNotEmpty().hasSizeBetween(3, 10);
        assertThat(stockEntry.name()).isNotBlank();
        assertThat(stockEntry.marketCap()).isPositive();
        assertThat(stockEntry.price()).isPositive();
    }

    @Test
    void stock_without_Locale_uses_default_locale() {
        System.setProperty("jrandomly.seed", "1");
        System.setProperty("jrandomly.locale", "de-DE");

        JRandomly r = JRandomly.randomly("FinanceTest#stockEntryWOLocale");
        StockEntry stockEntry = r.finance().stock();
        assertThat(stockEntry).isNotNull();
        assertThat(stockEntry.symbol()).isNotEmpty().hasSizeBetween(3, 10);
        assertThat(stockEntry.name()).isNotBlank();
        assertThat(stockEntry.marketCap()).isPositive();
        assertThat(stockEntry.price()).isPositive();
    }

    @Test
    void stock_withUnsupportedLocale() {
        System.setProperty("jrandomly.seed", "1");

        JRandomly r = JRandomly.randomly("FinanceTest#stockEntryLocaleUnsupported");

        assertThatThrownBy(() -> r.finance().stock(Locale.ITALY))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Catalog not found")
                .hasMessageContaining("it");
    }

    @Test
    void stockSymbol_usesConfiguredLocale() {
        JRandomly r = JRandomly.randomly("FinanceTest#Currency");
        Currency currency = r.finance().currency();
        String currencyCode = r.finance().currencyCode();
        String currencySymbol = r.finance().currencySymbol();

        assertThat(currency).isNotNull();
        assertThat(currencyCode).hasSize(3);
        assertThat(currencySymbol).isNotEmpty();
    }

    @Test
    void currency_with_excluding() {
        JRandomly r = JRandomly.randomly("FinanceTest#Currency");
        List<Currency> excluding = List.of(Currency.getInstance("EUR"), Currency.getInstance("USD"));
        Currency currency = r.finance().currency(excluding);

        assertThat(currency).isNotNull()
                .isNotEqualTo(Currency.getInstance("EUR"))
                .isNotEqualTo(Currency.getInstance("USD"));
    }

    @Test
    void currencyCode_excluding() {
        JRandomly r = JRandomly.randomly("FinanceTest#CurrencyCodeExcluding");
        String currency1 = r.finance().currencyCode();
        String currency2 = r.finance().currencyCode(List.of(currency1));

        assertThat(currency1).isNotNull().isNotEqualTo(currency2);
    }

    @Test
    void currencySymbol_excluding() {
        JRandomly r = JRandomly.randomly("FinanceTest#CurrencySymbolExcluding");
        String currency1 = r.finance().currencySymbol();
        String currency2 = r.finance().currencySymbol(List.of(currency1));

        assertThat(currency1).isNotNull().isNotEqualTo(currency2);
    }

}

