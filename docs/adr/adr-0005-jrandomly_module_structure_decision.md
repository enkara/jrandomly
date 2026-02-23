# ADR-0005: Module structure for core utilities and domains

**Status:** Accepted  
**Date:** 2026-02-14

## Context

`JRandomly` is the main entry point (`JRandomly.randomly()` / `scoped(scope)`) providing deterministic randomness, locale substreams, and domain generators (e.g. `finance()`).

As the library grows, adding many more methods directly to `JRandomly` risks turning it into a "God class". We want a scalable structure that keeps the API discoverable and consistent.

## Decision

### A) Core modules (primary building blocks)

We introduce primary modules for core, type-oriented functionality:

- `r.text()` for string generation
- `r.num()` for numeric generation
- `r.coll()` for collection helpers (List/Set/Map sampling and builders)
- `r.date()` for date-oriented types (e.g., `LocalDate`, `YearMonth`, `Period`)
- `r.time()` for time-oriented types (e.g., `LocalTime`, `Duration`)
- `r.dateTime()` for combined types (e.g., `Instant`, `LocalDateTime`, `ZonedDateTime`)

Notes:

- We intentionally start with `dateTime()` in `de.jinteg.randomly.core`. If splitting becomes necessary later, we can still introduce `date()` and `time()` as separate modules (without breaking `dateTime()` immediately).
- The public name is `dateTime()` (user-friendly). "chrono" may be used internally if helpful.

### B) Cross-cutting utilities

- `r.maybe()` remains a probability-based context (`MaybeContext`) (good fit as a cross-cutting concept).
- `r.bool()` stays as a direct convenience method on `JRandomly` (small and frequently used).

### C) Domains remain domains

Domains stay as domain modules, same fluent style:

- `r.finance()`
- later: `r.person()`, etc.

We do **not** introduce a separate domain prefix (e.g. no `r.domain().finance()`), to keep the fluent API uniform.

### D) What is NOT a core module (for now)

The following are planned to live as domains (or later extensions) rather than primary core modules:

- `id()` / UUID-oriented generators (UUIDs are planned, but the grouping will likely be domain-like)
- `net()` (emails/urls/etc.)
- `pattern()` (templating / formatted string expressions)

## Consequences

### Pros

- Keeps `JRandomly` small and discoverable.
- Clear separation between generic building blocks and semantically rich domains.
- Allows growth without sacrificing determinism or API clarity.

### Cons / risks

- More public types (module classes).
- Migration considerations if many direct `JRandomly.*` helpers already exist.

### Compatibility / migration plan

- Existing direct methods on `JRandomly` may remain and delegate internally.
- New features should primarily be added to the appropriate module.
- Deprecation should be considered only after usage feedback.

## Open questions

- Exact scope of `coll()` (sampling only vs also builders).
- Whether UUID belongs to a small core convenience (`r.uuid()`) or a domain module (`r.id().uuid()`).