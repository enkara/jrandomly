package de.jinteg.randomly.maybe;

import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;

class MaybeTest {

    @Test
    void orNull_present_returnsSupplierValue() {
        Supplier<String> supplier = () -> "X";
        Maybe<String> m = new Maybe<>(true, supplier);

        assertEquals("X", m.orNull());
    }

    @Test
    void orNull_absent_returnsNull() {
        Supplier<String> supplier = () -> "X";
        Maybe<String> m = new Maybe<>(false, supplier);

        assertNull(m.orNull());
    }

    @Test
    void optional_present_wrapsValue() {
        Supplier<Integer> supplier = () -> 42;
        Maybe<Integer> m = new Maybe<>(true, supplier);

        Optional<Integer> opt = m.optional();
        assertTrue(opt.isPresent());
        assertEquals(42, opt.get());
    }

    @Test
    void optional_absent_empty() {
        Supplier<Integer> supplier = () -> 42;
        Maybe<Integer> m = new Maybe<>(false, supplier);

        assertTrue(m.optional().isEmpty());
    }

    @Test
    void orElse_present_nullSupplier_returnsFallback() {
        Supplier<String> supplier = () -> null;
        Maybe<String> m = new Maybe<>(true, supplier);

        assertEquals("fallback", m.orElse("fallback"));
    }

    @Test
    void orElse_absent_returnsFallback() {
        Supplier<String> supplier = () -> "X";
        Maybe<String> m = new Maybe<>(false, supplier);

        assertEquals("fallback", m.orElse("fallback"));
    }

    @Test
    void of_and_ofNullable() {
        assertEquals("A", Maybe.of("A").orNull());
        assertNull(Maybe.ofNullable(null).orNull());
    }
}