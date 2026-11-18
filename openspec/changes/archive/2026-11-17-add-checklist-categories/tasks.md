# Tasks

## 1. Data model and color schema

- [x] 1.1 Add `ChecklistCategory` enum (`NORMAL`, `NON_NORMAL`, `PROCEDURE`) and `category: ChecklistCategory = ChecklistCategory.NORMAL` field to `shared/src/commonMain/kotlin/feature/checklistDetails/model/Checklist.kt`; verify `.\gradlew :shared:desktopTest` passes with the unchanged catalog (default path exercised by all 58 records)
- [x] 1.2 Add constants to `SimColors`: `procedureBlue = 0xff81d4fa`, `nonNormalRed = 0xffe57373` (active), `nonNormalRedSoft = 0xffef9a9a`, `nonNormalRedVivid = 0xff8a80`, plus a `@Composable` card-color mapping helper (category + isCompleted → container/content) per design D2–D3; verify shared module compiles
- [x] 1.3 Add unit tests in `shared/src/commonTest` covering: JSON without `category` deserializes to `NORMAL`, explicit `NON_NORMAL`/`PROCEDURE` values parse correctly; verify they pass via `.\gradlew :shared:desktopTest`

## 2. Catalog data: categories and text normalization

- [x] 2.1 Add `"category": "PROCEDURE"` to Citation X `Engine start`, King Air 350 `Engine start`, Piper `Run up`, and `"category": "NON_NORMAL"` to Piper's four emergency checklists in `aircraft.json`; add a catalog test asserting exactly these seven assignments and NORMAL for all others; verify via `.\gradlew :shared:desktopTest`
- [x] 2.2 Uppercase all 58 checklist captions in `aircraft.json` and reduce the two `Engine start (normal)` captions to `Engine start` before uppercasing; update `CitationXChecklistsTest` expected captions and caption-based lookups (`Before engine start`, `Before takeoff`, `Approach`) to uppercase; add a catalog test asserting no lowercase letters in any checklist caption and no `(normal)` qualifier; verify via `.\gradlew :shared:desktopTest`
- [x] 2.3 Uppercase every item `action` value in `aircraft.json` (≈112 values, mostly Piper PA-24 plus four SR22), leaving item `caption` and `details` byte-identical; add a catalog test asserting no lowercase letters in any `action`; verify via `.\gradlew :shared:desktopTest` and a diff review confirming only `action`/`caption`/`category` lines changed

## 3. Checklists list UI

- [x] 3.1 Replace the `isCompleted`-only color conditional in `ChecklistsScreen.kt` with the `SimColors` mapping helper: NORMAL pending/completed unchanged, PROCEDURE light-blue, NON-NORMAL red, dark text on category cards; verify by running `.\gradlew :desktop:run` and observing a NORMAL, a PROCEDURE (e.g. Citation X ENGINE START) and a NON-NORMAL (Piper emergency) card
- [x] 3.2 Show a check icon (`Icons.Default.Check`, `textBlack` tint) on completed NON-NORMAL/PROCEDURE cards without changing their container color; verify in the running app by checking all items of a Piper emergency checklist and seeing the marker appear while the card stays red, and that NORMAL completed cards stay green without icon

## 4. Integration verification

- [x] 4.1 Run the full shared test suite (`.\gradlew :shared:desktopTest`) and confirm all existing tests (including `AircraftBaseTestImplTest` and updated `CitationXChecklistsTest`) plus new category/normalization tests pass
- [x] 4.2 Visual pass in light and dark themes on the running desktop app: category colors readable on both backgrounds, check marker visible; hand over to the user for red-candidate testing (swap the active constant in the mapping helper between `nonNormalRed`, `nonNormalRedSoft`, `nonNormalRedVivid` and rebuild)

## Workflow follow-up

- After the user picks the final red candidate, set it as the active constant (one-line change) and re-verify visually.
- Archive the change once implementation and visual verification are complete (`/opsx-archive`).
- Optional separate change: reconcile the pre-existing `piper-pa24-data` spec drift (18 specified vs 16 actual checklists).
