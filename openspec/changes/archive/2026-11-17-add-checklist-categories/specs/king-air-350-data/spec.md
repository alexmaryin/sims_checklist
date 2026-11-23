# Spec Delta

## MODIFIED Requirements

### Requirement: King Air 350 checklist set

The King Air 350 record SHALL include exactly ten checklists, derived from `checklists.pdf`, with sequential ids `0`–`9` and captions in this order:

| id | caption |
|----|---------|
| 0 | PREFLIGHT |
| 1 | ENGINE START |
| 2 | BEFORE TAXI |
| 3 | BEFORE TAKEOFF |
| 4 | CLIMB |
| 5 | CRUISE |
| 6 | DESCENT |
| 7 | APPROACH |
| 8 | AFTER LANDING |
| 9 | PARKING |

Captions SHALL be stored in uppercase per the catalog caption normalization. The id `1` caption is reconciled from the previously specified `Engine start (cold)` to `ENGINE START`, matching the actual catalog record that this change categorizes as `PROCEDURE`.

Each checklist SHALL use the existing `Checklist`/`Item` schema. Each `Item` SHALL carry a `caption` and an `action`; the `details` field MAY hold multi-line sub-steps. Where the PDF groups items under a heading or a visual rule, the record MAY use a caption-only separator item (`caption == "LINE"`, matching `CHECKLIST_LINE`) consistent with the Cirrus SR22 record.

#### Scenario: Ten checklists in order

- **WHEN** the King Air 350 record is loaded
- **THEN** `checklists` SHALL contain ten entries with ids `0` through `9`
- **AND** their captions SHALL match, in order: `PREFLIGHT`, `ENGINE START`, `BEFORE TAXI`, `BEFORE TAKEOFF`, `CLIMB`, `CRUISE`, `DESCENT`, `APPROACH`, `AFTER LANDING`, `PARKING`

#### Scenario: Excluded PDF sections

- **WHEN** the checklist set is compared against the source PDF
- **THEN** the standalone `Takeoff`, `Go Around`, and `One Engine Out` PDF sections SHALL NOT be included as separate checklists (takeoff speeds may appear as `details` comments where relevant)
