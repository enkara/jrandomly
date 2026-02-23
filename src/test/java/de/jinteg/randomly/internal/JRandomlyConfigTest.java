package de.jinteg.randomly.internal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Locale;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class JRandomlyConfigTest {

    @Test
    @DisplayName("should initialize config with default values")
    void shouldInitializeConfigWithGivenParameters() {
        JRandomlyConfig config = new JRandomlyConfig(
                Optional.of(123L),
                Locale.FRANCE,
                0.125,
                Instant.parse("2026-02-07T12:34:56Z"));

        assertNotNull(config.rootSeed());
        assertThat(config.rootSeed()).hasValue(123L);
        assertThat(config.locale()).isEqualTo(Locale.FRANCE);
        assertThat(config.maybeRate()).isEqualTo(0.125);
        assertThat(config.runStartTime()).isEqualTo(Instant.parse("2026-02-07T12:34:56Z"));
    }

    @Test
    @DisplayName("should initialize config with default values if null given")
    void shouldInitializeConfigWithDefaultValuesIfNullGiven() {
        assertThatThrownBy(() -> new JRandomlyConfig(Optional.empty(), Locale.GERMANY, 0.125, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("runStartTime must not be null");
    }

    @Test
    @DisplayName("should fail initialize config if above one is given")
    void shouldFailInitializeConfigIfAboveOneIsGiven() {
        assertThatThrownBy(() -> new JRandomlyConfig(
                Optional.empty(),
                Locale.GERMANY,
                1.00001,
                Instant.parse("2026-02-07T12:34:56Z")
        )).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("maybeRate must be in range");
    }

    @Test
    @DisplayName("should fail initialize config if below zero is given")
    void shouldFailInitializeConfigIfBelowZeroIsGiven() {
        assertThatThrownBy(() -> new JRandomlyConfig(
                Optional.empty(),
                Locale.GERMANY,
                -0.00001,
                Instant.parse("2026-02-07T12:34:56Z")
        )).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("maybeRate must be in range");
    }
}