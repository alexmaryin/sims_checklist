# Design

## Context

The aircraft catalog is a single static JSON file (`shared/src/commonMain/composeResources/files/aircraft.json`) deserialized at runtime into `List<Aircraft>`; models, loader, list UI and fuel calculator are schema-driven, so this is a pure data change. The current Citation X record (`id: 2`, 11 checklists, 68 items) predates the OpenSpec workflow and is based on an old FFX revision ("PACS", APU "hold 5 sec", fixed Vref 132).

The source material is the seven page scans in `openspec/specs/citation-x/` (JayDee FlightFX Citation X checklist v2.1 for MSFS 2024). Unlike the King Air change, the source is images, not extractable text, so authoring reads the JPGs directly.

The user's post-release amendments to the King Air and Piper records (commits `179ddc0`, `3c93232`) define the target style and are the binding precedent for this transcription.

## Goals / Non-Goals

**Goals:**
- A 13-checklist Citation X set that covers the switch-level flow of the full v2.1 document while staying scannable: items roughly half the size of the source's line count (~145 items vs ~250 source lines).
- The wording rules from the King Air amendments applied preemptively during transcription, not as a follow-up trim.
- No changes outside the `checklists` array of `id: 2`.

**Non-Goals:**
- No Kotlin/model/UI changes, no new drawables, no changes to the other aircraft.
- No emergency/abnormal checklist program: the v2.1 doc has none beyond cross-bleed start, which is excluded.
- No attempt to reproduce the scan's tables verbatim; numbers are kept only where a checklist item needs them.

## Decisions

**Decision: amend in place, not a second record.** The record keeps `id: 2`, `name`, `photo`, `performance` and only `checklists` is replaced. Alternative (adding "Citation X (FlightFX v2.1)" as a new aircraft) rejected — duplicates the catalog entry, and nothing persists checklist state (`Item.checked` is session-only), so renumbering checklist ids `0..12` is safe.

**Decision: phase set and naming.** 13 checklists following the King Air phase-name convention (`Preflight`, `Before takeoff`, `Climb`, `Cruise`, `Descent`, `Approach`, `After landing`, `Parking`), plus Citation-specific `Pre start`, `Before engine start`, `Engine start`, `Shutdown`; `After start / Taxi` merges two small source sections. The scans' FMS-setup pages are not a checklist (user amendment): the FMS burden is reduced to the `FMS` (`SETUP COMPLETED`) and `PFD V-Speeds` items of `Before engine start`. Mapping from the old release: `Pre start`→`Pre start`(+cockpit prep), `Start up`→`Before engine start`+`Engine start`, `After start`+`Taxi`→`After start / Taxi`, `Line up`→absorbed into `Before takeoff`, `After take off`→`Climb`, `Approach`+`Landing`→`Approach`, `After landing`→`After landing`+`Parking`, `Shutdown`→`Shutdown`. New: `Preflight`, `Descent`.

**Decision: condense while transcribing (King Air amendment rules).**
- Main dotted lines → `Item{caption, action}` with terse uppercase actions (`SET`, `ON`, `CHECK`, `AS REQ`, `AS DES`).
- `→` sub-step bullets → dropped when procedural flavor ("line up and brake, release brakes"), or kept as compact multi-line `details` only when they carry numbers (start sequence: residual ITT ≤200°C, IDLE at 10% N2, ignition ~33%, starter disengage ~57%; APU: 101% RPM, DC ≥22V, Amps <300).
- Reference tables → `details` on the nearest item, values only, no column headers: V1/VR/V2 (4 weight columns, 1 row each) under `PFD V-Speeds`; VREF/VAPP under the approach `VREF/VAPP` item; stab trim (2 rows) under `Stab Trim`; TOD rule formula under `TOD X-CHECK`.
- Sim-specific and clerical content dropped per precedent: `Replay Tool`, EFB configuration essays, `FFX EFB - Covers, Chocks` (kept once as `Chocks & covers REMOVED` in Preflight only), documents/belt items, repeated light checks.
- No `LINE` separators (neither King Air nor Piper uses them after the amendments).
- Approach procedure blocks are out of app scope (user amendment): the scan's config schedule, `Landing Check`, landing roll and `Go-Around` sub-blocks, and the taxi-speed line, are dropped rather than collapsed — `Approach` keeps switch/config items and the VREF/VAPP table only.

**Decision: terminology and flows follow v2.1 where they contradict the old record.** Thrust levers, FGCP yaw damper/Mach trim, Fuel Boost LH+RH, PAC isol valve, STBY power, IRS align; APU runs from `Pre start` to `Climb` (shutdown before FL310) — the old post-landing APU restart is dropped because v2.1 never does it; `PACS` items are replaced by `Passenger Signs`. Rationale: the record must match the add-on users currently fly.

**Decision: style details.** 2-space JSON indentation matching the existing file; checklist object shape identical to other records; actions single-word where possible; captions sentence case like the existing records ("Engine start", not "ENGINE START").

## Risks / Trade-offs

- [Transcription drift from JPG scans] → Re-read each target page while writing its checklist; cross-check every number table against the scan after the edit; the seven source images stay in the repo as the review baseline.
- [Over-trimming loses v2.1 value] → Err toward one-line items over prose: keep the item, drop the explanation. Exclusions are enumerated in the spec so the boundary is testable.
- [Malformed JSON breaks the whole catalog] → Validate parse + deserialize (existing `commonTest`) before finishing; only one record's array changes, so a syntax error is easy to isolate.
- [Old habits resurface from the previous record] → Copy items from the scans, not from the old JSON; obsolete terms are explicitly banned in the spec scenarios.

## Migration Plan

Single data commit rewriting `checklists` of `id: 2`; rollback is reverting the commit. No migration of user state is needed (checklist progress is not persisted).
