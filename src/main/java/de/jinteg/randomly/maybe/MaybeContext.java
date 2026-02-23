package de.jinteg.randomly.maybe;

import java.util.function.Supplier;
import java.util.random.RandomGenerator;

/**
 * Context for generating Maybe instances with a given probability of absence.
 */
public final class MaybeContext {
    private final RandomGenerator rng;
    private final double absentProbability;

    /**
     * Constructor.
     *
     * @param rng               random number generator
     * @param absentProbability probability of absence
     */
    public MaybeContext(RandomGenerator rng, double absentProbability) {
        if (absentProbability < 0.0 || absentProbability > 1.0) {
            throw new IllegalArgumentException("absentProbability must be in range [0.0, 1.0] but was " + absentProbability);
        }
        this.rng = rng;
        this.absentProbability = absentProbability;
    }

    /**
     * Returns a Maybe instance with the given supplier.
     *
     * @param supplier supplier for the value
     * @param <T>      type of the value
     * @return Maybe instance
     */
    public <T> Maybe<T> value(Supplier<T> supplier) {
        boolean present = rng.nextDouble() >= absentProbability;
        return new Maybe<>(present, supplier);
    }

    /**
     * Returns a Maybe instance with the given value.
     *
     * @param value value to wrap
     * @param <T>   type of the value
     * @return Maybe instance
     */
    public <T> Maybe<T> value(T value) {
        return value(() -> value);
    }

    /**
     * Returns a MaybeString instance with the given supplier.
     *
     * @param supplier supplier for the value
     * @return MaybeString instance
     */
    public MaybeString text(Supplier<String> supplier) {
        boolean present = rng.nextDouble() >= absentProbability;
        return new MaybeString(present, supplier);
    }

    /**
     * Returns a MaybeString instance with the given value.
     *
     * @param value value to wrap
     * @return MaybeString instance
     */
    public MaybeString text(String value) {
        return text(() -> value);
    }
}