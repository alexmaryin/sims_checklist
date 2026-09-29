## ADDED Requirements

### Requirement: King Air 350 aircraft record in the catalog

The system SHALL include a fourth aircraft entry in `shared/src/commonMain/composeResources/files/aircraft.json` for the Beech King Air 350. The entry SHALL use the existing `Aircraft` schema (`id`, `name`, `performance`, `checklists`, `photo`) and SHALL be appended without modifying the three existing aircraft. The `id` SHALL be `3` so it does not collide with existing ids `0`, `1`, `2`, and the `name` SHALL be `"Beech King Air 350"`.

#### Scenario: Catalog parses with four aircraft

- **WHEN** the bundled `aircraft.json` is loaded and deserialized via `AircraftBaseImpl`
- **THEN** parsing SHALL succeed and produce a list of four `Aircraft` objects

#### Scenario: New record identity

- **WHEN** the loaded list is inspected
- **THEN** it SHALL contain an aircraft with `id == 3` and `name == "Beech King Air 350"`
- **AND** the aircraft with ids `0`, `1`, and `2` SHALL be unchanged

### Requirement: King Air 350 performance numbers

The King Air 350 `performance` block SHALL carry `fuelCapacity` of `539` (gallons), `averageFuelFlow` of `125` (gallons per hour), and `averageCruiseSpeed` of `310` (knots), matching the values requested and consumed by the fuel calculator.

#### Scenario: Performance values exposed to fuel calculator

- **WHEN** the King Air 350 is selected in the fuel calculator
- **THEN** `performance.fuelCapacity` SHALL be `539`
- **AND** `performance.averageFuelFlow` SHALL be `125`
- **AND** `performance.averageCruiseSpeed` SHALL be `310`

### Requirement: King Air 350 photo binding

The King Air 350 record SHALL set `photo` to `"king_air_350"` so the existing `loadAircraftJpgPhoto` helper resolves the already-present `king_air_350.jpg` drawable. No new image asset SHALL be added.

#### Scenario: Aircraft list shows the King Air photo

- **WHEN** the aircraft list renders the King Air 350 card
- **THEN** the image painter SHALL resolve from the `king_air_350` drawable resource

### Requirement: King Air 350 checklist set

The King Air 350 record SHALL include exactly ten checklists, derived from `openspec/specs/king-air-checklist/checklists.pdf`, with sequential ids `0`–`9` and captions in this order:

| id | caption |
|----|---------|
| 0 | Preflight |
| 1 | Engine start (cold) |
| 2 | Before taxi |
| 3 | Before takeoff |
| 4 | Climb |
| 5 | Cruise |
| 6 | Descent |
| 7 | Approach |
| 8 | After landing |
| 9 | Parking |

Each checklist SHALL use the existing `Checklist`/`Item` schema. Each `Item` SHALL carry a `caption` and an `action`; the `details` field MAY hold multi-line sub-steps. Where the PDF groups items under a heading or a visual rule, the record MAY use a caption-only separator item (`caption == "LINE"`, matching `CHECKLIST_LINE`) consistent with the Cirrus SR22 record.

#### Scenario: Ten checklists in order

- **WHEN** the King Air 350 record is loaded
- **THEN** `checklists` SHALL contain ten entries with ids `0` through `9`
- **AND** their captions SHALL match, in order: `Preflight`, `Engine start (cold)`, `Before taxi`, `Before takeoff`, `Climb`, `Cruise`, `Descent`, `Approach`, `After landing`, `Parking`

#### Scenario: Excluded PDF sections

- **WHEN** the checklist set is compared against the source PDF
- **THEN** the standalone `Takeoff`, `Go Around`, and `One Engine Out` PDF sections SHALL NOT be included as separate checklists (takeoff speeds may appear as `details` comments where relevant)

### Requirement: Reference figures embedded as details comments

Reference numbers from the PDF that are not discrete actions (for example cruise-climb power settings and speeds, V-speeds, and limitation values such as `100% TQ / 820° ITT / 104% N1 / 1700 RPM`) SHALL be attached as `details` comments on the most relevant checklist item rather than dropped or promoted to top-level items.

#### Scenario: Cruise power table retained as a comment

- **WHEN** the `Cruise` checklist is inspected
- **THEN** at least one item SHALL carry a `details` string containing the cruise power/speed reference (for example a `% TQ` and `KIAS` pairing) drawn from the PDF

#### Scenario: Climb speed reference retained as a comment

- **WHEN** the `Climb` checklist is inspected
- **THEN** the climb power limits and/or cruise-climb speed schedule SHALL be present as a `details` comment on a relevant item
