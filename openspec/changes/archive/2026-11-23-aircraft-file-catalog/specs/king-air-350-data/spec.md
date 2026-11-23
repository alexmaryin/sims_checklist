# Spec Delta

## MODIFIED Requirements

### Requirement: King Air 350 aircraft record in the catalog

The system SHALL include the Beech King Air 350 as a bundled aircraft record stored in the per-aircraft file `shared/src/commonMain/composeResources/files/aircraft/king_air_350.json`. The entry SHALL use the existing `Aircraft` schema (`id`, `name`, `performance`, `checklists`, `photo`) and SHALL NOT modify the other bundled aircraft files. The `id` SHALL be `3` so it does not collide with the other bundled ids, and the `name` SHALL be `"Beech King Air 350"`.

#### Scenario: Catalog parses with four aircraft

- **WHEN** the bundled catalog is loaded and deserialized from the per-aircraft files
- **THEN** parsing SHALL succeed and the resulting list SHALL contain the King Air 350 record alongside the other bundled aircraft

#### Scenario: New record identity

- **WHEN** the loaded list is inspected
- **THEN** it SHALL contain an aircraft with `id == 3` and `name == "Beech King Air 350"`
- **AND** the aircraft with ids `0`, `1`, and `2` SHALL be unchanged