package de.jinteg.randomly.internal.catalog;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class RawParserUtilTest {

    @Test
    void parse_validLine() {
        String[] parts = RawParserUtil.parse("A|B|C", 3);
        assertThat(parts).containsExactly("A", "B", "C");
    }

    @Test
    void parse_tooFewColumns_throwsException() {
        assertThatThrownBy(() -> RawParserUtil.parse("A|B", 3))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("expected 3 columns");
    }

    @Test
    void parse_nullInput_throwsException() {
        assertThatThrownBy(() -> RawParserUtil.parse(null, 2))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("null");
    }

    @Test
    void parse_invalidExpectedColumns_throwsException() {
        assertThatThrownBy(() -> RawParserUtil.parse("A|B", 0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("expectedColumns");
    }

    @Test
    void parse_extraColumns_areMergedInLastPart() {
        // split with limit: "A|B|C|D" with expectedColumns=3 → ["A", "B", "C|D"]
        String[] parts = RawParserUtil.parse("A|B|C|D", 3);
        assertThat(parts).hasSize(3);
        assertThat(parts[2]).isEqualTo("C|D");
    }
}