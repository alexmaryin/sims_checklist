# Spec Delta

## MODIFIED Requirements

### Requirement: PA-24-250 checklist set

The PA-24-250 record SHALL include exactly eighteen checklists, transcribed from `openspec/specs/piper-pa24/PA-24-250 Comanche Checklist 58-60v1.1.pdf` (page images used as ground truth where the PDF text layer is corrupted), with sequential ids `0`–`17` and captions in this order:

| id | caption |
|----|---------|
| 0 | BEFORE STARTING |
| 1 | STARTING THE ENGINE |
| 2 | BEFORE TAXI |
| 3 | TAXI |
| 4 | RUN UP |
| 5 | BEFORE TAKE OFF |
| 6 | TAKE OFF |
| 7 | AFTER TAKE OFF |
| 8 | ENROUTE |
| 9 | DESCENT |
| 10 | LANDING |
| 11 | AFTER LANDING |
| 12 | ENGINE SHUT DOWN |
| 13 | BEFORE LEAVING AIRCRAFT |
| 14 | ENGINE FIRE WHEN STARTING |
| 15 | ENGINE FAILURE IN FLIGHT |
| 16 | ENGINE FIRE IN FLIGHT |
| 17 | LANDING WITHOUT MOTOR |

Captions SHALL be stored in uppercase per the catalog caption normalization.

#### Scenario: Eighteen checklists in order

- **WHEN** the PA-24-250 record is loaded
- **THEN** `checklists` SHALL contain eighteen entries with ids `0` through `17`
- **AND** their captions SHALL match the table above, in order

#### Scenario: Emergency checklists present as distinct checklists

- **WHEN** the checklist set is inspected
- **THEN** `ENGINE FIRE WHEN STARTING`, `ENGINE FAILURE IN FLIGHT`, `ENGINE FIRE IN FLIGHT`, and `LANDING WITHOUT MOTOR` SHALL each be a standalone checklist

#### Scenario: Excluded PDF sections

- **WHEN** the checklist set is compared against the source PDF
- **THEN** the `Go around` block and the `Speeds (IAS)`/weights table SHALL NOT be included, as checklists or as `details` embeds

#### Scenario: Landing pattern legs preserved

- **WHEN** the `LANDING` checklist is inspected
- **THEN** the four PDF legs `Pattern Entry`, `Downwind`, `Base`, and `Final Approach` SHALL be represented with their sub-steps intact (via `details` groupings)

### Requirement: Cautions and notes retained as details

Red-text cautions and italic notes in the PDF (for example "Do not exceed 2200 rpm in routine static test", "Avoid prolonged ground ops with Carb Heat on", "Allow the engine to warm between 800-1200 RPM for 2–4 minutes before runup", "Approach with engine power; Avoid 'floating in' at idle", "Extend Landing Gear only when landing on Firm ground is ENSURED") SHALL be attached as `details` on the adjacent checklist item rather than dropped or promoted to standalone items.

#### Scenario: Run-up caution retained

- **WHEN** the `RUN UP` checklist is inspected
- **THEN** the item preceding or following the 2000 RPM throttle setting SHALL carry a `details` string containing the "do not exceed 2200 rpm" caution
