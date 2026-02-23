package de.jinteg.randomly;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import static org.assertj.core.api.Assertions.assertThat;

class JRandomlyScopedDeterminismTest {

    @AfterEach
    void cleanup() {
        System.clearProperty("jrandomly.seed");
    }

    @Test
    void sameRootSeedAndSameScope_producesSameSequence() {
        System.setProperty("jrandomly.seed", "123456");

        JRandomly a = JRandomly.randomly("MyTest#caseA");
        JRandomly b = JRandomly.randomly("MyTest#caseA");

        List<Integer> seqA = new ArrayList<>();
        List<Integer> seqB = new ArrayList<>();

        for (int i = 0; i < 20; i++) {
            seqA.add(a.intBetween(0, 1_000_000));
            seqB.add(b.intBetween(0, 1_000_000));
        }

        assertThat(seqA).isEqualTo(seqB);
    }

    @Test
    void sameRootSeedDifferentScopes_produceDifferentSequencesWithHighProbability() {
        System.setProperty("jrandomly.seed", "123456");

        JRandomly a = JRandomly.randomly("ScopeA");
        JRandomly b = JRandomly.randomly("ScopeB");

        List<Integer> seqA = new ArrayList<>();
        List<Integer> seqB = new ArrayList<>();

        for (int i = 0; i < 20; i++) {
            seqA.add(a.intBetween(0, 1_000_000));
            seqB.add(b.intBetween(0, 1_000_000));
        }

        assertThat(seqA).isNotEqualTo(seqB);
    }

    @Test
    void parallelCalls_sameScope_sameSequenceRegardlessOfInterleavings() throws Exception {
        System.setProperty("jrandomly.seed", "999");

        int threads = 8;
        int n = 50;

        ExecutorService pool = Executors.newFixedThreadPool(threads);
        try {
            List<Callable<List<Integer>>> tasks = new ArrayList<>();
            for (int t = 0; t < threads; t++) {
                tasks.add(() -> {
                    JRandomly r = JRandomly.randomly("ParallelTest#scope");
                    List<Integer> seq = new ArrayList<>();
                    for (int i = 0; i < n; i++) {
                        seq.add(r.intBetween(0, 10_000_000));
                    }
                    return seq;
                });
            }

            List<Future<List<Integer>>> results = pool.invokeAll(tasks);

            List<Integer> first = results.getFirst().get();
            for (Future<List<Integer>> f : results) {
                assertThat(f.get()).isEqualTo(first);
            }
        } finally {
            pool.shutdownNow();
            pool.awaitTermination(2, TimeUnit.SECONDS);
        }
    }

    @Test
    void callOrderDoesNotMatter_perScope() {
        System.setProperty("jrandomly.seed", "42");

        JRandomly a1 = JRandomly.randomly("A");
        JRandomly b1 = JRandomly.randomly("B");

        int a1First = a1.intBetween(0, Integer.MAX_VALUE);
        int b1First = b1.intBetween(0, Integer.MAX_VALUE);

        // reverse creation order
        JRandomly b2 = JRandomly.randomly("B");
        JRandomly a2 = JRandomly.randomly("A");

        int b2First = b2.intBetween(0, Integer.MAX_VALUE);
        int a2First = a2.intBetween(0, Integer.MAX_VALUE);

        assertThat(a2First).isEqualTo(a1First);
        assertThat(b2First).isEqualTo(b1First);
    }
}