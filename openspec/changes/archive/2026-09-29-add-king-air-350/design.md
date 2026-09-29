## Context

The aircraft catalog is a single static JSON file (`shared/src/commonMain/composeResources/files/aircraft.json`) deserialized at runtime by `AircraftBaseImpl` into `List<Aircraft>`. Adding an aircraft is a pure data operation: the models (`Aircraft`, `Performance`, `Checklist`, `Item`), the loader, the aircraft list UI, and the fuel calculator are all schema-driven and require no code changes.

The source material is `openspec/specs/king-air-checklist/checklists.pdf` (Beech King Air 350i beginner guide by JayDee v0.3). Its text was extracted for authoring. The photo asset `king_air_350.jpg` already exists in `composeResources/drawable`.

## Goals / Non-Goals

**Goals:**
- Append one King Air 350 `Aircraft` record (id `3`) that parses cleanly against the existing schema.
- Include exactly the ten requested checklists, in order, transcribed faithfully from the PDF.
- Preserve PDF reference figures (power settings, speeds, limitations) as `details` comments so no information is lost.
- Reuse the existing `king_air_350` drawable and the existing `LINE` separator convention.

**Non-Goals:**
- No Kotlin/model/UI/loader changes.
- No new image or resource files.
- No changes to the three existing aircraft records.
- Not modeling the excluded PDF sections (Takeoff, Go Around, One Engine Out) as checklists.

## Decisions

**Decision: id and placement.** Use `id: 3` and append as the last array element. Rationale: existing ids are `0`–`2`; the loader and any id-keyed navigation stay stable. Alternative (reordering/inserting) rejected — risks shifting existing ids.

**Decision: Ten checklists, ids 0–9, in the user-requested order.** Map PDF sections to captions:
`Preflight` ← PREFLIGHT · `Engine start (cold)` ← ENGINE START (COLD) · `Before taxi` ← BEFORE TAXI · `Before takeoff` ← BEFORE TAKEOFF · `Climb` ← CLIMB · `Cruise` ← CRUISE · `Descent` ← DESCENT · `Approach` ← APPROACH · `After landing` ← AFTER LANDING · `Parking` ← PARKING / SHUT DOWN. Rationale: matches the requested list exactly; the PDF's separate TAKEOFF, GO AROUND, and ONE ENGINE OUT sections are intentionally omitted. Takeoff/V-speed references that remain useful are folded into `details` on neighboring items rather than dropped.

**Decision: Reference figures become `details` comments.** The PDF interleaves narrative/reference blocks (cruise-climb power & speed schedules, max/normal cruise power tables, takeoff power limits, V-speeds) with actionable items. Actionable lines map to `Item.caption` + `Item.action`; reference blocks attach to the most relevant item's `details` (multi-line, `\n`-separated), mirroring the Citation X record's use of `details` for hold/limit notes. Rationale: the user explicitly asked to "apply reference numbers as comments inside our checklist structure"; this keeps the checklist scannable while retaining the data. Alternative (promoting every reference to its own item) rejected — clutters the checklist and departs from existing style.

**Decision: Reuse the `LINE` separator convention.** Where the PDF uses a visual rule or a sub-heading break within a checklist, emit a caption-only item `{ "caption": "LINE" }` (matching `CHECKLIST_LINE` and the Cirrus SR22 record). Use sparingly and only where it improves readability.

**Decision: `action` casing.** Follow existing records: short imperative actions in upper case (`SET`, `ON`, `CHECK`, `OFF`). Keep the PDF's parenthetical qualifiers (e.g. `(MP)`, `(OH)`, `(CP)`, `(FSB)`) inside `caption` so panel locations are preserved.

## Risks / Trade-offs

- [Transcription drift from the PDF] → Cross-check each checklist against the extracted PDF text; keep captions/actions concise but faithful, and retain numeric references verbatim in `details`.
- [Malformed JSON breaks the whole catalog at parse time] → Validate the file parses (JSON lint / a `commonTest` deserialize) before finishing; the new object is appended so a syntax error is easy to isolate.
- [id collision] → Spec mandates `id: 3`; verify no duplicate ids across the array.
- [Over- or under-including reference data] → Err toward inclusion in `details`; excluded whole sections are explicitly enumerated in the spec so the boundary is clear.
