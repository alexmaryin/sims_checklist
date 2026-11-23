# Spec Delta

## MODIFIED Requirements

### Requirement: PA-24-250 aircraft record in the catalog

The system SHALL include the Piper PA-24-250 Comanche as a bundled aircraft record stored in the per-aircraft file `shared/src/commonMain/composeResources/files/aircraft/piper_pa24.json`. The entry SHALL use the existing `Aircraft` schema (`id`, `name`, `performance`, `checklists`, `photo`) and SHALL NOT modify the other bundled aircraft files. The `id` SHALL be `4` so it does not collide with the other bundled ids `0`–`3`, and the `name` SHALL be `"Piper PA-24-250 Comanche"`.

#### Scenario: Catalog parses with five aircraft

- **WHEN** the bundled catalog is loaded and deserialized from the per-aircraft files
- **THEN** parsing SHALL succeed and produce a list of five `Aircraft` objects

#### Scenario: New record identity

- **WHEN** the loaded list is inspected
- **THEN** it SHALL contain an aircraft with `id == 4` and `name == "Piper PA-24-250 Comanche"`
- **AND** the aircraft with ids `0`, `1`, `2`, and `3` SHALL be unchanged