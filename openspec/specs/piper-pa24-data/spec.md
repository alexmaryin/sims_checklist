# piper-pa24-data

## Purpose

Add the Piper PA-24-250 Comanche as the fifth aircraft in the bundled catalog (`aircraft.json`), including its performance numbers, photo binding, and an eighteen-checklist set derived from `openspec/specs/piper-pa24/PA-24-250 Comanche Checklist 58-60v1.1.pdf`, with cautions and notes retained as `details` on adjacent items.

## Requirements

### Requirement: PA-24-250 aircraft record in the catalog

The system SHALL include a fifth aircraft entry in `shared/src/commonMain/composeResources/files/aircraft.json` for the Piper PA-24-250 Comanche. The entry SHALL use the existing `Aircraft` schema (`id`, `name`, `performance`, `checklists`, `photo`) and SHALL be appended without modifying the four existing aircraft. The `id` SHALL be `4` so it does not collide with existing ids `0`–`3`, and the `name` SHALL be `"Piper PA-24-250 Comanche"`.

#### Scenario: Catalog parses with five aircraft

- **WHEN** the bundled `aircraft.json` is loaded and deserialized via `AircraftBaseImpl`
- **THEN** parsing SHALL succeed and produce a list of five `Aircraft` objects

#### Scenario: New record identity

- **WHEN** the loaded list is inspected
- **THEN** it SHALL contain an aircraft with `id == 4` and `name == "Piper PA-24-250 Comanche"`
- **AND** the aircraft with ids `0`, `1`, `2`, and `3` SHALL be unchanged

### Requirement: PA-24-250 performance numbers

The PA-24-250 `performance` block SHALL carry `fuelCapacity` of `94` (gallons, mains plus tip tanks), `averageFuelFlow` of `10.5` (gallons per hour), and `averageCruiseSpeed` of `140` (knots), consumed by the fuel calculator.

#### Scenario: Performance values exposed to fuel calculator

- **WHEN** the PA-24-250 is selected in the fuel calculator
- **THEN** `performance.fuelCapacity` SHALL be `94`
- **AND** `performance.averageFuelFlow` SHALL be `10.5`
- **AND** `performance.averageCruiseSpeed` SHALL be `140`

### Requirement: PA-24-250 checklist set

The PA-24-250 record SHALL include exactly eighteen checklists, transcribed from `openspec/specs/piper-pa24/PA-24-250 Comanche Checklist 58-60v1.1.pdf` (page images used as ground truth where the PDF text layer is corrupted), with sequential ids `0`–`17` and captions in this order:

| id | caption |
|----|---------|
| 0 | Before starting |
| 1 | Starting the engine |
| 2 | Before taxi |
| 3 | Taxi |
| 4 | Run up |
| 5 | Before take off |
| 6 | Take off |
| 7 | After take off |
| 8 | Enroute |
| 9 | Descent |
| 10 | Landing |
| 11 | After landing |
| 12 | Engine shut down |
| 13 | Before leaving aircraft |
| 14 | Engine fire when starting |
| 15 | Engine failure in flight |
| 16 | Engine fire in flight |
| 17 | Landing without motor |

#### Scenario: Eighteen checklists in order

- **WHEN** the PA-24-250 record is loaded
- **THEN** `checklists` SHALL contain eighteen entries with ids `0` through `17`
- **AND** their captions SHALL match the table above, in order

#### Scenario: Emergency checklists present as distinct checklists

- **WHEN** the checklist set is inspected
- **THEN** `Engine fire when starting`, `Engine failure in flight`, `Engine fire in flight`, and `Landing without motor` SHALL each be a standalone checklist

#### Scenario: Excluded PDF sections

- **WHEN** the checklist set is compared against the source PDF
- **THEN** the `Go around` block and the `Speeds (IAS)`/weights table SHALL NOT be included, as checklists or as `details` embeds

#### Scenario: Landing pattern legs preserved

- **WHEN** the `Landing` checklist is inspected
- **THEN** the four PDF legs `Pattern Entry`, `Downwind`, `Base`, and `Final Approach` SHALL be represented with their sub-steps intact (via `details` groupings)

### Requirement: Cautions and notes retained as details

Red-text cautions and italic notes in the PDF (for example "Do not exceed 2200 rpm in routine static test", "Avoid prolonged ground ops with Carb Heat on", "Allow the engine to warm between 800-1200 RPM for 2–4 minutes before runup", "Approach with engine power; Avoid 'floating in' at idle", "Extend Landing Gear only when landing on Firm ground is ENSURED") SHALL be attached as `details` on the adjacent checklist item rather than dropped or promoted to standalone items.

#### Scenario: Run-up caution retained

- **WHEN** the `Run up` checklist is inspected
- **THEN** the item preceding or following the 2000 RPM throttle setting SHALL carry a `details` string containing the "do not exceed 2200 rpm" caution

### Requirement: PA-24-250 photo binding

The PA-24-250 record SHALL set `photo` to `"piper_pa24"` and a `piper_pa24.jpg` drawable SHALL be added to `shared/src/commonMain/composeResources/drawable/` so `loadAircraftJpgPhoto` resolves it. The image SHALL be the N5448P side-view extracted from the PDF page header (or a PNG-crop fallback of equal content).

#### Scenario: Aircraft list shows the Comanche photo

- **WHEN** the aircraft list renders the PA-24-250 card
- **THEN** the image painter SHALL resolve from the `piper_pa24` drawable resource
