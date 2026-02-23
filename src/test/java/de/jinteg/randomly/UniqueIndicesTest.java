package de.jinteg.randomly;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UniqueIndicesTest {

    @BeforeEach
    void setUp() {
        System.setProperty("jrandomly.seed", "42");
    }

    @AfterEach
    void cleanup() {
        System.clearProperty("jrandomly.seed");
    }

    @Test
    void uniqueIndices_returnsCorrectCount() {
        int[] result = JRandomly.randomly("unique#count").uniqueIndices(5, 16);

        assertThat(result).hasSize(5);
    }

    @Test
    void uniqueIndices_allValuesAreUnique() {
        int[] result = JRandomly.randomly("unique#unique").uniqueIndices(5, 16);

        assertThat(Arrays.stream(result).distinct().count()).isEqualTo(5);
    }

    @Test
    void uniqueIndices_allValuesInRange() {
        int[] result = JRandomly.randomly("unique#range").uniqueIndices(5, 16);

        assertThat(Arrays.stream(result).boxed().toList())
                .allSatisfy(v -> assertThat(v).isBetween(0, 15));
    }

    @Test
    void uniqueIndices_isDeterministic() {
        int[] a = JRandomly.randomly("unique#det").uniqueIndices(5, 16);
        int[] b = JRandomly.randomly("unique#det").uniqueIndices(5, 16);

        assertThat(a).isEqualTo(b);
    }

    @Test
    void uniqueIndices_countEqualsUpperBound_returnsAllValues() {
        int[] result = JRandomly.randomly("unique#all").uniqueIndices(5, 5);

        assertThat(result).hasSize(5);
        assertThat(Arrays.stream(result).sorted().toArray()).isEqualTo(new int[]{0, 1, 2, 3, 4});
    }

    @Test
    void uniqueIndices_zeroCount_returnsEmptyArray() {
        int[] result = JRandomly.randomly("unique#zero").uniqueIndices(0, 10);

        assertThat(result).isEmpty();
    }

    @Test
    void uniqueIndices_countExceedsUpperBound_throwsIAE() {
        assertThatThrownBy(() -> JRandomly.randomly("unique#overflow").uniqueIndices(10, 5))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("count (10) exceeds upperBound (5)");
    }

    @Test
    void uniqueIndices_negativeCount_throwsIAE() {
        assertThatThrownBy(() -> JRandomly.randomly("unique#neg").uniqueIndices(-1, 10))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void uniqueIndices_zeroUpperBound_throwsIAE() {
        assertThatThrownBy(() -> JRandomly.randomly("unique#zeroBound").uniqueIndices(1, 0))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void uniqueIndices_application() {
        List<String> characters = List.of("A", "B", "C", "D", "E", "F", "G", "H", "I", "J");
        JRandomly randomly = JRandomly.randomly("uniqueIndices#app1");

        int[] ints = randomly.uniqueIndices(4, characters.size());

        // Größe & Uniqueness
        assertThat(ints)
                .hasSize(4)
                .doesNotHaveDuplicates();

        // Bereichsprüfung
        assertThat(IntStream.of(ints).allMatch(i -> i >= 0 && i < characters.size()))
                .isTrue();

        List<String> selected = IntStream.of(ints)
                .mapToObj(characters::get)
                .toList();

        assertThat(selected).hasSize(4)
                .allMatch(characters::contains);
    }
}