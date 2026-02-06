# ADR 0002: JRandomly – parallel-safe determinism via scoped seeds

**Status:** Accepted  
**Date:** 2026-02-06

## Context

JRandomly must be a reliable tool for test suites that commonly run with parallel execution (JUnit parallel, build tools, CI runners).

We already decided in ADR-0001 that:
- a root seed can be injected externally (system properties > environment),
- `JRandomly.randomly()` returns a new instance each call,
- each instance uses a single RNG source,
- and instance seeds must be derived deterministically from the root seed to avoid identical sequences.

However, parallel execution makes *call-order-based* derivation strategies (e.g., "instance #1, #2, #3") non-deterministic across runs because:
- thread scheduling can change the order in which instances are created,
- tests may start in different orders between runs,
- and therefore the mapping “root seed → instance seed sequence” becomes unstable.

We need a deterministic approach that remains stable under concurrency and does not depend on instance creation order.

## Decision

### 1) Introduce `JRandomly.scoped(String scope)` in core

We add a core API entry point:

- `JRandomly.scoped(String scope)`

It creates a new `JRandomly` instance whose seed is derived deterministically from:
- the externally injected root seed (if present), and
- the provided `scope` identifier.

**Rationale:** A stable, user-controlled scope (e.g., a test id like `MyTest#shouldCreateOrder`) yields a stable sequence regardless of parallel scheduling.

### 2) Deterministic seed derivation is scope-based (not call-order-based)

When a root seed is provided, the instance seed is derived as:

- `instanceSeed = mix64(rootSeed XOR hash64(scope))`

Where:
- `hash64(scope)` is a deterministic 64-bit hash of the `scope` string (UTF-8 bytes).
- `mix64(x)` is a well-defined 64-bit mixing function (avalanche) to distribute bits well.

**Scope rules:**
- `scope` must be non-null and non-blank.
- `scope` is case-sensitive.
- `scope` is hashed using UTF-8 encoding without locale-sensitive transformations.

**Rationale:** This makes the result independent from thread scheduling and call order while keeping the API test-framework-agnostic.

### 3) Behavior when no root seed is provided

- If no root seed is provided externally, `scoped(scope)` still returns a valid instance.
- In this case, the seed is derived from an internal entropy source (implementation-defined) and the scope may be used as additional salt, but **cross-run reproducibility is not guaranteed** without a root seed.

**Rationale:** Reproducibility requires a stable external root seed; scope alone cannot guarantee identical runs if the root seed is absent.

### 4) Relationship to `JRandomly.randomly()`

- `JRandomly.randomly()` remains available as a convenience API returning a new instance with default config.
- Under a root seed, `randomly()` may use a derived instance seed, but **it is not guaranteed to be stable across parallel runs** if the derivation depends on instance creation order.
- Therefore, for parallel-stable determinism, `scoped(scope)` is the recommended API.

**Rationale:** Keep the “just works” experience, while providing a robust parallel-safe option.

## Consequences

### Positive
- Deterministic, parallel-safe sequences are achievable:
    - same root seed + same scope ⇒ same sequence across runs,
    - independent of thread scheduling and test execution order.
- No test framework lock-in: any caller can construct a stable scope id.
- Enables higher confidence in reproducing failing tests in CI.

### Trade-offs
- Callers must provide stable and unique scopes to avoid accidental collisions.
- Two different scopes could theoretically collide at the hash level (extremely unlikely with 64-bit hashing; acceptable for test data).
- Without a root seed, reproducibility across runs is not guaranteed (by design).

## Testing strategy

Add tests that verify:

1) **Scope determinism**
- Given a fixed root seed and a fixed scope, `scoped(scope)` always produces the same first N values across runs.

2) **Parallel stability**
- In a multi-threaded test, multiple threads calling `scoped("A")` produce identical sequences for "A" regardless of interleavings.
- Different scopes ("A" vs "B") produce different sequences (high probability).

3) **Non-dependence on call order**
- Creating instances in different orders (A then B vs B then A) yields identical results per scope.

## Next steps

- Finalize and document the exact `hash64` and `mix64` functions in the implementation (must be stable across versions).
- Implement `JRandomly.scoped(String scope)` and ensure it uses the same config resolution as `randomly()`.
- Update README with guidance:
    - use external root seed + `scoped(scope)` for parallel-safe determinism.