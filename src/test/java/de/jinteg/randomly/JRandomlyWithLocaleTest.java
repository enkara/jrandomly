package de.jinteg.randomly;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;

class JRandomlyWithLocaleTest {

    @Test
    void sameBuilderConfig_producesSameSequence() {
        JRandomly a = JRandomly.builder()
                .withScope("LocaleTest#determinism")
                .withSeed(777L)
                .withLocale(Locale.US)
                .withRunStartTime(Instant.parse("2026-01-01T00:00:00Z"))
                .build();

        JRandomly b = JRandomly.builder()
                .withScope("LocaleTest#determinism")
                .withSeed(777L)
                .withLocale(Locale.US)
                .withRunStartTime(Instant.parse("2026-01-01T00:00:00Z"))
                .build();

        // Same seed + scope + locale → identical sequence
        for (int i = 0; i < 10; i++) {
            assertThat(a.intBetween(0, 1_000_000))
                    .as("Value #%d should match", i)
                    .isEqualTo(b.intBetween(0, 1_000_000));
        }
    }

    @Test
    void differentLocale_selectsDifferentCatalogData() {
        JRandomly us = JRandomly.builder()
                .withScope("LocaleTest#catalog")
                .withSeed(777L)
                .withLocale(Locale.US)
                .withRunStartTime(Instant.parse("2026-01-01T00:00:00Z"))
                .build();

        JRandomly de = JRandomly.builder()
                .withScope("LocaleTest#catalog")
                .withSeed(777L)
                .withLocale(Locale.GERMANY)
                .withRunStartTime(Instant.parse("2026-01-01T00:00:00Z"))
                .build();

        // Same RNG stream, but locale drives catalog selection
        assertThat(us.getLocale()).isEqualTo(Locale.US);
        assertThat(de.getLocale()).isEqualTo(Locale.GERMANY);

        // Catalog-based methods may return different results
        boolean anyDifference = false;
        for (int i = 0; i < 10; i++) {
            if (!us.finance().stockSymbol().equals(de.finance().stockSymbol())) {
                anyDifference = true;
                break;
            }
        }
        assertThat(anyDifference)
                .as("Different locales should select different catalog entries")
                .isTrue();
    }
}