# Proposal

## Why

The released Citation X checklists (`aircraft.json`, id `2`) were authored from an older FlightFX/JayDee revision and now diverge from the current source material in `openspec/specs/citation-x/` (JayDee "CESSNA CITATION X – Flight FX" v2.1 for MSFS 2024, 7 scanned pages): key switch names, the APU/engine-start flow and V-speeds are outdated, and whole flight phases (preflight, cockpit prep, before-takeoff, descent, parking) are missing or trivial (Cruise/Approach have one item each).

## What Changes

- Replace the checklist set of the existing `id: 2` "Cessna Citation X" record with a 13-checklist set transcribed from the v2.1 FlightFX pages, keeping the record's `name`, `photo` and `performance` untouched.
- Update terminology to the v2.1 systems model: thrust levers (not "throttles"), FGCP (not "Autopilot"), Fuel Boost LH+RH, PAC isol valve, STBY power, IRS/ADIRS, EICAS pages; drop obsolete items ("PACS", "hold 5 sec" APU start, "Strobe at V rotate − 145 kt", fixed "Vref 132 kt").
- Change the APU flow to match v2.1: APU started at pre start, bleed OFF at engine start, APU shut down in the climb — no APU restart after landing.
- Apply the sim-optimization style established for the King Air 350 (commits `179ddc0`/`3c93232`): short caption + terse uppercase action per item; `details` reserved for compact numeric references (V-speed tables, stab-trim table, TOD rule, start-sequence limits); narrative sub-steps, clerical and sim-tool lines (Replay Tool, documents) dropped; procedure variants collapsed into single items; no `LINE` separators; no parenthetical qualifiers in captions.
- Excluded from the checklist set (app-scope trims per the King Air precedent, extended by user amendments): the "Quick Cold and Dark" shortcut, cross-bleed start (abnormal), the FMS/EFB setup and performance pages (reduced to the `FMS` and `PFD V-Speeds` items of `Before engine start`), the approach configuration schedule, landing check, landing roll and go-around procedures, taxi-speed guidance, system remarks, specifications/limitations prose and the abbreviations/changelog pages.

## Capabilities

### New Capabilities
- `citation-x-data`: The Citation X aircraft record in the bundled catalog — its checklist set derived from the FlightFX v2.1 pages, transcription/condensing rules, and the retained `name`/`photo`/`performance` fields.

### Modified Capabilities
<!-- None. `openspec/specs/citation-x/` holds only the source scans (no spec.md, so it is not a registered capability). No other capability's behavior changes: the catalog schema, loader, and UI are untouched. -->

## Impact

- Data: `shared/src/commonMain/composeResources/files/aircraft.json` — only the `checklists` array of the `id: 2` object is rewritten; ids of other aircraft/checklists unchanged.
- Code: none — `Aircraft`/`Checklist`/`Item` models, `AircraftBaseImpl` and UI are schema-driven; checklist state is not persisted, so renumbering checklist ids is safe.
- Assets: reuses the existing `citationx.jpg` drawable; the reference scans in `openspec/specs/citation-x/` remain the source of truth.
- Specs: archiving this change backfills a `citation-x-data` capability spec (the old data had no spec at all).
