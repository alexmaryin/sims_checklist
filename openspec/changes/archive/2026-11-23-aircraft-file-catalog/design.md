# Design

## Context

See proposal.md — Why. Current mechanics that shape this design:

- `AircraftBaseImpl` reads one resource file (`Res.readBytes("files/aircraft.json")`) and decodes `List<Aircraft>`; `AircraftRepositoryImpl` holds the list in memory (Koin singleton, loaded once via `runBlocking`) and mutates checklist state in place.
- Photos resolve through `Res.allDrawableResources[name]` in `loadAircraftJpgPhoto` — compile-time resources only.
- Both targets are JVM; `commonMain` already uses `java.io.File` / `java.nio` (e.g. `RoomConverter`), so no okio/kotlinx-io dependency is needed.
- Desktop storage today: `airports.db` in `~/.simschecklist` (`di/getRoomBuilder.kt`), update CSVs in `java.io.tmpdir` (`services/airportService/getFilepath.kt`). Android: `getDatabasePath` and `filesDir` via `AppAndroid.instance()`.
- The FollowMyMus project provides two proven patterns to port: `getAppDataDir()` (OS-conventional root) and `expect class FileHandler` (desktop `FileDialog`, Android SAF with activity binding).

## Goals / Non-Goals

**Goals:**

- One storage root per platform, following OS conventions; all file IO routed through it.
- Per-aircraft files with deploy-sync and per-file fallback; custom aircraft import with validation before any write.
- Keep the `Aircraft` schema and `Int` ids unchanged (no navigation ripple).

**Non-Goals:**

- No editing/deleting custom aircraft from the UI (files can be managed manually in the aircraft directory; missing/invalid custom files are skipped with a warning).
- No cloud sync, no sharing/export of aircraft, no automated copyright verification (attestation only).
- No change to checklist state persistence (still in-memory per session).
- Android database location stays at the platform-standard path.

## Decisions

### D1: Storage root — expect/actual `getAppDataDir(): File`

Port FollowMyMus's `getAppDataDir()` as the desktop actual; Android actual returns `context.filesDir`. Because both targets are JVM, the expect/actual lives in `commonMain` with `java.io.File` types (no okio). The existing `getFilePath(filename)` becomes `File(getAppDataDir(), filename).absolutePath`, so airport CSV downloads move to the root automatically. Aircraft files live in `<root>/aircraft/`.

Alternatives: okio `FileSystem` (adds a dependency for no benefit on JVM-only targets); `~/.simschecklist` everywhere (rejected by user — OS conventions required).

### D2: Desktop DB migration — move once, verify, then delete legacy

In desktop `getRoomBuilder`: if `<root>/airports.db` exists, use it. Else if `~/.simschecklist/airports.db` exists, `Files.move` it (fallback: copy + verify size + delete); on any failure keep the legacy file and create a fresh DB at the new path (airports are re-downloadable). Legacy is removed only after a successful move. Android untouched.

Alternative: copy without deleting legacy — rejected; leaves two divergent databases, which is exactly the inconsistency this change removes.

### D3: Bundled set identified by a manifest, custom files by name prefix

Resources gain `files/aircraft/catalog.json` — a manifest listing the five bundled file names — alongside the per-aircraft files. Deploy-sync copies manifest + aircraft files and writes `<root>/aircraft/.deployed-version` containing the app version (from BuildKonfig). On start: version differs (or marker missing) → re-copy bundled files; equal → skip. Custom aircraft are files named `custom_<id>.json` / `custom_<id>.jpg`; anything in the directory not in the manifest and matching the prefix is a custom aircraft. Sync never touches `custom_*` files.

Alternatives: runtime enumeration of resource files (Compose resources API doesn't reliably enumerate `files/`); id ranges for custom aircraft (breaks when future bundled aircraft take the next id); no manifest, hard-coded bundled list (duplicates knowledge already in resources).

### D4: Loading — per-file try/fallback in `AircraftBaseImpl`

For each manifest entry: read app file → strict decode + semantic validation → on any failure read the bundled resource. Then load each `custom_*` file the same way, with no fallback; failures are collected and surfaced as a snackbar warning ("Aircraft X skipped: file missing or invalid"). One bad file never aborts the catalog. The same validator is reused at import time (D6).

### D5: Photo resolution — file first, drawable second

`photo` keeps its meaning for bundled records (drawable key). Custom records store `photo = "custom_<id>"`; the list screen resolves it by checking `<root>/aircraft/custom_<id>.jpg` first. New expect/actual `decodeImageFile(bytes): ImageBitmap` — Android `BitmapFactory.decodeByteArray`, desktop Skiko `Image.makeFromEncoded(...).toComposeImageBitmap()`. `loadAircraftJpgPhoto` becomes a `produceState`-based loader keyed on the photo value: file exists → decoded bitmap, else `painterResource`. Bundled cards keep their current rendering path.

Alternative: base64-embedded icons in JSON — rejected by user (two separate files).

### D6: Validation — one pure validator, used at import and at load

`AircraftValidator` (common, no IO): strict `Json { ignoreUnknownKeys = false }` decode of a single `Aircraft`, then semantic rules — performance fields > 0, unique checklist ids, non-empty item captions/actions, category enum enforced by decoding. Returns a list of human-readable errors. Image validation: bytes ≤ 50 KB, JPEG magic `FF D8 FF`, decoded dimensions ≤ 200 px per side and `abs(w - h) ≤ 0.10 * max(w, h)`. Import writes nothing until both validators return empty error lists and the attestation is checked.

### D7: Import flow — FileHandler + Decompose child

Port FollowMyMus's `expect class FileHandler { suspend fun openFile(mimeType): Pair<String, ByteArray>? }`. Desktop: `FileDialog(LOAD)` with extension filter (`.json` / `.jpg,.jpeg`). Android: `ActivityResultContracts.OpenDocument` via `activityResultRegistry` + `suspendCancellableCoroutine`, activity bound at startup exactly as FollowMyMus does. New `Configuration.AddCustomAircraft` + component holding: json bytes/name + errors, image bytes/name + errors, attested flag, computed save-enabled state. On save: id := max(loaded ids) + 1, rewrite `id` and `photo`, serialize, write `custom_<id>.json` + `custom_<id>.jpg`, call new `AircraftRepository.addAircraft(...)` which appends to the in-memory list and refreshes main screen state, navigate back.

### D8: Splitting the catalog — content-preserving transformation

Split `aircraft.json` into five pretty-printed single-record files named after their photo keys (`cessna172.json`, `cirrus_sr22.json`, `citationx.json`, `king_air_350.json`, `piper_pa24.json`); byte-level content of each record unchanged. A test asserts the reassembled list equals the original catalog before the monolith is deleted.

### D9: Duplicate names blocked at import; custom aircraft removable from the card

Duplicate detection compares the validated record's name (trimmed, case-insensitive) against `repository.getAll()` inside `onJsonPicked`, surfacing the conflict as a JSON validation error — same feedback channel as other rejections, no new UI state. Blocking was chosen over warn-and-allow to keep the list unambiguous; ids are already collision-free by construction (D7).

Removal is custom-only: the card row gains a trash-can `IconButton` next to the fuel calculator icon when `photo` starts with `custom_`. It emits `MainScreenEvent.RemoveAircraftConfirm(id)` → snackbar confirmation (the existing `MainScreenSnack` pattern used by Drop airports database) → `RemoveAircraft(id)`. The handler deletes `custom_<id>.json`/`.jpg`, rewrites `.custom-index` without the id (so the missing-file warning mechanism stays consistent), calls new `AircraftRepository.removeAircraft(id)`, and updates the list state. Bundled cards never render the icon, so bundled data cannot be removed from the UI. The trash-can drawable is user-provided (Google icons XML); none of the existing drawables is a trash can.

## Risks / Trade-offs

- [DB migration interrupted (crash mid-move)] → copy+verify+delete fallback; legacy kept until verified; airports DB is re-downloadable, worst case is a re-download.
- [Downgrade after migration: old app version looks in `~/.simschecklist` and finds nothing] → accepted; old version recreates an empty DB and the user re-downloads airports. Documented in migration notes.
- [Android SAF picker needs a bound activity; process death loses the callback] → FollowMyMus pattern is proven in production; null result simply leaves the file control empty.
- [Strict JSON rejects user files with extra fields] → intended; error message names the problem so the user can fix the file. Bundled files are under our control.
- [Leftover empty legacy folder after migration] → remove `~/.simschecklist` when it is empty after a successful move.
- [Large custom JSON could block the UI thread during validation] → validation runs on `Dispatchers.IO`/`Default` inside the component coroutine, screen shows progress.

## Migration Plan

1. Ship the split resources + manifest + sync code together; first launch copies bundled files and migrates the desktop DB.
2. Existing users: aircraft appear unchanged (same ids/content); airports DB moved silently; CSV downloads relocate.
3. Rollback (app downgrade): aircraft app files are ignored by the old monolith loader; DB re-downloads as above. No destructive data loss path.