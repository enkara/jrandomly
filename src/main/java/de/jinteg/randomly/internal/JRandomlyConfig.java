package de.jinteg.randomly.internal;

import java.time.Instant;
import java.util.Locale;
import java.util.Optional;

/**
 * Configuration for JRandomly.
 *
 * @param rootSeed     random seed
 * @param locale       locale
 * @param maybeRate    probability of generating a random value
 * @param runStartTime start time of the random generation run
 */
public record JRandomlyConfig(
        Optional<Long> rootSeed,
        Locale locale,
        double maybeRate,
        Instant runStartTime
) {
    public static final String PROP_SEED = "jrandomly.seed";
    public static final String PROP_LOCALE = "jrandomly.locale";
    public static final String PROP_MAYBE_RATE = "jrandomly.maybeRate";
    public static final String PROP_RUN_START_TIME = "jrandomly.runStartTime";

    public static final String ENV_SEED = "JRANDOMLY_SEED";
    public static final String ENV_LOCALE = "JRANDOMLY_LOCALE";
    public static final String ENV_MAYBE_RATE = "JRANDOMLY_MAYBE_RATE";
    public static final String ENV_RUN_START_TIME = "JRANDOMLY_RUN_START_TIME";

    public static final double DEFAULT_MAYBE_RATE = 0.125;

    /**
     * Constructor.
     *
     * @param rootSeed     random root seed
     * @param locale       locale
     * @param maybeRate    maybe rate
     * @param runStartTime start time of the random generation run
     */
    public JRandomlyConfig {
        if (maybeRate < 0.0 || maybeRate > 1.0) {
            throw new IllegalArgumentException("maybeRate must be in range [0.0, 1.0] but was " + maybeRate);
        }
        if (runStartTime == null) {
            throw new IllegalArgumentException("runStartTime must not be null");
        }
    }

    @Override
    public String toString() {
        return "JRandomlyConfig{" +
                "rootSeed=" + rootSeed +
                ", locale=" + locale +
                ", maybeRate=" + maybeRate +
                ", runStartTime=" + runStartTime +
                '}';
    }
}