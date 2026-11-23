# citation-x-data

## Purpose

Defines the Citation X aircraft record in the bundled catalog: a 13-checklist set transcribed from the FlightFX v2.1 scans in `openspec/specs/citation-x/`, the condensed sim-optimized wording rules, the excluded source sections, and the record fields that stay unchanged.

## Requirements

### Requirement: Citation X checklist set rebased to FlightFX v2.1

The system SHALL replace only the `checklists` array of the existing `Cessna Citation X` record (`id: 2`), stored in the per-aircraft file `shared/src/commonMain/composeResources/files/aircraft/citationx.json`, with exactly thirteen checklists derived from the FlightFX v2.1 scans (`openspec/specs/citation-x/Citation_X---001.jpg` … `007.jpg`). Checklist ids SHALL be sequential `0`–`12` with the captions enumerated in the scenario below; the record's `name`, `photo` and `performance` fields SHALL not be touched by this replacement. Captions SHALL be stored in uppercase per the catalog caption normalization.

#### Scenario: Thirteen checklists in order

- **WHEN** the Citation X record is loaded
- **THEN** `checklists` SHALL contain thirteen entries whose ids and captions match, in order:

| id | caption |
|----|---------|
| 0 | PREFLIGHT |
| 1 | PRE START |
| 2 | BEFORE ENGINE START |
| 3 | ENGINE START |
| 4 | AFTER START / TAXI |
| 5 | BEFORE TAKEOFF |
| 6 | CLIMB |
| 7 | CRUISE |
| 8 | DESCENT |
| 9 | APPROACH |
| 10 | AFTER LANDING |
| 11 | PARKING |
| 12 | SHUTDOWN |

#### Scenario: Catalog stays valid

- **WHEN** the bundled catalog is loaded and deserialized from the per-aircraft files
- **THEN** parsing SHALL succeed and produce the full aircraft list with all other records unchanged

### Requirement: Source fidelity and v2.1 terminology

Checklist items SHALL be transcribed from the v2.1 scans using the v2.1 systems vocabulary (thrust levers, FGCP, Fuel Boost LH + RH, PAC isol valve, STBY power, IRS/ADIRS, EICAS pages). Item wording that only exists in the superseded release record SHALL NOT be carried over.

#### Scenario: Obsolete wording removed

- **WHEN** the new Citation X checklists are inspected
- **THEN** no item SHALL mention `PACS`, the `hold 5 sec` APU start, a fixed `Vref... 132 Kt`, or strobes timed to `V rotate - 145 Kt`

#### Scenario: APU flow matches v2.1

- **WHEN** the checklists are compared with the v2.1 flow
- **THEN** the APU SHALL be started in `Pre start`, its bleed turned off at engine start, and its shutdown performed in `Climb`
- **AND** the `After landing` checklist SHALL NOT start the APU as a standing flow item

### Requirement: Concise item wording

Every item SHALL use the existing `Item` schema with a short `caption` and a terse uppercase `action`. The `details` field SHALL be reserved for compact numeric references only (V-speed tables, stab trim table, TOD rule, start-sequence limits). Narrative sub-steps, clerical lines (documents, seat belts, cabin lights), sim-tool lines (Replay Tool), `LINE` separator items and parenthetical caption qualifiers SHALL NOT be included.

#### Scenario: V-speed tables kept as compact details

- **WHEN** the `Before engine start` checklist is inspected
- **THEN** the `PFD V-Speeds` item SHALL carry takeoff speeds (V1/VR/V2 per weight, flaps 15°) as a `details` comment
- **AND** the `details` SHALL contain table values only, without procedural prose

#### Scenario: Procedures outside app scope omitted

- **WHEN** the `Approach`, `After start / Taxi` and ground checklists are inspected
- **THEN** `Approach` SHALL contain switch/config items only — no `Approach ... PERFORM` schedule, `Landing check`, `Landing roll` or `Go-around` items
- **AND** `After start / Taxi` SHALL have no taxi-speed item
- **AND** the FMS/performance pages of the scans SHALL be represented only by the `FMS` / `PFD V-Speeds` items of `Before engine start`, not by a dedicated FMS checklist

### Requirement: Excluded source sections

The checklist set SHALL NOT model the following parts of the v2.1 scans: the "Quick Cold and Dark" shortcut, the cross-bleed engine start (abnormal procedure), the FMS/EFB setup and performance pages, the approach configuration schedule, landing check, landing roll, visual pattern approach and go-around procedures, the taxi speed guidance, and the Preface, Specifications & Limitations prose, system remarks (FFX EFB/ATS/FGCP essays), abbreviations legend and changelog.

#### Scenario: Excluded sections absent

- **WHEN** the checklist set is compared against the source scans
- **THEN** no checklist SHALL duplicate the Quick Cold and Dark page, the cross-bleed start or the FMS setup pages
- **AND** no item SHALL model the approach/landing roll/go-around procedure blocks
- **AND** limitation tables not tied to a checklist item SHALL be dropped or attached as `details` on the nearest relevant item

### Requirement: Retained record fields

The Citation X record SHALL keep its existing identity and non-checklist fields: `id: 2`, `name: "Cessna Citation X"`, `photo: "citationx"` (reusing the existing `citationx.jpg` drawable) and the `performance` block `{fuelCapacity: 1930, averageCruiseSpeed: 528, averageFuelFlow: 336}`, since the source scans contain no fuel/performance figures.

#### Scenario: Performance values unchanged

- **WHEN** the Citation X is selected in the fuel calculator
- **THEN** `performance` SHALL be `1930` / `528` / `336` as before

#### Scenario: Photo and name unchanged

- **WHEN** the aircraft list renders the Citation X card
- **THEN** the record SHALL show `Cessna Citation X` with the `citationx` photo
