package de.jinteg.randomly.core;

import de.jinteg.randomly.JRandomly;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.UUID;

class IdRandomlyTest {

    private JRandomly randomly = JRandomly.randomly("IdRandomlyTest");

    @Test
    void uuid() {
        UUID uuid = randomly.id().uuid();
        Assertions.assertThat(uuid).isNotNull();
        Assertions.assertThat(uuid.toString()).hasSize(36);
    }

    @Test
    void longId() {
        long l = randomly.id().longId();
        Assertions.assertThat(l).isGreaterThanOrEqualTo(0)
                .isLessThanOrEqualTo(Long.MAX_VALUE);
    }

    @Test
    void longIdBetween() {
        long l = randomly.id().longIdBetween(1000, 2000);
        Assertions.assertThat(l).isGreaterThanOrEqualTo(1000)
                .isLessThan(2000);
    }

    @Test
    void intId() {
        int i = randomly.id().intId();
        Assertions.assertThat(i).isGreaterThanOrEqualTo(0)
                .isLessThan(Integer.MAX_VALUE);
    }

    @Test
    void intIdBetween() {
        int i = randomly.id().intIdBetween(1000, 2000);
        Assertions.assertThat(i).isGreaterThanOrEqualTo(1000)
                .isLessThan(2000);
    }

    @Test
    void prefixedId() {
        String prefix = "PREFIX";
        String id = randomly.id().prefixedId(prefix, 10);
        Assertions.assertThat(id).startsWith(prefix)
                .hasSize(10);

    }
}