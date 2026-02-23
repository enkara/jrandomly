package de.jinteg.randomly.maybe;

import java.util.Optional;
import java.util.function.Supplier;

/**
 * Represents an optional value that may or may not be present.
 *
 * @param <T> type of the value
 */
public final class Maybe<T> {
    private final boolean present;
    private final Supplier<T> supplier;

    /**
     * Constructs a Maybe instance with the given presence and supplier.
     *
     * @param present  whether the value is present
     * @param supplier supplier for the value
     */
    public Maybe(boolean present, Supplier<T> supplier) {
        this.present = present;
        this.supplier = supplier;
    }

    /**
     * Returns a Maybe instance with the given value.
     *
     * @param value value to wrap
     * @param <T>   type of the value
     * @return Maybe instance
     */
    public static <T> Maybe<T> of(T value) {
        return new Maybe<>(true, () -> value);
    }

    /**
     * Returns a Maybe instance with the given value, if non-null.
     *
     * @param value value to wrap
     * @param <T>   type of the value
     * @return Maybe instance
     */
    public static <T> Maybe<T> ofNullable(T value) {
        return new Maybe<>(value != null, () -> value);
    }

    /**
     * Returns the wrapped value, or null if the value is not present.
     *
     * @return wrapped value or null
     */
    public T orNull() {
        return present ? supplier.get() : null;
    }

    /**
     * Returns the wrapped value as an Optional.
     *
     * @return Optional instance
     */
    public Optional<T> optional() {
        return present ? Optional.ofNullable(supplier.get()) : Optional.empty();
    }

    /**
     * Returns the wrapped value, or the given fallback value if the value is not present.
     *
     * @param fallback fallback value
     * @return wrapped value or fallback
     */
    public T orElse(T fallback) {
        if (!present) {
            return fallback;
        }
        T value = supplier.get();
        return value != null ? value : fallback;
    }

}