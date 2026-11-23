# Design

## Context

See `proposal.md` for motivation. Current mechanics that shape this design:

- Color pipeline: raw values in `commonUi/utils/SimColors.kt` (shared) → wired into Material `colorScheme` slots by `android/.../Themes.kt` and `desktop/.../Theme.kt` → UI consumes slots. The completed-green (`accentGreen = 0xff00c853` via `inversePrimary`) is **identical in light and dark themes** with black text (`inverseOnSurface = textBlack` in both) — the precedent for category colors.
- `Checklist` (`feature/checklistDetails/model/Checklist.kt`) is `@Serializable` with defaults already in use (`isCompleted = false`). `AircraftBaseImpl` parses with strict `Json` (no `ignoreUnknownKeys`), so model and bundled file must ship together — adding a field with a default is safe in that direction.
- `ChecklistsScreen.kt` picks card colors with a single `if (checklist.isCompleted)` conditional; caption is already rendered via `.uppercase()`.
- `errorContainer`/`onErrorContainer` slots are unused; `tertiaryContainer` and `secondaryContainer` are taken (InfoTabs, WaypointCard).
- Catalog: 5 aircraft, 58 checklists. ~112 item actions contain lowercase (≈108 in Piper PA-24, 4 in Cirrus SR22). Two captions carry `(normal)` qualifiers.

## Goals / Non-Goals

**Goals:**

- Category-aware card coloring in the checklists list only, with zero behavior change elsewhere.
- Backward-compatible data contract: absent `category` ⇒ NORMAL.
- All red candidates available as named constants so the user can swap and visually test after implementation.
- One consistent text convention across the catalog: uppercase checklist captions, uppercase item actions.

**Non-Goals:**

- No changes to the checklist details screen, completion/clear logic, or repository.
- No changes to `Themes.kt` / `Theme.kt` color schemes or Material slot wiring.
- No new checklists or aircraft; no reconciliation of the pre-existing Piper spec/JSON drift (flagged in proposal).
- No theme-specific (light/dark) category color variants.

## Decisions

### D1: Enum field with serialization default

```kotlin
@Serializable
enum class ChecklistCategory { NORMAL, NON_NORMAL, PROCEDURE }

// in Checklist:
val category: ChecklistCategory = ChecklistCategory.NORMAL
```

kotlinx.serialization applies the default when the key is absent — no custom decoder needed. JSON uses `NON_NORMAL` (underscore): Kotlin enum identifiers cannot contain hyphens, and `@SerialName("NON-NORMAL")` adds indirection for no functional gain. Alternative considered: plain `String` field with runtime validation — rejected, enum is type-safe and self-documenting.

### D2: Colors live in SimColors, consumed via a small composable mapping

Add constants to `SimColors` and a `@Composable` helper (e.g. `checklistCardColors(category, isCompleted)` returning container/content pair) used by `ChecklistsScreen`. No Material slot wiring: red could fit the free `errorContainer`, but blue has no free semantic slot, and a hybrid (red via slot, blue via constant) would be inconsistent. Direct constants mirror how the green behaves and keep both theme files untouched. Alternative considered: `errorContainer` + new slot abuse — rejected.

### D3: Color values — one blue, three red candidates

| Constant | Value | Source | Role |
|---|---|---|---|
| `procedureBlue` | `0xff81d4fa` | Material Light Blue 200 | PROCEDURE container (active) |
| `nonNormalRed` | `0xffe57373` | Material Red 300 | NON-NORMAL container (**active default**) |
| `nonNormalRedSoft` | `0xffef9a9a` | Material Red 200 | candidate, softer |
| `nonNormalRedVivid` | `0xff8a80` | Material Red A100 | candidate, matches the "A-accent" style of `accentGreen` |

All three reds are defined as constants; the mapping helper references exactly one active constant, so switching candidates is a one-line change followed by a rebuild. Text on category cards: `SimColors.textBlack` (contrast ≈ 13:1 on blue, ≈ 8:1 on Red 300). Same values in both themes, per the green precedent. Final red selection is deferred to the user's visual testing.

### D4: Check marker for completed non-NORMAL cards

Completed NON-NORMAL/PROCEDURE cards keep their category color and gain a check icon (Material `Icons.Default.Check`, tinted `textBlack`) alongside the centered caption — e.g. a `Row` with the caption weighted/centered and the icon at the end, or icon+text centered together. NORMAL cards are untouched (green only, no icon). Completion tracking itself is unchanged — the marker is purely a list-level indicator derived from the existing `isCompleted`.

### D5: Text normalization rules

- Checklist captions: uppercase in the JSON; remove the `(normal)` qualifier from the two `Engine start (normal)` captions → `ENGINE START`. No other wording changes. Display is unaffected (UI already uppercases).
- Item `action`: uppercase the whole string. Units and symbols follow (`in Hg` → `IN HG`, `rpm` → `RPM`, `¼ inch` → `¼ INCH`), consistent with the dominant existing convention (`1000 RPM`, `TEST & SET`). Curly quotes and fractions are preserved.
- Item `caption` and `details`: byte-identical, untouched (narrative sub-steps keep their case).

### D6: Category annotations — explicit only where non-default

Seven records get `"category"`: Citation X `ENGINE START`, King Air 350 `ENGINE START`, Piper `RUN UP` → `PROCEDURE`; Piper's four emergency checklists → `NON_NORMAL`. The other 51 stay unmarked, exercising the default path in production data.

## Risks / Trade-offs

- [Pastel containers may feel bright on the dark-brown theme] → identical treatment to the existing green; user will visually verify both themes, and red candidates allow tuning.
- [Whole-string action uppercasing also uppercases units] → matches the majority convention already in the catalog; `details` narrative is preserved for readability.
- [Strict `Json` rejects unknown keys] → the new field is added to model and file in the same change; existing deserialization tests guard the contract.
- [Compile-time red selection requires rebuild to compare candidates] → acceptable for a one-person visual test loop; constants are named and co-located for fast swapping.
- [Piper spec still claims 18 checklists vs 16 in JSON] → pre-existing drift, explicitly out of scope; delta keeps table content case-normalized only.

## Migration Plan

Bundled read-only resource; no persisted user data references categories. Ship model + JSON + UI together. Rollback = revert the change; old code ignores nothing (field didn't exist), old file parses fine with new model only if the field is absent — hence atomic commit of model and data.

## Open Questions

- Which red candidate becomes permanent (`nonNormalRed` / `nonNormalRedSoft` / `nonNormalRedVivid`) — decided by the user after implementation, one-line swap. Does not affect specs or task breakdown.
