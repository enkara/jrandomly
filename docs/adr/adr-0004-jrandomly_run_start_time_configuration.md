# ADR 0004: JRandomly – `runStartTime` time anchoring for reproducible date/time generation

**Status:** Accepted (updated 2026-02-14: renamed `runStart` → `runStartTime`)  
**Date:** 2026-02-07

## Context

JRandomly generates test data that must be reproducible to debug failing test runs.

A root seed alone is insufficient to reproduce time-based values if the implementation uses:

- `LocalDate.now()`
- `LocalDateTime.now()`
- `Instant.now()`

Two runs with the same seed but different wall-clock time can produce different date/time values.

We need a stable reference timestamp per test run so that time-based generators behave deterministically across reruns.

## Decision

### 1) Introduce `runStartTime` (a run anchor timestamp)

JRandomly configuration includes a `runStartTime` timestamp:

- Type: `Instant`
- Meaning: the reference "now" used by all time-based generators

### 2) External configuration (system properties override environment)

`runStartTime` can be injected externally without touching test code:

- System property: `jrandomly.runStartTime`
- Environment variable: `JRANDOMLY_RUN_START_TIME`

**Precedence:** system properties > environment > default

### 3) Default behavior

If `runStartTime` is not provided externally, JRandomly will compute a default:

- `runStartTime = Instant.now()` captured once per JVM/test run (implementation must cache it)
- This default is deterministic within the run but not reproducible across different runs unless `runStartTime` is provided externally.

### 4) Implementation rule

All time-related generators must use `runStartTime` and must not call `now()` directly.

### 5) Observability / replay

- Each `JRandomly` instance exposes `replayInfo()` returning a CLI-friendly string containing seed, runStartTime, and locale.
- Instances log their configuration at creation time (via `System.Logger`).

## Consequences

### Positive

- Failing tests can be reproduced with: `jrandomly.seed` + `jrandomly.runStartTime`
- Eliminates flakiness from wall-clock timing and midnight boundaries.
- `replayInfo()` makes it trivial to capture and re-use configuration.

### Trade-offs

- Requires defining a parsing format and documentation for `runStartTime`.
- Introduces an additional configuration value users may need for perfect reproduction.

## Notes

- `runStartTime` parsing format: ISO-8601 Instant (`Z`) or OffsetDateTime (`+/-HH:MM`).
- Time zone handling: `dateTime()` defaults to `ZoneId.systemDefault()`, overridable via `dateTime(ZoneId)`.