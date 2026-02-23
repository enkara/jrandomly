package de.jinteg.randomly.internal;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class SeedDerivationTest {

    @Test
    void seedForScope_isDeterministic() {
        long a = SeedDerivation.seedForScope(123L, "MyTest#x");
        long b = SeedDerivation.seedForScope(123L, "MyTest#x");
        assertThat(a).isEqualTo(b);
    }

    @Test
    void seedForScope_changesWithScope() {
        long a = SeedDerivation.seedForScope(123L, "A");
        long b = SeedDerivation.seedForScope(123L, "B");
        assertThat(a).isNotEqualTo(b);
    }

    @Test
    void seedForSubstream_isDeterministic() {
        long a = SeedDerivation.seedForSubstream(999L, "locale:en-US");
        long b = SeedDerivation.seedForSubstream(999L, "locale:en-US");
        assertThat(a).isEqualTo(b);
    }

    @Test
    void seedForSubstream_rejectsBlankPurpose() {
        assertThatThrownBy(() -> SeedDerivation.seedForSubstream(1L, " "))
                .isInstanceOf(IllegalArgumentException.class);
    }
}