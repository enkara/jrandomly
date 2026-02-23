package de.jinteg.randomly;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Locale;

/**
 * Tests of the `JRandomly` class without core or domain modules.
 */
class JRandomlyTest {

    @Test
    void randomly_withDefaults() {
        JRandomly r = JRandomly.randomly();

        Assertions.assertThat(r.getInstanceSeed()).isNotZero();
        Assertions.assertThat(r.getRunStartTime()).isNotNull();
        Assertions.assertThat(r.getLocale()).isNotNull();
    }

    @Test
    void randomly_withScope() {
        JRandomly r = JRandomly.randomly("MyRandomTest4711");

        Assertions.assertThat(r.getScopeLabel()).contains("MyRandomTest4711");
        Assertions.assertThat(r.getInstanceSeed()).isNotZero();
    }

    @Test
    void builder_withAllValues() {
        JRandomly r = JRandomly.builder()
                .withScope("BuilderTest#full")
                .withSeed(42L)
                .withLocale(Locale.ITALY)
                .withRunStartTime(Instant.parse("2026-01-01T00:00:00Z"))
                .withMaybeRate(0.5)
                .build();

        Assertions.assertThat(r.getLocale()).isEqualTo(Locale.ITALY);
        Assertions.assertThat(r.getRunStartTime()).isEqualTo(Instant.parse("2026-01-01T00:00:00Z"));
        Assertions.assertThat(r.replayInfo())
                .contains("-Djrandomly.seed=42")
                .contains("-Djrandomly.locale=it-IT")
                .contains("-Djrandomly.runStartTime=2026-01-01T00:00:00Z");
    }

    @Test
    void builder_localeAffectsDomainData() {
        JRandomly rIt = JRandomly.builder()
                .withScope("BuilderTest#locale")
                .withSeed(42L)
                .withLocale(Locale.ITALY)
                .build();

        JRandomly rDe = JRandomly.builder()
                .withScope("BuilderTest#locale")
                .withSeed(42L)
                .withLocale(Locale.GERMANY)
                .build();

        // Different locale → potentially different catalog data
        Assertions.assertThat(rIt.getLocale()).isEqualTo(Locale.ITALY);
        Assertions.assertThat(rDe.getLocale()).isEqualTo(Locale.GERMANY);
    }
}