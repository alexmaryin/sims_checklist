# Design: Add Piper PA-24-250 Comanche

## Context

The catalog (`shared/src/commonMain/composeResources/files/aircraft.json`) holds five-plus-records worth of `Aircraft` objects (`id`, `name`, `performance`, `checklists`, `photo`), consumed by `AircraftBaseImpl` and rendered through `loadAircraftJpgPhoto` (JPG drawables only). The King Air 350 change (`2026-09-29-add-king-air-350`) established the pattern for adding a type purely as data: append the JSON record, bind an existing-or-new photo, and embed reference figures as `details` comments. Source material for the Comanche is the two-page PDF `PA-24-250 Comanche Checklist 58-60v1.1.pdf` in `openspec/specs/piper-pa24/`.

## Goals / Non-Goals

**Goals:**

- Fifth catalog record `id == 4`, name `"Piper PA-24-250 Comanche"`, no changes to existing records.
- Complete faithful transcription of both PDF pages into 18 checklists (14 normal + 4 emergency) using the existing `Checklist`/`Item` schema.
- Photo bound to a new `piper_pa24.jpg` drawable.

**Non-Goals:**

- No Kotlin model, screen, or resource-loading changes.
- No reformatting of the Cirrus `LINE` separator convention beyond what the schema already supports (`details` sub-steps suffice for grouped blocks like Landing pattern legs).
- No simulator-side data (no G1000/steam-gauge wiring); this is bundled JSON only.
- The PDF's `Go around` block and `Speeds (IAS)`/weights table are NOT transcribed (see decision 4).

## Decisions

1. **Checklist inventory follows the PDF sections, minus the excluded blocks.** All 18 remaining sections become checklists with sequential ids `0`–`17`: Before starting, Starting the engine, Before taxi, Taxi, Run up, Before take off, Take off, After take off, Enroute, Descent, Landing, After landing, Engine shut down, Before leaving aircraft, then the four emergency sections (Engine fire when starting, Engine failure in flight, Engine fire in flight, Landing without motor). Alternative (including Go Around as a normal-flow checklist) rejected: the user excluded it, consistent with the King Air 350 precedent of dropping standalone Go-Around/One-Engine-Out style sections.
2. **Sub-structure uses `details`, not separator items.** The PDF groups content under inline headings (`Climb` in After take off, `Pattern Entry`/`Downwind`/`Base`/`Final Approach` in Landing, `Restart`/`If Prop is not turning` in Engine failure in flight). These become caption-only parent items whose `details` hold the grouped sub-steps, or per-item `details` where a caution/note attaches to one line. Avoids inventing `LINE` separators where the Cirrus convention adds nothing.
3. **Red cautions/notes are `details` text on the item they follow** (e.g. "Do not exceed 2200 rpm in routine static test", "Approach with engine power"). Mirrors the King Air reference-figure rule.
4. **Excluded PDF blocks: `Go around` and the `Speeds (IAS)`/weights table.** Neither becomes a checklist nor a `details` embed. Speeds that appear inline in the PDF's own checklist items (e.g. "Accelerate to VR 85 MPH (74 kt)", approach 90 MPH) stay as part of those transcribed items; the page-2 reference table itself is not reproduced.
5. **Performance numbers:** `fuelCapacity` 94 (50 gal mains + 2×22 gal tip tanks, per the PDF's tip-tank MGW note), `averageFuelFlow` 10.5 GPH, `averageCruiseSpeed` 140 kt (≈75 % power cruise, O-540). The PDF's 120 MPH en-route-climb figure is a climb speed, not cruise, so it is not used. Chosen over the 50-gal no-tip-tank variant because the checklist explicitly assumes tip tanks for MGW.
6. **Photo:** extract the embedded header JPEG (the N5448P side view) from page 1 of the PDF via `pypdf` image extraction and save as `piper_pa24.jpg`; if extraction quality is unusable, fall back to the equivalent crop of `PA-24-250 Chklst_1.png`. Name follows existing drawable convention (`citationx.jpg`, `king_air_350.jpg` — lowercase, no underscores needed but `piper_pa24` keeps it readable).

## Risks / Trade-offs

- [PDF ligature corruption in text layer (`Ɵ` for "ti", odd spacing)] → transcribe from the two page PNGs as ground truth; use extracted text only as a starting draft.
- [Header image extracted from PDF may be low-res or CMYK] → validate the saved JPEG renders in the aircraft list; fall back to the PNG crop (decision 6).
- [Performance values (94 gal / 10.5 GPH / 140 kt) are not printed in the source PDF] → they are club-pilot-standard POH figures for the tip-tank 250; acceptable for the fuel calculator, easy to adjust later in JSON-only.
- [18 checklists is more than any existing record] → schema has no limit; UI already renders Citation X's 11.

## Migration Plan

Data-only change; ship by rebuilding the app. Rollback = revert the JSON entry and drawable.
