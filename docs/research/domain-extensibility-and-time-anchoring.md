# Research note: Domain extensibility & time anchoring (runStart) for reproducible test data

**Date:** 2026-02-07  
**Status:** Research / discussion notes (not a decision document)

## Motivation

Practical test suites frequently rely on:
- locale-specific formats (addresses, identifiers, catalogs),
- domain-specific variations per country/market,
- and time-based values (LocalDate/LocalDateTime/Instant “now”).

If the library uses `now()` directly, rerunning a failing test with the same seed may still produce different date/time values. This harms reproducibility and debugging.

Additionally, to increase adoption, users may want to extend/override catalogs for their country/format without waiting for upstream changes.

## 1) Locale as the primary selection mechanism

### Principle
Use `Locale` as the primary driver for:
- selecting catalogs,
- choosing format conventions,
- and picking market-/country-specific variants where feasible.

### API consequence
Prefer a single consistent entry point for overrides:

- `r.withLocale(Locale.GERMANY).finance().stock()`

This keeps locale handling uniform across all domains (finance, address, person, ...).

## 2) Domain variability (e.g., German stocks have WKN)

### Problem
Different markets/countries have different identifiers and conventions:
- Germany: WKN is common
- US: typically no WKN
- Other countries may have other identifiers

### Options
A) **Single record with optional fields**
- e.g. `wkn` may be null/absent depending on locale.

B) **Variant types (sealed interface)**
- `GermanStock` vs `UsStock` etc.
- More type-safe, but more API surface.

C) **Scope-specific generator APIs**
- `finance.stock()` uses locale conventions
- later: optional `finance.stockMarket(StockMarket.DE)` if needed

### Recommendation (initial)
Start with **locale-driven** `finance.stock()` and add market-specific APIs only if real usage demands it.
Keep the internal design flexible enough to add variants later without breaking the public API.

## 3) Catalog customization by users

### Goal
Allow users to extend/override domain catalogs for their locale/country conventions.

### Stage 1 (recommended first): Classpath resource override
Users can ship additional `.properties` catalogs on the classpath under the same base paths.
This is:
- dependency-free,
- deterministic,
- naturally cacheable,
- and works well with build tooling.

We should clarify and document:
- precedence rules (application resources override library defaults?),
- merge strategy (override vs union),
- and deterministic ordering requirements.

### Stage 2 (future): Pluggable catalog source (SPI)
Provide an interface like `CatalogSource` to load catalogs from other sources (JSON/YAML/DB/etc.).
This is powerful but adds complexity (security, caching, deterministic ordering).

### Recommendation
Document Stage 1 as the supported customization mechanism now; revisit Stage 2 later.

## 4) Time anchoring: `runStart` for reproducible time-based values

### Problem
Using `LocalDate.now()` / `Instant.now()` makes reproducing failures difficult even with the same seed.

### Proposed solution
Introduce a **run anchor timestamp** called `runStart`:
- It is determined once per test run / JVM process.
- All time-based generators should use `runStart` instead of calling `now()` directly.
- For reproduction, users provide both:
    - `jrandomly.seed`
    - `jrandomly.runStart`

### Configuration keys
- System property: `jrandomly.runStart`
- Environment variable: `JRANDOMLY_RUN_START`
- Suggested format: ISO-8601 Instant, e.g. `2026-02-07T12:34:56Z`

### Benefits
- Full reproduction of time-related sequences across runs.
- No flaky tests due to timing or midnight boundaries.

## 5) Interactions with determinism and parallelism

- `scoped(scope)` + root seed provides parallel-stable determinism for RNG sequences.
- `runStart` provides stability for time-based values across runs.
- Both should be derivable and observable for debugging (loggable / exposable via API).

## Next research questions

- Should `runStart` be global per JVM or derived per scope?
    - Global per JVM is simpler and matches “test run” semantics.
- Should `withLocale(locale)` create a deterministic substream (current decision: yes).
- Should time generators derive dedicated substreams (e.g. `"time"`) to isolate sequences from other domains?