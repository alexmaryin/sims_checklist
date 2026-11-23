# custom-aircraft-import Specification

## Purpose

Lets users add their own aircraft through a dedicated import screen with two-file selection, full validation, copyright attestation, and app-assigned identity.

## Requirements

### Requirement: Import screen with two file selections

The import screen SHALL provide two separate file selection controls — one for the aircraft JSON file and one for the icon image — a copyright attestation checkbox, per-file validation feedback, and a save action. The save action SHALL be enabled only when both selected files pass validation and the attestation checkbox is checked.

#### Scenario: Initial state

- **WHEN** the import screen opens
- **THEN** both file controls SHALL be empty, the checkbox SHALL be unchecked, and the save action SHALL be disabled

#### Scenario: Validation feedback per file

- **WHEN** a selected file fails validation
- **THEN** the screen SHALL show the specific failure reason next to that file's control

#### Scenario: Save enabled only when everything passes

- **WHEN** both files are valid but the attestation checkbox is unchecked
- **THEN** the save action SHALL remain disabled

### Requirement: Aircraft JSON validation

Before anything is written, the system SHALL validate the selected JSON: it SHALL deserialize strictly into the `Aircraft` schema, and SHALL satisfy semantic rules — performance values positive, checklist ids unique within the aircraft, item captions and actions non-empty, and every checklist category one of `NORMAL`, `NON_NORMAL`, `PROCEDURE`. Any failure SHALL reject the file with a message describing the problem.

#### Scenario: Malformed JSON rejected

- **WHEN** the selected file is not valid JSON
- **THEN** the file SHALL be rejected with a syntax error message and nothing SHALL be written

#### Scenario: Missing required data rejected

- **WHEN** the JSON deserializes but lacks required fields such as `performance` or `checklists`
- **THEN** the file SHALL be rejected with a message naming the missing data

#### Scenario: Invalid semantics rejected

- **WHEN** the JSON contains a non-positive fuel capacity, duplicate checklist ids, or an unknown category value
- **THEN** the file SHALL be rejected with a message describing the violated rule

#### Scenario: Valid file accepted

- **WHEN** the JSON passes schema and semantic validation
- **THEN** the file control SHALL show a valid state

### Requirement: Icon image validation

Before anything is written, the system SHALL validate the selected icon: it SHALL be a JPEG image, at most 50 KB in size, at most 200 pixels on each side, and its width and height SHALL differ by no more than 10% of the larger side. Any failure SHALL reject the file with a message describing the problem.

#### Scenario: Non-JPEG rejected

- **WHEN** the selected image is a PNG or other non-JPEG format
- **THEN** the file SHALL be rejected with a format error message

#### Scenario: Oversized file rejected

- **WHEN** the selected JPEG is larger than 50 KB
- **THEN** the file SHALL be rejected with a size error message

#### Scenario: Oversized dimensions rejected

- **WHEN** the selected JPEG has a side longer than 200 pixels
- **THEN** the file SHALL be rejected with a dimensions error message

#### Scenario: Non-square proportions rejected

- **WHEN** the selected JPEG is 150x180 pixels (difference above 10%)
- **THEN** the file SHALL be rejected with a proportions error message

#### Scenario: Valid icon accepted

- **WHEN** the selected JPEG is 150x150 pixels and 40 KB
- **THEN** the file control SHALL show a valid state

#### Scenario: Slightly non-square accepted

- **WHEN** the selected JPEG is 152x150 pixels and within the size limit
- **THEN** the file SHALL pass validation

### Requirement: Copyright attestation

The import screen SHALL require the user to check a box declaring that they own the image or obtained it from a free source. Import SHALL NOT proceed without this attestation. No license metadata SHALL be required in the aircraft file or stored with it.

#### Scenario: Attestation gates import

- **WHEN** both files are valid and the user attempts to save without checking the attestation box
- **THEN** the save action SHALL be unavailable and no files SHALL be written

#### Scenario: Attestation given

- **WHEN** the user checks the attestation box with both files valid
- **THEN** the save action SHALL become available

### Requirement: App-assigned id and photo label

On save, the system SHALL replace the id in the user's JSON with the next integer after the highest id among all currently loaded aircraft, and SHALL replace the `photo` value with the file name of the icon as stored in the aircraft directory. Both files SHALL then be written into the aircraft directory under the app data root.

#### Scenario: User id replaced

- **WHEN** the user imports a JSON declaring `id: 99` while the highest existing aircraft id is `4`
- **THEN** the stored record SHALL have `id: 5`

#### Scenario: Photo label rewritten

- **WHEN** the import is saved
- **THEN** the stored record's `photo` value SHALL reference the stored icon file name, not any value from the user's original JSON

#### Scenario: Both files persisted

- **WHEN** the import is saved successfully
- **THEN** the aircraft JSON and the icon SHALL both exist in the aircraft directory and survive an app restart

### Requirement: Imported aircraft fully functional

After a successful import, the aircraft list SHALL show the new aircraft with its imported icon without an app restart, and the aircraft SHALL support checklists and the fuel calculator exactly like bundled aircraft, using the imported performance values.

#### Scenario: Card appears immediately

- **WHEN** the import completes and the user returns to the aircraft list
- **THEN** a card for the new aircraft SHALL be visible, rendering the imported icon from the app data directory

#### Scenario: Fuel calculator uses imported data

- **WHEN** the user opens the fuel calculator for the imported aircraft
- **THEN** the calculations SHALL use the imported performance values

#### Scenario: Checklists work

- **WHEN** the user opens a checklist of the imported aircraft and toggles items
- **THEN** item checking and completion tracking SHALL behave as for bundled aircraft

### Requirement: Duplicate aircraft name rejection

At import, after the JSON passes schema and semantic validation, the system SHALL compare the aircraft name (case-insensitive, trimmed) against all currently loaded aircraft. If a match exists, the import SHALL be rejected with a message naming the conflicting aircraft, and nothing SHALL be written.

#### Scenario: Duplicate of a bundled aircraft rejected

- **WHEN** the user imports a JSON whose name matches an existing bundled aircraft (ignoring case and surrounding whitespace)
- **THEN** the file SHALL be rejected with a message stating that an aircraft with that name already exists

#### Scenario: Duplicate of a custom aircraft rejected

- **WHEN** the user imports the same aircraft JSON a second time
- **THEN** the second import SHALL be rejected with the duplicate-name message and no new files SHALL be written

#### Scenario: Distinct name accepted

- **WHEN** the imported name differs from every loaded aircraft name
- **THEN** the duplicate check SHALL pass

### Requirement: Removal of custom aircraft

The aircraft list SHALL show a trash-can icon in the bottom-left corner of each custom aircraft card, with the aircraft title centered and the fuel calculator icon in the bottom-right corner. Bundled aircraft cards SHALL NOT show the trash-can icon. Activating it SHALL ask for confirmation; on confirmation the system SHALL delete the aircraft's JSON and icon files, remove its id from the custom index, remove it from the loaded catalog, and refresh the list without an app restart.

#### Scenario: Trash icon only on custom cards

- **WHEN** the aircraft list renders bundled and custom aircraft
- **THEN** only custom aircraft cards SHALL show the trash-can icon in the bottom-left corner, with the title centered and the fuel calculator icon in the bottom-right corner

#### Scenario: Confirmation required

- **WHEN** the user taps the trash-can icon
- **THEN** a confirmation SHALL be shown and nothing SHALL be deleted until the user confirms

#### Scenario: Confirmed removal deletes files and refreshes the list

- **WHEN** the user confirms removal of a custom aircraft
- **THEN** its `custom_<id>.json` and `custom_<id>.jpg` SHALL be deleted, its id SHALL be removed from the custom index, and the card SHALL disappear from the list without an app restart

#### Scenario: Cancelled removal keeps everything

- **WHEN** the user dismisses the confirmation without confirming
- **THEN** no files SHALL be deleted and the aircraft SHALL remain in the list

#### Scenario: Removed aircraft stays removed after restart

- **WHEN** the app restarts after a custom aircraft was removed
- **THEN** the removed aircraft SHALL NOT reappear and no missing-file warning SHALL be shown for it
