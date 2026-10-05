# Proposal: Add Piper PA-24-250 Comanche

## Why

The bundled aircraft catalog covers four types but not the classic single-engine piston taildraggers/fixed-gear types our sim users fly in the club fleet. The Piper PA-24-250 Comanche (N5448P, 1958–1960) checklist source set has already been captured under `openspec/specs/piper-pa24/` and now needs to be turned into structured app data.

## What Changes

- Add a fifth aircraft entry (`id == 4`, `"Piper PA-24-250 Comanche"`) to the bundled `aircraft.json`, without modifying the four existing records.
- Add performance numbers for the fuel calculator: `fuelCapacity` 94 US gal (mains + tip tanks), `averageFuelFlow` 10.5 GPH, `averageCruiseSpeed` 140 kt (75 % power cruise).
- Add a full checklist set derived from `PA-24-250 Comanche Checklist 58-60v1.1.pdf`: 14 normal checklists (before starting → before leaving aircraft) plus 4 emergency checklists (engine fire when starting, engine failure in flight, engine fire in flight, landing without motor). The `Go around` block and the `Speeds (IAS)` table from the PDF are excluded.
- Add a `piper_pa24.jpg` aircraft photo drawable (extracted from the PDF header image) and bind it via `photo == "piper_pa24"`.

## Capabilities

### New Capabilities

- `piper-pa24-data`: Piper PA-24-250 Comanche catalog record — identity, performance numbers, checklist set derived from the source PDF, reference-figure handling, and photo binding.

### Modified Capabilities

<!-- None: existing aircraft records and shared schemas are unchanged. -->

## Impact

- `shared/src/commonMain/composeResources/files/aircraft.json` — one appended record.
- `shared/src/commonMain/composeResources/drawable/piper_pa24.jpg` — new asset.
- No Kotlin schema changes required: the existing `Aircraft`/`Checklist`/`Item` model and `loadAircraftJpgPhoto` helper cover the new record (same approach as `king-air-350-data`).
- Source documents live in `openspec/specs/piper-pa24/` (PDF + two page PNGs).
