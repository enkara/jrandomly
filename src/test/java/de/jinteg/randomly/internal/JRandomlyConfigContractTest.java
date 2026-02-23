package de.jinteg.randomly.internal;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.RecordComponent;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class JRandomlyConfigContractTest {

    @Test
    void record_components_are_expected_and_complete() {
        Class<?> type = JRandomlyConfig.class;

        assertThat(type.isRecord())
                .as("JRandomlyConfig should remain a record (Contract-Test)")
                .isTrue();

        List<String> actual = Arrays.stream(type.getRecordComponents())
                .map(RecordComponent::getName)
                .toList();

        assertThat(actual).containsExactly(
                "rootSeed",
                "locale",
                "maybeRate",
                "runStartTime"
        );
    }

    @Test
    void record_component_types_are_expected() {
        RecordComponent[] comps = JRandomlyConfig.class.getRecordComponents();

        assertThat(comps).extracting(RecordComponent::getName)
                .containsExactly("rootSeed", "locale", "maybeRate", "runStartTime");

        assertThat(comps[0].getType()).isEqualTo(java.util.Optional.class);
        assertThat(comps[1].getType()).isEqualTo(java.util.Locale.class);
        assertThat(comps[2].getType()).isEqualTo(double.class);
        assertThat(comps[3].getType()).isEqualTo(Instant.class);
    }

    @Test
    void public_constants_exist_and_values_match() throws Exception {
        Map<String, Object> expected = Map.ofEntries(
                Map.entry("PROP_SEED", "jrandomly.seed"),
                Map.entry("PROP_LOCALE", "jrandomly.locale"),
                Map.entry("PROP_MAYBE_RATE", "jrandomly.maybeRate"),

                Map.entry("ENV_SEED", "JRANDOMLY_SEED"),
                Map.entry("ENV_LOCALE", "JRANDOMLY_LOCALE"),
                Map.entry("ENV_MAYBE_RATE", "JRANDOMLY_MAYBE_RATE"),

                Map.entry("DEFAULT_MAYBE_RATE", 0.125d)
        );

        Class<?> type = JRandomlyConfig.class;

        for (var e : expected.entrySet()) {
            Field f = type.getDeclaredField(e.getKey());

            assertThat(Modifier.isPublic(f.getModifiers())).isTrue();
            assertThat(Modifier.isStatic(f.getModifiers())).isTrue();
            assertThat(Modifier.isFinal(f.getModifiers())).isTrue();

            Object value = f.get(null); // static field
            assertThat(value).isEqualTo(e.getValue());
        }
    }

}