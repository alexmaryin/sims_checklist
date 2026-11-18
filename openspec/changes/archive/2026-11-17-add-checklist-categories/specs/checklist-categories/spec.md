# Spec Delta

## Purpose

Classifies every checklist into a group (NORMAL, NON-NORMAL, PROCEDURE), encodes that group as a distinct card color in the checklists list following the app color conventions, and defines how completion is indicated for non-NORMAL groups.

## ADDED Requirements

### Requirement: Optional checklist category field

Each checklist record in the catalog SHALL support an optional `category` field with exactly three allowed values: `NORMAL`, `NON_NORMAL`, `PROCEDURE`. When the field is absent, the checklist SHALL be treated as `NORMAL`. Existing catalog records without the field SHALL continue to deserialize without error.

#### Scenario: Missing category defaults to NORMAL

- **WHEN** a checklist record without a `category` field is deserialized
- **THEN** its category SHALL be `NORMAL`

#### Scenario: Explicit category is honored

- **WHEN** a checklist record with `"category": "NON_NORMAL"` or `"category": "PROCEDURE"` is deserialized
- **THEN** its category SHALL be `NON_NORMAL` or `PROCEDURE` respectively

#### Scenario: Catalog without categories stays valid

- **WHEN** the bundled catalog is loaded and every checklist record lacks the `category` field
- **THEN** parsing SHALL succeed and all checklists SHALL be `NORMAL`

### Requirement: Category color encoding in the checklists list

The checklists list SHALL color each checklist card by category: NORMAL cards SHALL keep the existing behavior (default surface when pending, the app's completed-green when completed); PROCEDURE cards SHALL use a light-blue container; NON-NORMAL cards SHALL use a red container. The PROCEDURE and NON-NORMAL colors SHALL be the same in light and dark themes, with dark text, consistent with how the completed-green behaves today.

#### Scenario: Pending NORMAL card unchanged

- **WHEN** a NORMAL checklist is not completed
- **THEN** its card SHALL use the default surface color, as before this change

#### Scenario: Completed NORMAL card unchanged

- **WHEN** a NORMAL checklist is completed
- **THEN** its card SHALL use the existing completed-green color

#### Scenario: PROCEDURE card is light blue

- **WHEN** a PROCEDURE checklist is displayed in the list, in either theme, completed or not
- **THEN** its card container SHALL be the light-blue category color with dark text

#### Scenario: NON-NORMAL card is red

- **WHEN** a NON-NORMAL checklist is displayed in the list, in either theme, completed or not
- **THEN** its card container SHALL be the red category color with dark text

### Requirement: Completion indication for non-NORMAL checklists

NON-NORMAL and PROCEDURE checklists SHALL keep tracking item checkmarks and overall completion exactly as NORMAL checklists do. Completion SHALL NOT change the card color of these categories; instead, a completed NON-NORMAL or PROCEDURE card SHALL display a visible check marker.

#### Scenario: Checkmarks still tracked

- **WHEN** the user checks items of a NON-NORMAL or PROCEDURE checklist in the details screen
- **THEN** the items SHALL be marked checked and the checklist SHALL become completed when all items are checked, as for NORMAL checklists

#### Scenario: Completed non-NORMAL card shows check marker

- **WHEN** a NON-NORMAL or PROCEDURE checklist is completed
- **THEN** its list card SHALL keep its category color AND show a check marker indicating completion

#### Scenario: Pending non-NORMAL card has no check marker

- **WHEN** a NON-NORMAL or PROCEDURE checklist is not completed
- **THEN** its list card SHALL show no check marker

### Requirement: Category assignments in the bundled catalog

The bundled catalog SHALL mark exactly these checklists with explicit categories: Piper PA-24 `RUN UP` as `PROCEDURE`; King Air 350 `ENGINE START` as `PROCEDURE`; Citation X `ENGINE START` as `PROCEDURE`; Piper PA-24 `ENGINE FIRE WHEN STARTING`, `ENGINE FAILURE IN FLIGHT`, `ENGINE FIRE IN FLIGHT`, and `LANDING WITHOUT MOTOR` as `NON_NORMAL`. All other checklists SHALL remain without a `category` field (default NORMAL).

#### Scenario: Procedure checklists marked

- **WHEN** the bundled catalog is loaded
- **THEN** the `RUN UP` checklist of Piper PA-24 and the `ENGINE START` checklists of King Air 350 and Citation X SHALL have category `PROCEDURE`

#### Scenario: Emergency checklists marked

- **WHEN** the bundled catalog is loaded
- **THEN** the four Piper PA-24 emergency checklists (`ENGINE FIRE WHEN STARTING`, `ENGINE FAILURE IN FLIGHT`, `ENGINE FIRE IN FLIGHT`, `LANDING WITHOUT MOTOR`) SHALL have category `NON_NORMAL`

#### Scenario: All other checklists default

- **WHEN** the bundled catalog is loaded
- **THEN** every checklist other than the seven listed above SHALL have category `NORMAL` via the absent-field default

### Requirement: Checklist captions normalized to uppercase

Every checklist caption in the bundled catalog SHALL be stored in uppercase letters. Parenthetical group qualifiers in captions SHALL be removed: the two `Engine start (normal)` captions (Cessna 172, Cirrus SR22) SHALL become `ENGINE START`. Caption wording beyond case and qualifier removal SHALL NOT change.

#### Scenario: Captions are uppercase

- **WHEN** the bundled catalog is inspected
- **THEN** every checklist caption SHALL consist of uppercase letters (digits, symbols and spacing preserved)

#### Scenario: Normal qualifier removed

- **WHEN** the Cessna 172 and Cirrus SR22 records are inspected
- **THEN** their first checklist caption SHALL be `ENGINE START`, with no `(normal)` qualifier

### Requirement: Item actions normalized to uppercase

Every item `action` value in the bundled catalog SHALL be stored in uppercase letters, with digits, fractions, symbols and units preserved as written. Item `caption` and `details` values SHALL NOT be modified by this normalization.

#### Scenario: Actions are uppercase

- **WHEN** the bundled catalog is inspected
- **THEN** no item `action` SHALL contain lowercase letters

#### Scenario: Item captions and details untouched

- **WHEN** the catalog is compared before and after normalization
- **THEN** all item `caption` and `details` values SHALL be byte-identical to their pre-normalization content
