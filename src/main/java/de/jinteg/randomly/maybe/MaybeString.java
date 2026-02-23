package de.jinteg.randomly.maybe;

import java.util.Optional;
import java.util.function.Supplier;

public final class MaybeString {
    private final Maybe<String> delegate;

    /**
     * Constructor.
     *
     * @param present  whether the value is present
     * @param supplier supplier for the value
     */
    public MaybeString(boolean present, Supplier<String> supplier) {
        this.delegate = new Maybe<>(present, supplier);
    }

    /**
     * Returns the wrapped value, or null if the value is not present.
     *
     * @return wrapped value or null
     */
    public String orNull() {
        return delegate.orNull();
    }

    /**
     * Returns the wrapped value, or an empty string if the value is not present.
     *
     * @return wrapped value or empty string
     */
    public String orEmpty() {
        String v = delegate.orNull();
        return v == null ? "" : v;
    }

    /**
     * Returns the wrapped value as an Optional.
     *
     * @return Optional instance
     */
    public Optional<String> optional() {
        return delegate.optional();
    }

    /**
     * Returns the wrapped value, or the given fallback value if the value is not present.
     *
     * @param fallback fallback value
     * @return wrapped value or fallback
     */
    public String orElse(String fallback) {
        return delegate.orElse(fallback);
    }
}
