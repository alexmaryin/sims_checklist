# Proposal

## Why

All checklists currently look identical in the list regardless of their nature: a routine "Before takeoff" flow, an emergency "Engine failure in flight" drill, and a reference procedure are visually indistinguishable. Pilots scanning the list under time pressure need the checklist's group (NORMAL / NON-NORMAL / PROCEDURE) to be recognizable at a glance, following the app's existing color conventions.

## What Changes

- Add an optional `category` field to the checklist JSON schema with values `NORMAL`, `NON_NORMAL`, `PROCEDURE`; when the field is missing, the checklist defaults to `NORMAL` (fully backward-compatible with existing data).
- Extend the app color schema (`SimColors`) with a light-blue PROCEDURE color and red NON-NORMAL color variants (all candidate reds defined as constants so they can be swapped and tested after implementation).
- Color-code checklist cards in the checklists list by category:
  - NORMAL: unchanged — default surface, green (`inversePrimary`) when completed.
  - PROCEDURE: light-blue card, no completion color change.
  - NON-NORMAL: red card, no completion color change.
- NON-NORMAL and PROCEDURE checklists keep tracking checkmarks/completion internally; completed cards of these categories show a check marker instead of changing color.
- Annotate the bundled catalog: Piper PA-24 `RUN UP` → PROCEDURE; King Air 350 `ENGINE START` → PROCEDURE; Citation X `ENGINE START` → PROCEDURE; Piper PA-24's four emergency checklists (`ENGINE FIRE WHEN STARTING`, `ENGINE FAILURE IN FLIGHT`, `ENGINE FIRE IN FLIGHT`, `LANDING WITHOUT MOTOR`) → NON_NORMAL. All other checklists remain unmarked (default NORMAL).
- Normalize catalog text: all checklist captions stored in uppercase in `aircraft.json`, redundant `(normal)` caption qualifiers removed (Cessna 172, Cirrus SR22), and all item `action` values uppercased. Item captions and `details` text are not touched.
- No changes to the checklist details screen behavior or to completion/clear logic.

## Capabilities

### New Capabilities

- `checklist-categories`: checklist group taxonomy (NORMAL / NON-NORMAL / PROCEDURE), the optional-with-default JSON field, category color encoding in the checklists list, completion indication for non-NORMAL categories, category assignments in the bundled catalog, and catalog caption/action text normalization.

### Modified Capabilities

- `citation-x-data`: the exact-caption table for the thirteen checklists changes to uppercase captions.
- `king-air-350-data`: the exact-caption table changes to uppercase captions; the `Engine start (cold)` caption is reconciled to the actual catalog caption `ENGINE START` (the checklist being categorized as PROCEDURE).
- `piper-pa24-data`: the exact-caption table and caption references change to uppercase.

## Impact

- **Model**: `shared/src/commonMain/kotlin/feature/checklistDetails/model/Checklist.kt` — new `ChecklistCategory` enum and `category` field with default.
- **Colors**: `shared/src/commonMain/kotlin/commonUi/utils/SimColors.kt` — new constants (one blue, three red candidates). No changes to `Themes.kt` / `Theme.kt` color schemes.
- **UI**: `shared/src/commonMain/kotlin/feature/checklists/ui/ChecklistsScreen.kt` — category-based card colors and completion check marker.
- **Data**: `shared/src/commonMain/composeResources/files/aircraft.json` — 7 category annotations, 58 captions uppercased, 2 `(normal)` qualifiers removed, ~112 item actions uppercased (mostly Piper PA-24, four in Cirrus SR22).
- **Tests**: existing catalog deserialization tests (`CitationXChecklistsTest`, `AircraftBaseTestImplTest`) must stay green; new coverage for the default category and category parsing.
- **Known pre-existing drift (not fixed by this change)**: `piper-pa24-data` spec claims 18 checklists including `Landing` and `Before leaving aircraft`, but the catalog contains 16. The delta keeps the existing table content (case-normalized only); reconciling the missing checklists is a separate concern.
