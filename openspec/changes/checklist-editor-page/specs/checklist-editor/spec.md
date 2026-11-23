# Spec Delta

## Purpose

Provides a standalone web-based visual editor that lets users build, validate, and download a valid aircraft JSON file without writing JSON by hand, reducing import errors when adding custom aircraft to the app.

## ADDED Requirements

### Requirement: Single page with CDN dependencies only

The editor SHALL be delivered as a single `docs/checklist-editor.html` file containing all custom HTML, CSS, and JavaScript inline. The page MAY load external libraries via CDN `<script>` tags (no build step or bundler required). The page SHALL function in any modern browser and remain usable offline for the core editing experience after the initial load and CDN cache.

#### Scenario: CDN-loaded SortableJS
- **WHEN** the page loads
- **THEN** SortableJS SHALL be loaded from a CDN (jsDelivr) via a single `<script>` tag and used for drag-and-drop reordering

#### Scenario: No build step or bundler
- **WHEN** the page source is inspected
- **THEN** no `<script type="module">`, `import` statements, or build tooling SHALL be required — all custom code is inline, external libraries are loaded as plain browser globals

#### Scenario: CDN unavailable fallback
- **WHEN** the CDN is unreachable (offline, blocked)
- **THEN** the editor SHALL still function for core editing (form fields, validation, preview, download); drag-and-drop reordering SHALL degrade gracefully to up/down button controls

### Requirement: Consistent visual design

The editor page SHALL reuse the design tokens defined in `docs/aircraft-format.html`: the same dark background palette, the same Bebas Neue / Source Sans 3 / JetBrains Mono font stack, and the same amber primary and teal accent colors. The page SHALL include the same navigation bar linking back to `index.html` and a footer matching the rest of the docs site.

#### Scenario: Same look and feel as the JSON format page
- **WHEN** a user opens `checklist-editor.html` after viewing `aircraft-format.html`
- **THEN** the page SHALL use the same color scheme, typography, nav bar layout, and footer

#### Scenario: Back navigation
- **WHEN** the user clicks the back link in the nav bar
- **THEN** the browser SHALL navigate to `index.html`

### Requirement: Aircraft identity form

The editor SHALL provide input fields for the aircraft's top-level identity: a required non-empty `name` text field. The `id` and `photo` fields SHALL NOT be user-editable — they are assigned by the app at import time and SHALL be set to `0` and `""` (empty string) respectively in the generated JSON.

#### Scenario: Name is required
- **WHEN** the name field is empty
- **THEN** the validation SHALL report a missing name error and the JSON download SHALL not be offered

#### Scenario: id and photo omitted from editing
- **WHEN** the user inspects the form
- **THEN** no input fields for `id` or `photo` SHALL be present

#### Scenario: Generated JSON has id 0 and empty photo
- **WHEN** the user downloads the JSON
- **THEN** the output SHALL contain `"id": 0` and `"photo": ""`

### Requirement: Performance fields form

The editor SHALL provide numeric input fields for all six performance properties: three required (`fuelCapacity`, `averageCruiseSpeed`, `averageFuelFlow`) and three optional with visible defaults (`taxiFuel` default 1, `contingency` default 5, `reservesMinutes` default 45). Each field SHALL display its unit (gallons, knots, gal/hour, %, minutes) and enforce the same constraints the app uses: required fields must be positive, `contingency` must be 0–99, and `reservesMinutes` must be 0–89.

#### Scenario: Required performance fields must be positive
- **WHEN** the user enters zero or a negative value for `fuelCapacity`, `averageCruiseSpeed`, or `averageFuelFlow`
- **THEN** the validation SHALL flag the field as invalid and block JSON download

#### Scenario: Optional fields use defaults when empty
- **WHEN** the user leaves `taxiFuel`, `contingency`, or `reservesMinutes` empty
- **THEN** the generated JSON SHALL contain the default value (1, 5, 45 respectively)

#### Scenario: Contingency range enforced
- **WHEN** the user enters a value outside 0–99 for `contingency`
- **THEN** the validation SHALL flag the field as invalid

#### Scenario: Reserves range enforced
- **WHEN** the user enters a value outside 0–89 for `reservesMinutes`
- **THEN** the validation SHALL flag the field as invalid

### Requirement: Checklist builder

The editor SHALL provide an interactive list of checklists. The user SHALL be able to add a new checklist, remove an existing checklist, and set its `caption` (required, non-empty) and `category` (dropdown: NORMAL, PROCEDURE, NON_NORMAL — defaulting to NORMAL). Checklist ids SHALL be auto-assigned sequentially starting from 0 and SHALL NOT be user-editable.

#### Scenario: Adding a checklist
- **WHEN** the user clicks "Add checklist"
- **THEN** a new checklist section SHALL appear with an empty caption field and the default NORMAL category

#### Scenario: Removing a checklist
- **WHEN** the user removes a checklist
- **THEN** that checklist and all its items SHALL be removed from the form and the remaining checklists SHALL be renumbered sequentially from 0

#### Scenario: Sequential ids in output
- **WHEN** the user has three checklists and removes the middle one
- **THEN** the generated JSON SHALL assign ids 0, 1 to the remaining checklists

### Requirement: Drag-and-drop reordering

The editor SHALL support drag-and-drop reordering of checklists and of items within a checklist using SortableJS. When SortableJS is unavailable (CDN blocked or offline), the editor SHALL fall back to up/down arrow buttons for reordering. After any reorder — drag or button — checklist ids SHALL be renumbered sequentially from 0.

#### Scenario: Drag-and-drop checklist reorder
- **WHEN** the user drags a checklist card to a new position and releases
- **THEN** the checklists SHALL be reordered in the form and the JSON preview SHALL reflect the new order within one second

#### Scenario: Drag-and-drop item reorder
- **WHEN** the user drags an item to a new position within its checklist
- **THEN** the items SHALL be reordered and the JSON preview SHALL update

#### Scenario: Touch device support
- **WHEN** the user is on a touch device and long-presses a checklist or item
- **THEN** the drag-and-drop SHALL work with touch gestures (SortableJS is touch-native)

#### Scenario: Fallback to buttons when CDN unavailable
- **WHEN** SortableJS fails to load from the CDN
- **THEN** each checklist card and each item row SHALL show up/down arrow buttons for reordering instead

#### Scenario: Ids renumbered after reorder
- **WHEN** the user reorders checklists by drag-and-drop
- **THEN** the generated JSON SHALL assign ids 0, 1, 2, ... in the new order

### Requirement: Checklist items builder

Within each checklist, the editor SHALL provide an interactive list of items. The user SHALL be able to add an item, remove an item, and set its `caption` (required, non-empty), `action` (required, non-empty for normal items), and `details` (optional, multiline text). The editor SHALL support adding a `LINE` separator — a special item whose caption is `LINE` and whose action may be empty. Item `checked` fields SHALL NOT be user-editable and SHALL be omitted from the output.

#### Scenario: Adding a normal item
- **WHEN** the user adds an item and fills in caption and action
- **THEN** the item SHALL appear in the checklist with both fields

#### Scenario: LINE separator
- **WHEN** the user adds a LINE separator
- **THEN** an item with `"caption": "LINE"` and empty `"action"` SHALL appear in the JSON

#### Scenario: Item action auto-uppercased
- **WHEN** the user types an item action in lowercase
- **THEN** the generated JSON SHALL store the action in uppercase

#### Scenario: Checklist caption auto-uppercased
- **WHEN** the user types a checklist caption in mixed case
- **THEN** the generated JSON SHALL store the caption in uppercase

#### Scenario: Checked field omitted
- **WHEN** the user downloads the JSON
- **THEN** no item SHALL contain a `checked` field

### Requirement: localStorage auto-save

The editor SHALL automatically save the current form state to `localStorage` on every change (debounced). On page load, if saved state exists, the editor SHALL restore it automatically. The user SHALL be able to clear the saved state via a "Clear" button that resets the form and removes the `localStorage` entry.

#### Scenario: Auto-save on change
- **WHEN** the user edits any form field
- **THEN** the form state SHALL be saved to `localStorage` within 500 ms (debounced)

#### Scenario: Restore on page load
- **WHEN** the user opens the editor and a saved state exists in `localStorage`
- **THEN** the form SHALL be populated with the saved data automatically

#### Scenario: Clear resets everything
- **WHEN** the user clicks "Clear"
- **THEN** the form SHALL reset to the empty initial state and the `localStorage` entry SHALL be removed

#### Scenario: Load file overrides auto-save
- **WHEN** the user loads a JSON file via the file-input control
- **THEN** the loaded data SHALL replace the form state and the new state SHALL be saved to `localStorage`, replacing any previous entry

### Requirement: Live JSON preview

The editor SHALL display a live, syntax-highlighted JSON preview of the generated aircraft record, updated as the user edits the form. The preview SHALL show the exact JSON that will be downloaded, including auto-assigned ids, uppercase normalization, and default values for omitted optional fields.

#### Scenario: Preview reflects form state
- **WHEN** the user changes the aircraft name
- **THEN** the JSON preview SHALL update to show the new name within one second

#### Scenario: Preview shows defaults for empty optional fields
- **WHEN** the user leaves all optional performance fields empty
- **THEN** the preview SHALL show `taxiFuel: 1`, `contingency: 5`, `reservesMinutes: 45`

### Requirement: Validation feedback

The editor SHALL validate the entire form continuously and display a clear error state when the JSON cannot be produced. Validation errors SHALL identify the specific field or checklist that is invalid. The download action SHALL be disabled when any validation error is present.

#### Scenario: Missing required field blocks download
- **WHEN** the aircraft name is empty or a required performance field is non-positive
- **THEN** the download button SHALL be disabled and the relevant error message SHALL be visible

#### Scenario: Duplicate checklist captions warned
- **WHEN** two checklists have the same caption (after uppercase normalization)
- **THEN** the editor SHALL display a warning (but not block the download, since the app permits duplicate captions)

#### Scenario: Valid form enables download
- **WHEN** all required fields are filled and all constraints are satisfied
- **THEN** the download button SHALL be enabled

### Requirement: JSON download

The editor SHALL offer a download button that saves the generated JSON as a file. The file name SHALL be derived from the aircraft name (lowercased, spaces replaced with underscores, non-alphanumeric characters stripped, with `.json` appended). The JSON SHALL be formatted with two-space indentation and UTF-8 encoded.

#### Scenario: File name from aircraft name
- **WHEN** the aircraft name is "Cessna 172 Skyhawk"
- **THEN** the downloaded file SHALL be named `cessna_172_skyhawk.json`

#### Scenario: Valid JSON output
- **WHEN** the downloaded file is parsed
- **THEN** it SHALL be valid JSON matching the aircraft schema documented in `aircraft-format.html`

### Requirement: Load existing JSON

The editor SHALL provide a file-input control that lets the user load an existing aircraft JSON file into the form, populating all fields so the user can edit and re-export it. Loading a file SHALL replace the current form state entirely.

#### Scenario: Load populates all fields
- **WHEN** the user loads a valid aircraft JSON file
- **THEN** the aircraft name, performance fields, and all checklists with their items SHALL appear in the form

#### Scenario: Load replaces current state
- **WHEN** the user has existing form data and loads a file
- **THEN** the previous form state SHALL be replaced entirely by the loaded data

#### Scenario: Invalid file rejected
- **WHEN** the user loads a file that is not valid JSON or does not match the aircraft schema
- **THEN** the editor SHALL display an error and SHALL NOT modify the current form state

### Requirement: Stage 2 JPEG icon creator

Stage 2 SHALL add a JPEG icon creator section to the editor page, using **Cropper.js v2** loaded from a CDN. The user SHALL be able to drag-and-drop any image onto a drop zone, see it in an interactive cropper with a forced 1:1 (square) aspect ratio, zoom and reposition the crop, choose an output size (up to 200 px per side), and download the result as a JPEG within the app's icon constraints (≤ 200 px, ≤ 50 KB, near-square). This capability is explicitly deferred to Stage 2 and SHALL NOT be included in the Stage 1 implementation.

#### Scenario: Stage 1 does not include icon creator
- **WHEN** the Stage 1 page is delivered
- **THEN** no image upload, crop, or JPEG export controls SHALL be present

#### Scenario: Stage 2 drag-and-drop image
- **WHEN** Stage 2 is implemented and the user drops an image onto the icon area
- **THEN** the image SHALL appear in the Cropper.js editor with a square crop overlay the user can reposition and zoom

#### Scenario: Stage 2 forced square crop
- **WHEN** the Stage 2 cropper is active
- **THEN** the crop area SHALL maintain a 1:1 aspect ratio at all times

#### Scenario: Stage 2 JPEG export within constraints
- **WHEN** the user exports the icon from Stage 2
- **THEN** the output SHALL be a JPEG, at most 200 px on each side, at most 50 KB, and the width and height SHALL differ by no more than 10%
