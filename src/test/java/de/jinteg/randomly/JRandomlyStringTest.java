package de.jinteg.randomly;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class JRandomlyStringTest {

    @AfterEach
    void cleanup() {
        System.clearProperty("jrandomly.seed");
    }

    @Test
    void alphaNumeric_respectsLengthRange() {
        System.setProperty("jrandomly.seed", "123");
        JRandomly r = JRandomly.randomly("StringTest#range");

        String s = r.text().alphaNumeric(5, 12);

        assertThat(s.length()).isBetween(5, 12);
        assertThat(s).matches("[0-9A-Za-z]+");
    }

    @Test
    void numericString_hasOnlyDigits() {
        System.setProperty("jrandomly.seed", "123");
        JRandomly r = JRandomly.randomly("StringTest#digits");

        String s = r.text().numericString(10);

        assertThat(s).hasSize(10)
                .matches("[0-9]+");
    }

    @Test
    void prefixedAlphaNumeric_respectsTotalLengthAndPrefix() {
        System.setProperty("jrandomly.seed", "123");
        JRandomly r = JRandomly.randomly("StringTest#prefix");

        String s = r.text().prefixedAlphaNumeric("ABC", 5, 8);

        assertThat(s).startsWith("ABC");
        assertThat(s.length()).isBetween(5, 8);
        assertThat(s.substring(3)).matches("[0-9A-Za-z]*");
    }

    @Test
    void stringGeneration_isDeterministicForSameSeedAndScope() {
        System.setProperty("jrandomly.seed", "999");

        JRandomly a = JRandomly.randomly("StringTest#det");
        JRandomly b = JRandomly.randomly("StringTest#det");

        assertThat(a.text().alphaNumeric(12)).isEqualTo(b.text().alphaNumeric(12));
        assertThat(a.text().hexString(16)).isEqualTo(b.text().hexString(16));
    }
}