## Why

The app ships with three aircraft (Cessna 172, Cirrus SR22, Citation X) but no turboprop. Users have requested the Beech King Air 350, a popular MS Flight Simulator aircraft, and the reference material (`openspec/specs/king-air-checklist/checklists.pdf`) and photo asset (`king_air_350.jpg`) are already available in the repo.

## What Changes

- Add a fourth aircraft entry (`id: 3`, "Beech King Air 350") to `shared/src/commonMain/composeResources/files/aircraft.json`.
- Populate its `performance` block: fuel capacity 539 gal, average fuel flow 125 gph, average cruise speed 310 kt.
- Add ten checklists derived from the King Air PDF: Preflight, Engine start (cold), Before taxi, Before takeoff, Climb, Cruise, Descent, Approach, After landing, Parking/Shutdown.
- Set `photo` to the existing drawable resource name `king_air_350`.
- Reference figures that are not checklist items (cruise climb speeds, power settings, V-speeds, limitations) SHALL be embedded as `details` comments on the relevant items, mirroring the existing Citation X style.

## Capabilities

### New Capabilities
- `king-air-350-data`: The King Air 350 aircraft record — its performance numbers, checklist set, and photo binding — as loaded from the bundled `aircraft.json` catalog.

### Modified Capabilities
<!-- None: this adds a new data record to an existing, unchanged schema. No spec-level behavior of the aircraft catalog, checklist rendering, or fuel calculator changes. -->

## Impact

- Data: `shared/src/commonMain/composeResources/files/aircraft.json` (append one aircraft object; existing entries untouched).
- Assets: reuses the already-present `king_air_350.jpg` drawable; no new resources.
- Code: none — the `Aircraft`/`Checklist`/`Item` models, `AircraftBaseImpl` loader, and UI already support the schema. New `id: 3` must not collide with existing ids (0–2).
- Downstream: the fuel calculator and aircraft list will automatically surface the new aircraft once the JSON parses.
