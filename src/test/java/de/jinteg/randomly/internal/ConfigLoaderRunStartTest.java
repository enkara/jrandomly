package de.jinteg.randomly.internal;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ConfigLoaderRunStartTest {

    @AfterEach
    void cleanup() {
        System.clearProperty(JRandomlyConfig.PROP_RUN_START_TIME);
    }

    @Test
    void runStart_acceptsInstantWithZ() {
        System.setProperty(JRandomlyConfig.PROP_RUN_START_TIME, "2026-02-07T12:34:56Z");

        JRandomlyConfig cfg = new ConfigLoader().load();

        assertThat(cfg.runStartTime()).isEqualTo(Instant.parse("2026-02-07T12:34:56Z"));
    }

    @Test
    void runStart_acceptsOffsetDateTime() {
        System.setProperty(JRandomlyConfig.PROP_RUN_START_TIME, "2026-02-07T13:34:56+01:00");

        JRandomlyConfig cfg = new ConfigLoader().load();

        assertThat(cfg.runStartTime()).isEqualTo(Instant.parse("2026-02-07T12:34:56Z"));
    }

    @Test
    void runStart_defaultIsStableWithinSameJvm() {
        ConfigLoader loader = new ConfigLoader();

        Instant a = loader.load().runStartTime();
        Instant b = loader.load().runStartTime();

        assertThat(a).isEqualTo(b);
    }

    @Test
    void runStart_rejectsGarbage() {
        System.setProperty(JRandomlyConfig.PROP_RUN_START_TIME, "not-a-timestamp");

        assertThatThrownBy(() -> new ConfigLoader().load())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Invalid runStartTime value");
    }
}