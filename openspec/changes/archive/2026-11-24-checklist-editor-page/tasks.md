# Tasks

## 1. Page scaffold and styling

- [x] 1.1 Create `docs/checklist-editor.html` with the full HTML skeleton: `<!DOCTYPE html>`, meta tags (charset, viewport, description, theme-color matching `aircraft-format.html`), `<title>`, favicon link to `logo.png`, and the Google Fonts preconnect and stylesheet link for Bebas Neue, Source Sans 3, and JetBrains Mono. Add a `<script>` tag loading SortableJS from jsDelivr CDN (`https://cdn.jsdelivr.net/npm/sortablejs@latest/Sortable.min.js`) with a fallback detection (`window.Sortable` check). Verify the file opens without errors in a browser and SortableJS loads.
- [x]  Add the CSS design tokens (all `--color-*`, `--bg-*`, `--text-*`, `--border-*`, `--radius-*`, `--transition`, `--mono`, `--content-max` variables) copied from `aircraft-format.html`, plus base reset, body, heading, link, and code styles from the same file. Add custom CSS for the editor form layout (card sections, input fields, drag handles). Verify the page background, fonts, and colors match `aircraft-format.html` visually.
- [x]  Add the navigation bar HTML and CSS matching `aircraft-format.html` (logo + "Sims Checklist" text, back link to `index.html`). Add the footer HTML and CSS matching the same page. Verify the nav and footer render correctly and the back link navigates to `index.html`.
- [x]  Add the hero section with a kicker label, page title ("Checklist Editor"), and a short description explaining the tool. Add the page-layout container (`.wrap` max-width class). Verify the page shows the hero and has the same max-width constraint as `aircraft-format.html`.

## 2. Form: aircraft identity and performance

- [x]  Add the aircraft identity section: a single text input for `name` with a visible label. Initialize `aircraftData` as a JS object with `id: 0`, `name: ""`, `performance: { fuelCapacity: "", averageCruiseSpeed: "", averageFuelFlow: "", taxiFuel: "", contingency: "", reservesMinutes: "" }`, `checklists: []`, `photo: ""`. Verify the input binds to `aircraftData.name` on every `input` event.
- [x]  Add the performance section with six numeric inputs: `fuelCapacity` (gallons, required), `averageCruiseSpeed` (knots, required), `averageFuelFlow` (gal/hour, required), `taxiFuel` (gallons, optional, placeholder "1"), `contingency` (% of trip, optional, placeholder "5"), `reservesMinutes` (minutes, optional, placeholder "45"). Each input shows its unit label. Verify all six inputs bind to `aircraftData.performance` on every `input` event.

## 3. Form: checklist and item builder

- [x]  Add an "Add Checklist" button. On click, push a new checklist object `{ id: <next>, caption: "", category: "NORMAL", items: [] }` into `aircraftData.checklists` and re-render the checklists section. Verify a new empty checklist card appears.
- [x]  Render each checklist as a card with: a drag handle (⠿), a caption text input, a category dropdown (NORMAL / PROCEDURE / NON_NORMAL), a remove button, and a nested items section. Verify caption and category bind to the checklist object.
- [x]  Within each checklist, add an "Add Item" button and an "Add Separator" button. "Add Item" pushes `{ caption: "", action: "", details: "" }`. "Add Separator" pushes `{ caption: "LINE", action: "", details: "" }`. Both re-render the items section. Verify items appear in the card.
- [x]  Render each item as a row with: a drag handle (⠿), a caption text input, an action text input, a multiline details textarea, and a remove button. Verify all fields bind to the item object.
- [x]  Implement checklist and item removal: clicking the remove button on a checklist removes it and all its items, then renumbers remaining checklists from 0. Clicking remove on an item removes just that item. Verify the data object stays consistent after removal.

## 4. Drag-and-drop reordering (SortableJS)

- [x]  After rendering checklists, initialize `Sortable.create()` on the checklists container with `handle: '.drag-handle'`, `animation: 150`, and an `onEnd` callback that reorders `aircraftData.checklists` to match the new DOM order and renumbers ids from 0. Verify dragging a checklist card to a new position reorders the data and updates the JSON preview.
- [x]  After rendering items within each checklist, initialize `Sortable.create()` on each items container with the same config, operating on that checklist's `items` array. Verify dragging an item to a new position reorders within its checklist.
- [x]  Add touch support verification: test that long-press on a touch device initiates drag. SortableJS handles this natively; verify it works.
- [x]  Implement the CDN-failure fallback: if `window.Sortable` is undefined on page load, render up/down arrow buttons on each checklist card and each item row instead of drag handles. The buttons swap the item with its previous/next sibling in the array and renumber. Verify the fallback works when the SortableJS `<script>` tag is commented out.

## 5. localStorage auto-save

- [x]  Add a `saveState()` function that serializes `aircraftData` to JSON and writes it to `localStorage` under the key `sims-checklist-editor-state`. Debounce calls to `saveState()` to fire at most once per 500 ms. Call `saveState()` from every `input` event handler and from every add/remove/reorder action. Verify the `localStorage` key is set after editing a field.
- [x]  On page load (in the DOMContentLoaded handler), check `localStorage` for the key. If present and valid JSON, parse it and populate `aircraftData`, then re-render the entire form. If the stored data is invalid or missing, start with the empty initial state. Verify the form restores after a page refresh.
- [x]  Add a "Clear" button in the toolbar. On click, reset `aircraftData` to the initial empty state, re-render the form, and remove the `localStorage` key. Verify the form resets and `localStorage` is cleared.
- [x]  When the user loads a JSON file via the file-input control (task 6.2), call `saveState()` after populating the form so the loaded data replaces the `localStorage` entry. Verify loading a file updates the saved state.

## 6. JSON generation, live preview, and validation

- [x]  Implement the JSON generator function: walk `aircraftData`, apply defaults for empty optional performance fields (taxiFuel→1, contingency→5, reservesMinutes→45), uppercase all checklist captions and item actions, assign sequential checklist ids starting from 0, omit `checked` from items, set `id: 0` and `photo: ""` at the top level. Verify the output matches the schema in `aircraft-format.html` for a sample input.
- [x]  Add the live JSON preview panel: a read-only `<pre>` block with syntax-highlighted JSON (keys in teal, strings in amber, numbers in white, punctuation in muted gray — matching `aircraft-format.html` code blocks). Re-render on every state change (debounced ~150 ms). Verify the preview updates as the user types.
- [x]  Implement the validator: check name non-empty; `fuelCapacity`, `averageCruiseSpeed`, `averageFuelFlow` are positive numbers; `contingency` is 0–99; `reservesMinutes` is 0–89; each checklist caption is non-empty; each item has non-empty caption and (unless caption is `LINE`) non-empty action. Return an array of error strings. Run on every state change. Verify each constraint produces a specific error message.
- [x]  Render validation errors: show an error summary banner at the top of the form when errors exist, and inline error messages next to each invalid field (red text below the input). Disable the download button when any error is present; enable it when all pass. Verify the download button toggles correctly.

## 7. Download and load

- [x]  Implement the download button: generate the JSON string with 2-space indentation, create a `Blob` with type `application/json`, derive the file name from `aircraftData.name` (lowercase, spaces→underscores, strip non-alphanumeric except underscores, append `.json`), trigger download via a temporary `<a>` element with `URL.createObjectURL`. Verify the downloaded file is valid JSON matching the schema.
- [x]  Add a "Load JSON" file-input control (accept `.json`). On file selection, read the file via `FileReader`, parse with `JSON.parse`, validate the structure (has `name`, `performance` with required fields, `checklists` array with valid items). On success, populate `aircraftData`, re-render the form, and call `saveState()`. On failure, show an error message and leave the current state unchanged. Verify loading a bundled aircraft JSON (e.g. `cessna172.json`) populates all fields correctly.

## 8. Docs site integration

- [x]  Add a link to `checklist-editor.html` in the `docs/index.html` navigation bar (the `.nav-links` div, after the "JSON Format" link). Verify the link appears on desktop and leads to the editor page.
- [x]  Add a link to `checklist-editor.html` in the custom-aircraft section of `docs/index.html` — update the "Read the JSON format" button area to also offer "Open the editor" as a secondary link, or replace the area with two buttons. Verify the link is visible and navigates correctly.
- [x]  Add a cross-link to `checklist-editor.html` in `docs/aircraft-format.html` — add a link (e.g. in the hero section or as a callout) saying "Build your JSON visually — open the checklist editor". Verify the link appears and navigates to the editor.

## 9. Stage 2: JPEG icon creator

- [x]  Load Cropper.js v2.3.1 from jsDelivr (`https://cdn.jsdelivr.net/npm/cropperjs@2.3.1/dist/cropper.js`) with a `window.Cropper` detection flag. Add an "Aircraft icon" section between Performance and Checklists with a drop zone, a hidden image file input, a cropper host, an output-size select (100/150/200 px), zoom/reset controls, a live preview with metadata, and Download JPEG / Remove actions. Verify the section renders in the docs-site style.
- [x]  Implement image intake: drag-and-drop and click-to-pick, reject non-image files with an inline message, read the file via `FileReader.readAsDataURL`, rebuild the cropper with `Cropper.createCropper(img, { template })` using a `<cropper-selection aspect-ratio="1">`, and bind selection/image change events to a debounced preview refresh.
- [x]  Implement the export pipeline: `selection.$toCanvas({ width: size, height: size })`, encode with JPEG quality steps from 0.92 down to 0.32 until ≤ 50 KB (downscale in 10% steps if needed), show dimensions/quality/byte size, and enable the icon download only when the result is valid.
- [x]  Add the icon download: convert the current data URL to a `Blob` and save as `<aircraft-name>.jpg`, using the same name derivation as the JSON. Keep the JSON `photo` field as `""` — the icon is never embedded in the JSON.
- [x]  Persist the finished icon data URL under `sims-checklist-editor-icon`, restore it on load, and remove it on "Clear". Verify a refresh restores the icon and the form.
- [x]  Handle CDN failure: when `window.Cropper` is unavailable, show an unavailable notice in the icon section and leave the rest of the editor fully functional.
- [x]  Update `docs/aircraft-format.html` (icon section) and `docs/index.html` (custom-aircraft copy) to mention that the editor can also crop and export the icon.

## 10. Verification

- [x]  Open `docs/checklist-editor.html` in a browser; confirm no console errors, fonts/styles match, and both CDN libraries load.
- [x]  Create a sample aircraft with checklists, prepare a 150 px icon from a large non-square image, download both files, and confirm the JPEG is square, ≤ 50 KB, and ≤ 200 px (e.g. with `file` / `sips`).
- [x]  Verify validation still gates the JSON download, the icon draft survives a refresh, "Clear" removes both drafts, and the page still works with the Cropper script blocked.
