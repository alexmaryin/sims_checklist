# Proposal

## Why

Creating a custom aircraft JSON file by hand is error-prone — users must get the schema, field names, types, uppercase conventions, and checklist structure right in a raw text editor. A visual editor on the docs site would let users build a valid JSON file interactively, reducing import failures and lowering the barrier to adding custom aircraft.

## What Changes

- Add a new standalone page `docs/checklist-editor.html` — a single-page HTML/JS visual editor for building aircraft JSON files
- **Stage 1**: Form-based editor covering the full aircraft schema (name, performance fields, checklists with items, categories, details, separators), live JSON preview, JSON download, load existing JSON, **drag-and-drop reordering** of checklists and items (via SortableJS CDN), and **localStorage auto-save** so users never lose work on refresh. The editor enforces the same validation rules the app uses at import.
- **Stage 2** (future): JPEG icon creator using **Cropper.js v2** (CDN) — drag-and-drop any image, interactive square crop area with zoom, resize, and save as JPEG within the app's icon constraints (≤ 200 px, ≤ 50 KB, near-square).
- The page reuses the existing docs site design tokens (dark theme, Bebas Neue / Source Sans 3 / JetBrains Mono fonts, amber + teal accents) and links back to `aircraft-format.html` for the full schema reference.
- The page is linked from the existing docs site navigation and from the custom-aircraft section of `index.html`.

## Capabilities

### New Capabilities
- `checklist-editor`: A standalone web-based visual editor that lets users build, validate, and download a valid aircraft JSON file through a form UI — covering aircraft identity, performance values, and nested checklists with items, categories, and separator lines. Stage 1 is the JSON editor; Stage 2 adds a JPEG icon creator with drag-and-drop, square cropping, and size-constrained export.

### Modified Capabilities
<!-- None — no existing app spec behavior changes. -->

## Impact

- **New file**: `docs/checklist-editor.html` (self-contained HTML + CSS + JS, with CDN links to SortableJS for drag-and-drop reordering; Cropper.js v2 deferred to Stage 2)
- **Modified files**: `docs/index.html` (nav link + link in the custom-aircraft section), potentially `docs/aircraft-format.html` (cross-link to the editor)
- **No app code changes**: the editor produces the same JSON the app already accepts; no Kotlin, schema, or validation logic changes
- **CDN dependencies**: SortableJS (~40 KB) loaded from jsDelivr for drag-and-drop reordering. Both are MIT-licensed and require no build step. Cropper.js v2 (~45 KB) deferred to Stage 2.
