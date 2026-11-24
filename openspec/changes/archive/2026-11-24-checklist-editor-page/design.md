# Design

## Context

The docs site already has `aircraft-format.html` (a static reference page) and `index.html` (the main landing page), both self-contained HTML files using the same design tokens, fonts, and color scheme. The new editor page follows this established pattern: a single self-contained `.html` file, CDN-loaded libraries, no build step, vanilla JS only. GitHub Pages is static hosting but fully supports all client-side browser APIs (localStorage, File API, Blob/URL.createObjectURL, Canvas) and any CDN-hosted library. See proposal.md for motivation.

## Goals / Non-Goals

**Goals:**
- Produce a valid aircraft JSON that passes the app's import validation on the first try
- Match the docs site visual identity exactly
- Work offline after first load (CDN libraries cached by browser)
- Keep the code readable and maintainable by a single developer
- Produce an icon that passes the app's `ImageValidator` on the first try (JPEG, ≤ 200 px, ≤ 50 KB, square)
- Never lose the user's work (localStorage auto-save)

**Non-Goals:**
- Building a general-purpose JSON editor or schema builder
- Persisting state to a backend or server
- Embedding the icon into the aircraft JSON — the app imports the JSON and JPEG as two separate files
- Supporting legacy browsers without `fetch`, `Blob`, `FileReader`, or `URL.createObjectURL`

## Decisions

### CDN libraries

Two lightweight, MIT-licensed libraries loaded via CDN `<script>` tags — no npm, no build step:

1. **SortableJS** (~40 KB) — drag-and-drop reordering for checklists and items. Touch-native, works on mobile. Loaded from jsDelivr as a browser global (`window.Sortable`). If the CDN fails to load, the editor degrades to up/down arrow buttons.

2. **Cropper.js v2.3.1** (~45 KB) — interactive image cropping. CSS-in-JS (no separate CSS file needed). Loaded from jsDelivr as the browser global `window.Cropper` (an object, not a constructor), so instances are created with `Cropper.createCropper(element, options)`. The crop overlay is a `<cropper-selection>` with `aspect-ratio="1"`; export uses `selection.$toCanvas({ width, height })`. If the CDN fails to load, the icon section is shown as unavailable and the JSON editor continues to work.

**Alternative considered:** Vanilla HTML5 Drag and Drop API for reordering — rejected because it requires significant custom code for touch support, visual feedback, and edge cases that SortableJS handles out of the box. SortableJS adds only ~40 KB and is widely used (15K+ GitHub stars).

**Alternative considered:** Building a custom image cropper for Stage 2 — rejected because Cropper.js v2 is a mature, well-tested library that handles zoom, rotate, touch, and canvas export. Reimplementing this would be weeks of work for no benefit.

### localStorage auto-save

The form state is serialized to JSON and saved to `localStorage` on every change (debounced 500 ms). On page load, the editor checks for a saved state and restores it. This is supported on all browsers that GitHub Pages serves (localStorage is part of the Web Storage API, available since IE8).

The storage key is `sims-checklist-editor-state`. The "Clear" button removes this key and resets the form.

**Why localStorage over cookies:** localStorage has a 5–10 MB limit (vs 4 KB for cookies), is not sent to the server on every request, and has a simpler API. It's scoped to the page origin (`alexmaryin.github.io`), so it persists across sessions.

### Single HTML file with inline CSS and JS

All custom HTML, CSS, and JS are inline in the single `.html` file, matching the pattern set by `aircraft-format.html` and `index.html`. Only the CDN library `<script>` tags are external. This keeps deployment trivial (just commit the file) and eliminates any build tooling.

**Alternative considered:** Splitting into `.html` + `.css` + `.js` — rejected because it breaks the pattern and adds deployment complexity for no benefit.

### State management via a single JavaScript object

All form state lives in one plain JS object (`aircraftData`) structured identically to the output JSON. Form inputs bind to this object via `input` event listeners that update the object and trigger re-render of the JSON preview. Every change also triggers the debounced localStorage save. No reactive framework, no virtual DOM — just direct DOM reads/writes.

**Alternative considered:** Using a reactive library (Vue, Alpine.js) — rejected because it introduces an unnecessary dependency, contradicts the "minimal CDN" approach, and is overkill for this form's complexity.

### Nested checklist/item editing with drag-and-drop

Checklists and items are rendered as collapsible card-like sections. Each checklist card and item row has a drag handle (⠿ icon) that SortableJS uses for reordering. Adding/removing a checklist or item mutates the `aircraftData` object and re-renders the affected section. After any reorder, SortableJS's `onEnd` callback updates the array order and renumbers checklist ids.

**Fallback:** If `window.Sortable` is undefined (CDN failure), the editor detects this on load and renders up/down arrow buttons instead. Both paths update the same `aircraftData` object.

### Validation strategy

Validation runs on every `input` event and gates the download button. The validator walks the `aircraftData` object and checks:
- Name non-empty
- Performance: required fields positive, contingency 0–99, reservesMinutes 0–89
- Each checklist: caption non-empty, items non-empty (or at least one item)
- Each item: caption non-empty, action non-empty (unless caption is `LINE`)

Errors are shown inline next to the offending field and summarized at the top. Duplicate checklist captions produce a warning (not a block).

### Uppercase normalization

Checklist captions and item actions are auto-uppercased in the generated JSON, not in the form display. The form shows what the user types; the preview and download show the uppercased version. This avoids fighting the user while ensuring valid output.

### JSON preview rendering

The preview panel shows a syntax-highlighted JSON string, regenerated on every state change (debounced 150 ms). Keys, strings, numbers, and booleans get distinct colors matching the code blocks in `aircraft-format.html`. The preview is read-only — editing happens in the form, not the preview.

### File download

The download button creates a `Blob` with `application/json` type, generates an object URL, and triggers a click on a temporary `<a>` element. The file name is derived from the aircraft name by lowercasing, replacing spaces with underscores, stripping non-alphanumeric characters (except underscores), and appending `.json`.

### Loading existing JSON

A file input reads the selected file via `FileReader`, parses it with `JSON.parse`, validates the structure, and populates the `aircraftData` object. On parse or validation failure, an error message is shown and the current state is untouched.

### Icon creator (Cropper.js v2)

A new "Aircraft icon" section sits between Performance and Checklists. It is independent of `aircraftData`: the generated JSON always keeps `photo: ""`, because the app assigns the icon label on import. The icon is a second output file the user downloads and selects alongside the JSON.

**Input.** A drop zone plus a hidden `<input type="file" accept="image/*">`. Both drag-and-drop and click-to-pick are supported. Non-image files are rejected with an inline message. The file is read with `FileReader.readAsDataURL` — nothing is uploaded.

**Cropping.** `Cropper.createCropper(img, { template })` replaces the source image with a `<cropper-canvas>` containing a `<cropper-image translatable scalable zoom-around-center>` and a `<cropper-selection aspect-ratio="1" initial-coverage="0.8" movable resizable zoomable>` with grid, crosshair, and resize handles. Dragging the image pans it, the mouse wheel or Zoom buttons scale it, and dragging the selection or its handles repositions/resizes the crop. The aspect ratio is pinned to 1, so the crop cannot become non-square. The panel is ~340 px tall with a dark canvas matching the page.

**Output size.** A select offers 100, 150, and 200 px (default 150, matching the bundled icons). Export calls `selection.$toCanvas({ width: size, height: size })`, which always produces a square canvas at the requested size.

**JPEG within 50 KB.** The canvas is encoded with `canvas.toDataURL('image/jpeg', quality)` starting at 0.92, stepping down through 0.86, 0.80 … 0.32 until the decoded byte size is ≤ 50 × 1024. In the unlikely event quality alone is not enough, the canvas is downscaled in 10% steps and re-encoded, never below 40 px. The final side length, quality, and byte size are shown beside the preview. A download is offered only when the constraint is met. Because the crop is 1:1, the 10%-proportion rule is satisfied automatically.

**Preview and download.** A live preview `<img>` updates after each crop change (debounced ~250 ms). The download button converts the current data URL to a `Blob` and saves it as `<aircraft-name>.jpg` (same name derivation as the JSON). The JSON download is unaffected.

**Persistence.** The latest cropped JPEG data URL (≤ 50 KB, ~67 KB base64) is stored under `sims-checklist-editor-icon` and restored on load; "Clear" removes it. Only the finished crop is kept, not the original full-size image, which keeps localStorage usage bounded. Restoring shows the icon in the cropper at full coverage so it can still be re-cropped or replaced.

**CDN failure.** If `window.Cropper` is undefined, the section renders a short "icon creator unavailable" note and the rest of the page works normally. This mirrors the SortableJS fallback.

## Risks / Trade-offs

- **CDN dependency**: SortableJS and Cropper.js v2 are loaded from jsDelivr. If the CDN is down, drag-and-drop degrades to buttons and the icon section is unavailable; JSON editing still works. → Mitigation: both fallbacks are tested and documented. The CDN is highly available (Cloudflare-backed). Consider self-hosting the ~85 KB total in `docs/` if reliability is a concern.
- **Large source images**: decoding a 20 MP photo in the browser uses memory and may be slow on low-end devices. → Mitigation: the app only needs a 200 px square; the cropper renders at panel size, the canvas export is at most 200 px, and the original is never written to storage. Users can pick a smaller image if needed.
- **localStorage limits**: 5 MB is generous but could be hit if the user stores very large checklists. → Mitigation: aircraft JSON files are typically < 50 KB and the saved icon is capped at ~67 KB base64. The debounced save only fires once per 500 ms. No risk of hitting the limit in practice.
- **No server-side persistence**: localStorage is browser-scoped. If the user switches browsers or clears site data, they lose the saved state. → Mitigation: the load-from-file feature lets them recover from a previously downloaded JSON. The download is the permanent save; localStorage is just a convenience.
- **Large checklists may be slow to render**: a 50-item checklist re-renders the JSON preview on every keystroke. → Mitigation: debounce the preview update by 150 ms; the validator is fast enough to run synchronously.
- **Browser compatibility**: `Blob`, `URL.createObjectURL`, `FileReader`, `localStorage`, and template literals require a modern browser. → Mitigation: consistent with the existing docs pages' browser requirements; no legacy browser support needed.
