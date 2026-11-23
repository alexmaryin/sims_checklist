# Spec Delta

## MODIFIED Requirements

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