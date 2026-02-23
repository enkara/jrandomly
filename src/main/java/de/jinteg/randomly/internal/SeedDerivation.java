package de.jinteg.randomly.internal;

import java.nio.charset.StandardCharsets;

/**
 * Utility class for deriving seeds for different scopes and purposes.
 */
public final class SeedDerivation {

    private SeedDerivation() {
    }

    /**
     * Derive a seed for a given scope.
     *
     * @param rootSeed root seed
     * @param scope    scope for which to derive the seed
     * @return derived seed
     */
    public static long seedForScope(long rootSeed, String scope) {
        if (scope == null || scope.isBlank()) {
            throw new IllegalArgumentException("scope must be non-null and non-blank");
        }
        long scopeHash = fnv1a64(scope);
        long x = rootSeed ^ scopeHash;
        return mix64(x);
    }

    /**
     * Derive a seed for a substream within a scope.
     *
     * @param baseSeed base seed
     * @param purpose  purpose for which to derive the seed
     * @return derived seed
     */
    public static long seedForSubstream(long baseSeed, String purpose) {
        if (purpose == null || purpose.isBlank()) {
            throw new IllegalArgumentException("purpose must be non-null and non-blank");
        }
        return mix64(baseSeed ^ fnv1a64(purpose));
    }

    /**
     * For non-reproducible mode (no external root seed): derive a seed from runtime entropy.
     * This does NOT guarantee stability across runs.
     *
     * @param entropy entropy to mix
     * @return derived seed
     */
    public static long seedFromEntropy(long entropy) {
        return mix64(entropy);
    }

    static long fnv1a64(String s) {
        byte[] bytes = s.getBytes(StandardCharsets.UTF_8);
        long hash = 0xcbf29ce484222325L;
        for (byte b : bytes) {
            hash ^= (b & 0xff);
            hash *= 0x100000001b3L;
        }
        return hash;
    }

    static long mix64(long z) {
        z = (z ^ (z >>> 30)) * 0xbf58476d1ce4e5b9L;
        z = (z ^ (z >>> 27)) * 0x94d049bb133111ebL;
        return z ^ (z >>> 31);
    }
}