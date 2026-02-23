package de.jinteg.randomly.maybe;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.random.RandomGenerator;

import static org.junit.jupiter.api.Assertions.assertTrue;

class MaybeContextTest {

    @Test
    void default_maybeRate_isApproximatelyApplied() {
        double absentProbability = 0.125;
        RandomGenerator rng = RandomGenerator.getDefault();
        MaybeContext ctx = new MaybeContext(rng, absentProbability);

        int trials = 20_000;
        int presentCount = 0;

        for (int i = 0; i < trials; i++) {
            String v = ctx.text("X").orEmpty();
            if (!v.isEmpty()) {
                presentCount++;
            }
        }

        double presentRate = presentCount / (double) trials;
        double absentRate = 1.0 - presentRate;

        assertTrue(absentRate >= 0.10 && absentRate <= 0.15,
                "Absent-Rate above tolerated value: " + absentRate);
    }

    @ParameterizedTest(name = "maybeRate is applied (presentRate≈{0}, absentRate≈{1})")
    @CsvSource({
            "0.675, 0.325, 0.30, 0.35"
    })
    void maybeRate_isApproximatelyApplied(double expectedPresent, double expectedAbsent,
                                          double minAbsent, double maxAbsent) {
        RandomGenerator rng = RandomGenerator.getDefault();
        MaybeContext ctx = new MaybeContext(rng, expectedAbsent);

        int trials = 20_000;
        int presentCount = 0;

        for (int i = 0; i < trials; i++) {
            String v = ctx.text("X").orEmpty();
            if (!v.isEmpty()) {
                presentCount++;
            }
        }

        double presentRate = presentCount / (double) trials;
        double absentRate = 1.0 - presentRate;

        assertTrue(absentRate >= minAbsent && absentRate <= maxAbsent,
                "Absent-Rate above tolerated value: " + absentRate);
    }
}