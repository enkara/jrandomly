package de.jinteg.randomly.core;

import de.jinteg.randomly.JRandomly;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TextRandomlyTest {

    @BeforeEach
    void setUp() {
        System.setProperty("jrandomly.seed", "42");
    }

    @AfterEach
    void cleanup() {
        System.clearProperty("jrandomly.seed");
        System.clearProperty("jrandomly.runStartTime");
    }

    private TextRandomly textRandomly(String scope) {
        return JRandomly.randomly(scope).text();
    }

    // ── alpha ───────────────────────────────────────────────

    @Nested
    class Alpha {

        @Test
        void alpha_fixedLength_returnsExactLength() {
            String result = textRandomly("alpha#fixed").alpha(10);

            assertThat(result).hasSize(10)
                    .matches("[a-zA-Z]+");
        }

        @Test
        void alpha_range_returnsLengthInRange() {
            String result = textRandomly("alpha#range").alpha(5, 15);

            assertThat(result.length()).isBetween(5, 15);
            assertThat(result).matches("[a-zA-Z]+");
        }

        @Test
        void alpha_zeroLength_returnsEmptyString() {
            String result = textRandomly("alpha#zero").alpha(0);

            assertThat(result).isEmpty();
        }

        @Test
        void alpha_isDeterministic() {
            String a = textRandomly("alpha#det").alpha(20);
            String b = textRandomly("alpha#det").alpha(20);

            assertThat(a).isEqualTo(b);
        }
    }

    // ── alphaLower ──────────────────────────────────────────

    @Nested
    class AlphaLower {

        @Test
        void alphaLower_fixedLength_containsOnlyLowercase() {
            String result = textRandomly("lower#fixed").alphaLower(12);

            assertThat(result).hasSize(12)
                    .matches("[a-z]+");
        }

        @Test
        void alphaLower_range_returnsLengthInRange() {
            String result = textRandomly("lower#range").alphaLower(3, 8);

            assertThat(result.length()).isBetween(3, 8);
            assertThat(result).matches("[a-z]+");
        }
    }

    // ── alphaUpper ──────────────────────────────────────────

    @Nested
    class AlphaUpper {

        @Test
        void alphaUpper_fixedLength_containsOnlyUppercase() {
            String result = textRandomly("upper#fixed").alphaUpper(12);

            assertThat(result).hasSize(12)
                    .matches("[A-Z]+");
        }

        @Test
        void alphaUpper_range_returnsLengthInRange() {
            String result = textRandomly("upper#range").alphaUpper(4, 10);

            assertThat(result.length()).isBetween(4, 10);
            assertThat(result).matches("[A-Z]+");
        }
    }

    // ── alphaNumeric ────────────────────────────────────────

    @Nested
    class AlphaNumeric {

        @Test
        void alphaNumeric_fixedLength_returnsAlphanumericChars() {
            String result = textRandomly("alnum#fixed").alphaNumeric(20);

            assertThat(result).hasSize(20)
                    .matches("[a-zA-Z0-9]+");
        }

        @Test
        void alphaNumeric_range_returnsLengthInRange() {
            String result = textRandomly("alnum#range").alphaNumeric(5, 25);

            assertThat(result.length()).isBetween(5, 25);
            assertThat(result).matches("[a-zA-Z0-9]+");
        }

        @Test
        void alphaNumeric_isDeterministic() {
            String a = textRandomly("alnum#det").alphaNumeric(30);
            String b = textRandomly("alnum#det").alphaNumeric(30);

            assertThat(a).isEqualTo(b);
        }
    }

    // ── numericString ───────────────────────────────────────

    @Nested
    class NumericString {

        @Test
        void numericString_fixedLength_containsOnlyDigits() {
            String result = textRandomly("num#fixed").numericString(8);

            assertThat(result).hasSize(8)
                    .matches("[0-9]+");
        }

        @Test
        void numericString_range_returnsLengthInRange() {
            String result = textRandomly("num#range").numericString(2, 10);

            assertThat(result.length()).isBetween(2, 10);
            assertThat(result).matches("[0-9]+");
        }
    }

    // ── hexString ───────────────────────────────────────────

    @Nested
    class HexString {

        @Test
        void hexString_returnsValidHexChars() {
            String result = textRandomly("hex#valid").hexString(16);

            assertThat(result).hasSize(16)
                    .matches("[0-9a-f]+");
        }

        @Test
        void hexString_isDeterministic() {
            String a = textRandomly("hex#det").hexString(32);
            String b = textRandomly("hex#det").hexString(32);

            assertThat(a).isEqualTo(b);
        }
    }

    // ── prefixedAlphaNumeric ────────────────────────────────

    @Nested
    class PrefixedAlphaNumeric {

        @Test
        void prefixed_fixedLength_startsWithPrefix() {
            String result = textRandomly("prefix#fixed").prefixedAlphaNumeric("ORD-", 10);

            assertThat(result).startsWith("ORD-")
                    .hasSize(10);
            assertThat(result.substring(4)).matches("[a-zA-Z0-9]+");
        }

        @Test
        void prefixed_range_returnsLengthInRange() {
            String result = textRandomly("prefix#range").prefixedAlphaNumeric("TX-", 5, 12);

            assertThat(result).startsWith("TX-");
            assertThat(result.length()).isBetween(5, 12);
        }

        @Test
        void prefixed_prefixEqualsMaxLength_returnsOnlyPrefix() {
            String result = textRandomly("prefix#exact").prefixedAlphaNumeric("FULL", 4, 4);

            assertThat(result).isEqualTo("FULL");
        }

        @Test
        void prefixed_nullPrefix_throwsNPE() {
            assertThatThrownBy(() -> textRandomly("prefix#null").prefixedAlphaNumeric(null, 5))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        void prefixed_prefixLongerThanMax_throwsIAE() {
            assertThatThrownBy(() -> textRandomly("prefix#long").prefixedAlphaNumeric("TOOLONG", 3, 5))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("prefix is longer than maxTotalLength");
        }

        @Test
        void prefixed_negativeLength_throwsIAE() {
            assertThatThrownBy(() -> textRandomly("prefix#neg").prefixedAlphaNumeric("X", -1, 5))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void prefixed_minGreaterThanMax_throwsIAE() {
            assertThatThrownBy(() -> textRandomly("prefix#inv").prefixedAlphaNumeric("X", 10, 5))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    // ── Validation / Edge Cases ─────────────────────────────

    @Nested
    class Validation {

        @Test
        void alpha_negativeLength_throwsIAE() {
            assertThatThrownBy(() -> textRandomly("val#neg").alpha(-1))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void alpha_minGreaterThanMax_throwsIAE() {
            assertThatThrownBy(() -> textRandomly("val#minmax").alpha(10, 5))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void numericString_negativeRange_throwsIAE() {
            assertThatThrownBy(() -> textRandomly("val#numNeg").numericString(-3, 5))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    // ── sentence / paragraph ────────────────────────────────

    @Nested
    class SentenceAndParagraph {

        @Test
        void sentence_defaultLocale_returnsNonEmptyString() {
            String result = textRandomly("sentence#basic").sentence();

            assertThat(result).isNotNull().isNotBlank();
        }

        @Test
        void sentence_returnsNonEmptyString() {
            String result = textRandomly("sentence#basic").sentence(Locale.ENGLISH);

            assertThat(result).isNotNull().isNotBlank();
        }

        @Test
        void sentence_isDeterministic() {
            String a = textRandomly("sentence#det").sentence(Locale.ENGLISH);
            String b = textRandomly("sentence#det").sentence(Locale.ENGLISH);

            assertThat(a).isEqualTo(b);
        }

        @Test
        void sentence_nullLocale_throwsNPE() {
            assertThatThrownBy(() -> textRandomly("sentence#null").sentence(null))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        void paragraph_defaultLocale_returnsMultipleSentences() {
            String result = textRandomly("para#basic").paragraph();

            assertThat(result).isNotBlank()
                    // A paragraph should contain at least some content (3+ sentences joined by spaces)
                    .hasSizeGreaterThan(20);
        }

        @Test
        void paragraph_returnsMultipleSentences() {
            String result = textRandomly("para#basic").paragraph(Locale.ENGLISH);

            assertThat(result).isNotBlank()
                    // A paragraph should contain at least some content (3+ sentences joined by spaces)
                    .hasSizeGreaterThan(20);
        }

        @Test
        void paragraph_isDeterministic() {
            String a = textRandomly("para#det").paragraph(Locale.ENGLISH);
            String b = textRandomly("para#det").paragraph(Locale.ENGLISH);

            assertThat(a).isEqualTo(b);
        }

        @Test
        void paragraph_nullLocale_throwsNPE() {
            assertThatThrownBy(() -> textRandomly("para#null").paragraph(null))
                    .isInstanceOf(NullPointerException.class);
        }
    }

    // ── noun / verb / adjective ─────────────────────────────

    @Nested
    class Words {

        @Test
        void noun_defaultLocale_returnsNonEmptyString() {
            String result = textRandomly("noun#basic").noun();

            assertThat(result).isNotNull().isNotBlank();
        }

        @Test
        void noun_returnsNonEmptyString() {
            String result = textRandomly("noun#basic").noun(Locale.ENGLISH);

            assertThat(result).isNotNull().isNotBlank();
        }

        @Test
        void verb_defaultLocale_returnsNonEmptyString() {
            String result = textRandomly("verb#basic").verb();

            assertThat(result).isNotNull().isNotBlank();
        }

        @Test
        void verb_returnsNonEmptyString() {
            String result = textRandomly("verb#basic").verb(Locale.ENGLISH);

            assertThat(result).isNotNull().isNotBlank();
        }

        @Test
        void adjective_defaultLocale_returnsNonEmptyString() {
            String result = textRandomly("adj#basic").adjective();

            assertThat(result).isNotNull().isNotBlank();
        }

        @Test
        void adjective_returnsNonEmptyString() {
            String result = textRandomly("adj#basic").adjective(Locale.ENGLISH);

            assertThat(result).isNotNull().isNotBlank();
        }

        @Test
        void noun_isDeterministic() {
            String a = textRandomly("noun#det").noun(Locale.ENGLISH);
            String b = textRandomly("noun#det").noun(Locale.ENGLISH);

            assertThat(a).isEqualTo(b);
        }

        @Test
        void noun_nullLocale_throwsNPE() {
            assertThatThrownBy(() -> textRandomly("noun#null").noun(null))
                    .isInstanceOf(NullPointerException.class);
        }
    }

    // ── compoundName ────────────────────────────────────────

    @Nested
    class CompoundName {

        @Test
        void compoundName_defaultLocale_startsWithUpperCase() {
            String result = textRandomly("compound#basic").compoundName();

            assertThat(result).isNotNull().isNotBlank();
            assertThat(Character.isUpperCase(result.charAt(0))).isTrue();
        }

        @Test
        void compoundName_startsWithUpperCase() {
            String result = textRandomly("compound#basic").compoundName(Locale.ENGLISH);

            assertThat(result).isNotNull().isNotBlank();
            assertThat(Character.isUpperCase(result.charAt(0))).isTrue();
        }

        @Test
        void compoundName_isDeterministic() {
            String a = textRandomly("compound#det").compoundName(Locale.ENGLISH);
            String b = textRandomly("compound#det").compoundName(Locale.ENGLISH);

            assertThat(a).isEqualTo(b);
        }

        @Test
        void compoundName_containsNoSpacesOrHyphens() {
            String result = textRandomly("compound#nospace").compoundName(Locale.ENGLISH);

            assertThat(result).doesNotContain(" ", "-");
        }
    }

    // ── slug ────────────────────────────────────────────────

    @Nested
    class Slug {

        @Test
        void slug_defaultLocale_containsHyphen() {
            String result = textRandomly("slug#basic").slug();

            assertThat(result).isNotNull().isNotBlank()
                    .contains("-");
        }

        @Test
        void slug_containsHyphen() {
            String result = textRandomly("slug#basic").slug(Locale.ENGLISH);

            assertThat(result).isNotNull().isNotBlank()
                    .contains("-");
        }

        @Test
        void slug_isAllLowerCase() {
            String result = textRandomly("slug#lower").slug(Locale.ENGLISH);

            assertThat(result).isEqualTo(result.toLowerCase(Locale.ENGLISH));
        }

        @Test
        void slug_isDeterministic() {
            String a = textRandomly("slug#det").slug(Locale.ENGLISH);
            String b = textRandomly("slug#det").slug(Locale.ENGLISH);

            assertThat(a).isEqualTo(b);
        }

        @Test
        void slug_hasExactlyOneHyphen() {
            String result = textRandomly("slug#hyphens").slug(Locale.ENGLISH);

            long hyphenCount = result.chars().filter(c -> c == '-').count();
            assertThat(hyphenCount).isOne();
        }
    }
}