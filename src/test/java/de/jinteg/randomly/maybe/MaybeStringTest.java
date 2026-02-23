package de.jinteg.randomly.maybe;

import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the `MaybeString` class, testing the methods:
 * - `orNull`: Returns the supplied string if present, or `null` otherwise.
 * - `orEmpty`: Returns the supplied string if present, or an empty string otherwise.
 * - `optional`: Wraps the value in an `Optional` (empty if not present).
 * - `orElse`: Returns the supplied string if present, or a provided fallback string otherwise.
 */
class MaybeStringTest {

    @Test
    void testOrNull_WithPresentValue_ReturnsValueFromSupplier() {
        // Arrange
        Supplier<String> supplier = () -> "Sample Value";
        MaybeString maybeString = new MaybeString(true, supplier);

        // Act
        String result = maybeString.orNull();

        // Assert
        assertEquals("Sample Value", result, "Expected 'Sample Value' when present is true and supplier provides a value.");
    }

    @Test
    void testOrNull_WithAbsentValue_ReturnsNull() {
        // Arrange
        Supplier<String> supplier = () -> "Should not be used";
        MaybeString maybeString = new MaybeString(false, supplier);

        // Act
        String result = maybeString.orNull();

        // Assert
        assertNull(result, "Expected null when present is false, regardless of the supplier.");
    }

    @Test
    void testOrNull_WithSupplierReturningNull_ReturnsNullEvenWhenPresent() {
        // Arrange
        Supplier<String> supplier = () -> null;
        MaybeString maybeString = new MaybeString(true, supplier);

        // Act
        String result = maybeString.orNull();

        // Assert
        assertNull(result, "Expected null when the supplier explicitly returns null, even if present is true.");
    }

    @Test
    void testOrNull_WithAbsentValueAndNullSupplier_ReturnsNull() {
        // Arrange
        Supplier<String> supplier = () -> null;
        MaybeString maybeString = new MaybeString(false, supplier);

        // Act
        String result = maybeString.orNull();

        // Assert
        assertNull(result, "Expected null when present is false and the supplier returns null.");
    }

    @Test
    void testOrEmpty_WithPresentValue_ReturnsValueFromSupplier() {
        // Arrange
        Supplier<String> supplier = () -> "Sample Value";
        MaybeString maybeString = new MaybeString(true, supplier);

        // Act
        String result = maybeString.orEmpty();

        // Assert
        assertEquals("Sample Value", result, "Expected 'Sample Value' when present is true and supplier provides a value.");
    }

    @Test
    void testOrEmpty_WithAbsentValue_ReturnsEmptyString() {
        // Arrange
        Supplier<String> supplier = () -> "Should not be used";
        MaybeString maybeString = new MaybeString(false, supplier);

        // Act
        String result = maybeString.orEmpty();

        // Assert
        assertEquals("", result, "Expected an empty string when present is false.");
    }

    @Test
    void testOrEmpty_WithSupplierReturningNull_ReturnsEmptyString() {
        // Arrange
        Supplier<String> supplier = () -> null;
        MaybeString maybeString = new MaybeString(true, supplier);

        // Act
        String result = maybeString.orEmpty();

        // Assert
        assertEquals("", result, "Expected an empty string when supplier returns null, even if present is true.");
    }

    @Test
    void testOptional_WithPresentValue_WrapsValueInOptional() {
        // Arrange
        Supplier<String> supplier = () -> "Sample Value";
        MaybeString maybeString = new MaybeString(true, supplier);

        // Act
        Optional<String> optional = maybeString.optional();

        // Assert
        assertTrue(optional.isPresent(), "Expected the optional to be present.");
        assertEquals("Sample Value", optional.get(), "Expected 'Sample Value' to be wrapped in the optional.");
    }

    @Test
    void testOptional_WithAbsentValue_ReturnsEmptyOptional() {
        // Arrange
        Supplier<String> supplier = () -> "Should not be used";
        MaybeString maybeString = new MaybeString(false, supplier);

        // Act
        Optional<String> optional = maybeString.optional();

        // Assert
        assertTrue(optional.isEmpty(), "Expected the optional to be empty.");
    }

    @Test
    void testOptional_WithNullSupplier_ReturnsEmptyOptional() {
        // Arrange
        Supplier<String> supplier = () -> null;
        MaybeString maybeString = new MaybeString(true, supplier);

        // Act
        Optional<String> optional = maybeString.optional();

        // Assert
        assertTrue(optional.isEmpty(), "Expected the optional to be empty when supplier returns null.");
    }

    @Test
    void testOrElse_WithPresentValue_ReturnsValueFromSupplier() {
        // Arrange
        Supplier<String> supplier = () -> "Sample Value";
        MaybeString maybeString = new MaybeString(true, supplier);

        // Act
        String result = maybeString.orElse("Fallback Value");

        // Assert
        assertEquals("Sample Value", result, "Expected 'Sample Value' when present is true and supplier provides a value.");
    }

    @Test
    void testOrElse_WithAbsentValue_ReturnsFallback() {
        // Arrange
        Supplier<String> supplier = () -> "Should not be used";
        MaybeString maybeString = new MaybeString(false, supplier);

        // Act
        String result = maybeString.orElse("Fallback Value");

        // Assert
        assertEquals("Fallback Value", result, "Expected 'Fallback Value' when present is false.");
    }

    @Test
    void testOrElse_WithSupplierReturningNull_ReturnsFallback() {
        // Arrange
        Supplier<String> supplier = () -> null;
        MaybeString maybeString = new MaybeString(true, supplier);

        // Act
        String result = maybeString.orElse("Fallback Value");

        // Assert
        assertEquals("Fallback Value", result, "Expected 'Fallback Value' when supplier returns null, even if present is true.");
    }
}