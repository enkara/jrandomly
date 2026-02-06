# ADR 0001: JRandomly – deterministic seeds, locale catalogs, and a `maybe()` edge-case API

**Status:** Accepted  
**Date:** 2026-02-06

## Context

We are building a small Java library (published as a Maven artifact) that generates both:
- simple random values (booleans, numbers, strings, dates/times, enums, collections), and
- more realistic, domain-oriented test data (e.g., finance/person/address modules).

Key requirements:

- **Reproducibility:** A seed must reproduce the same data to debug and re-run failing tests.
- **Convenient defaults:** `JRandomly.randomly()` should “just work”.
- **External seed injection:** A seed must be injectable from outside (system properties / environment) without modifying test classes.
- **Locale awareness:** Domain catalogs should be locale-dependent (start with EN/DE) and extendable via classpath resources.
- **Opt-in edge cases:** Ability to intentionally generate `null`, empty strings, or `Optional.empty()` in a controlled, reproducible way.

## Decision

### 1) Instantiation / defaults

- `JRandomly.randomly()` returns a **new instance on each call** (no singleton).
- Default configuration is loaded when creating an instance from external sources:
  - System properties (e.g., `jrandomly.seed`, `jrandomly.locale`, `jrandomly.maybeRate`, …)
  - Environment variables (e.g., `JRANDOMLY_SEED`, `JRANDOMLY_LOCALE`, `JRANDOMLY_MAYBE_RATE`, …)
  - (Later/optional): classpath `*.properties` resources

**Rationale:** New instances avoid global state, test-order coupling, and concurrency surprises.

### 2) Seed and reproducibility

- Each `JRandomly` instance is driven by **a single source of randomness**.
- When a global/root seed is provided externally, instance seeds are **derived deterministically** so that:
  - the whole run is reproducible, and
  - different `randomly()` instances do not produce identical sequences.

**Rationale:** Large test suites create many instances in different places; external seeding must still be deterministic and diverse.

### 3) API naming: no `random...` prefixes

- Methods should not be prefixed with `random...` (e.g., `intBetween`, `stockSymbol`, `enumValue`, …).

**Rationale:** The instance is already “random”; adding `random` everywhere is redundant and harms readability/autocomplete.

### 4) Opt-in edge cases via `maybe()` (fluent API)

- Normal API methods return direct values (`String`, `int`, …) and are **always present**.
- Edge-case testing is enabled explicitly via:
  - `maybe()` using a default `maybeRate`
  - `maybe(double absentProbability)` overriding the probability
- In `maybe()` mode, generators return wrapper/spec types that can be materialized as:
  - `.orNull()` → `T` or `null`
  - `.orEmpty()` → for text: present value or `""` when absent
  - `.optional()` → `Optional<T>` (empty when absent)
  - `.orElse(T fallback)` → present value or fallback when absent

**Default maybeRate:** `0.125` (12.5%, one eighth)  
**Meaning:** `absentProbability` is the probability to produce an absent value.

**Rationale:** Normal usage stays ergonomic; edge cases are explicit and reproducible without Optional boilerplate.

### 5) Locale-aware domain catalogs via resources

- Domain data is loaded from classpath resources per locale and is extendable without code changes.
- Preferred format: numbered keys for stable ordering, e.g.:
  - `stockSymbols.1=AAPL`
  - `stockSymbols.2=MSFT`

**Rationale:** Stable ordering improves deterministic selection; resource bundles allow locale fallback.

## Consequences

- Failing tests can be reproduced by setting `jrandomly.seed` / `JRANDOMLY_SEED`.
- `JRandomly.randomly()` works everywhere with defaults.
- No unexpected nulls in the normal API; edge cases are opt-in via `maybe()`.
- Domain catalogs can be extended by adding resource files (EN/DE first).

## Next steps

- Finalize the exact property/env key names and precedence.
- Specify the seed-derivation algorithm from the root seed.
- Add caching for parsed catalogs per locale (performance).
- Evaluate locale integration for any external data providers (if used).