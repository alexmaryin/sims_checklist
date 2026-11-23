# Tasks

## 1. Storage root and database migration (app-data-storage)

- [x] 1.1 Add `expect fun getAppDataDir(): File` with desktop actual (Windows `%APPDATA%/SimsChecklist`, macOS `~/Library/Application Support/SimsChecklist`, Linux `~/.local/share/SimsChecklist`, ported from FollowMyMus `core/system/getAppDir.kt`) and Android actual (`filesDir` via `AppAndroid.instance()`); create the dir on demand. Verify with a JVM unit test asserting the OS-specific path and directory creation.
- [x] 1.2 Rewire `services/airportService/getFilepath.kt` desktop actual to resolve under `getAppDataDir()` instead of `java.io.tmpdir`; Android actual unchanged in behavior. Verify with a unit test that `getFilePath("x.csv")` points inside the app data root, and that airport update download/cleanup still works.
- [x] 1.3 Implement desktop DB migration in `di/getRoomBuilder.kt`: use `<root>/airports.db` when present; else move legacy `~/.simschecklist/airports.db` (move, or copy+verify+delete fallback), remove the legacy folder when empty; on failure keep legacy and create a fresh DB. Verify with JVM tests covering fresh install, legacy migration, both-present, and failed-move cases.
- [x] 1.4 Document the storage layout (per-OS roots, aircraft directory, migration note incl. downgrade behavior) in `docs/` or README. Verify the documented paths match the implementation.

## 2. Catalog split and deploy sync (aircraft-catalog-storage)

- [x] 2.1 Split `composeResources/files/aircraft.json` into `files/aircraft/{cessna172,cirrus_sr22,citationx,king_air_350,piper_pa24}.json` (single record each, content unchanged) plus `files/aircraft/catalog.json` manifest; add a test asserting the reassembled list equals the original catalog, then delete the monolith. Verify the equivalence test passes before deletion.
- [x] 2.2 Implement deploy sync: on start, compare app version (BuildKonfig) with `<root>/aircraft/.deployed-version`; when different or missing, copy manifest + bundled files from resources into `<root>/aircraft/`, overwriting bundled copies only, then write the marker. Verify with tests for first-run copy, upgrade overwrite, same-version skip, and `custom_*` files left untouched.
- [x] 2.3 Rework `AircraftBaseImpl` to load per-file: manifest entries from app files with strict decode + validation, per-file fallback to bundled resources on missing/invalid; then `custom_*` files with no fallback; collect skip warnings for the UI. Adapt `CitationXChecklistsTest`, `CatalogCategoriesTest`, `AircraftBaseTestImplTest` to the split catalog. Verify all adapted tests pass plus new tests for deleted-file fallback, corrupt-file fallback, missing-custom skip, and one-bad-file isolation.

## 3. Validation (shared by load and import)

- [x] 3.1 Implement `AircraftValidator` in common code: strict `Json { ignoreUnknownKeys = false }` decode of one `Aircraft` plus semantic rules (performance > 0, unique checklist ids, non-empty item captions/actions, category enum), returning human-readable errors. Verify with unit tests for malformed JSON, missing fields, each semantic violation, and a valid record.
- [x] 3.2 Implement image validation: bytes ≤ 50 KB, JPEG magic `FF D8 FF`, decoded dimensions ≤ 200 px per side, `abs(w-h) ≤ 0.10 * max(w,h)`; add `expect/actual` image decoder (Android `BitmapFactory`, desktop Skiko `Image.makeFromEncoded`). Verify with unit tests using fixture images: PNG rejected, 60 KB rejected, 250 px rejected, 150x180 rejected, 150x150 accepted, 152x150 accepted.

## 4. Photo loading from app files

- [x] 4.1 Replace `loadAircraftJpgPhoto` with a `produceState` loader keyed on `photo`: if `<root>/aircraft/<photo>.jpg` exists, decode via the 3.2 decoder; else resolve the drawable resource as today. Verify bundled cards render unchanged and a fixture file-based photo renders in a desktop run.

## 5. Custom aircraft import (custom-aircraft-import)

- [x] 5.1 Port FollowMyMus `expect class FileHandler { suspend fun openFile(mimeType): Pair<String, ByteArray>? }`: desktop `FileDialog(LOAD)` with `.json` / `.jpg,.jpeg` filters; Android SAF `OpenDocument` via `activityResultRegistry` + `suspendCancellableCoroutine` with activity binding at startup. Verify desktop picker returns bytes for both file types and Android picker works on a device/emulator.
- [x] 5.2 Add `Configuration.AddCustomAircraft`, its Decompose component and navigation wiring; state holds json/image bytes + validation errors + attestation flag, save enabled only when both valid and attested. Verify with component/state unit tests for initial disabled state, per-file error surfacing, and enablement logic.
- [x] 5.3 Build the import screen UI: two file pickers with per-file validation feedback, copyright attestation checkbox ("I own this image or obtained it from a free source"), save action, back navigation. Verify visually on desktop that all controls behave per the state tests.
- [x] 5.4 Add the fifth tools-menu row `Add custom aircraft` with a dedicated icon, navigating to the import screen. Verify the menu shows five labeled rows and the row opens the screen (updates `main-screen-tools-menu` behavior).
- [x] 5.5 Implement save: id := max(loaded ids) + 1 replacing the user id, `photo` := `custom_<id>`, write `custom_<id>.json` + `custom_<id>.jpg` into `<root>/aircraft/`, add `AircraftRepository.addAircraft` updating the in-memory list and main screen state, navigate back. Verify with tests for id assignment (user id 99 → 5), photo rewrite, both files persisted, and list refresh without restart; fuel calculator and checklist toggles work for the imported aircraft.

## 6. Integration verification

- [x] 6.1 Run full build and all tests for desktop and Android (`gradlew :shared:allTests :desktop:build` or project equivalents); verify green.
- [x] 6.2 Manual end-to-end pass on desktop: fresh install (files copied, DB created), version-marker upgrade sync, bundled app-file deletion/corruption fallback, custom import happy path + each rejection class, restart persistence of the imported aircraft; verify against the scenarios in the three new specs.
- [x] 6.3 Run `openspec validate aircraft-file-catalog --strict` and fix any findings.

## 7. Duplicate rejection and custom aircraft removal

- [x] 7.1 Add duplicate-name rejection to `AddCustomAircraft.onJsonPicked`: compare the validated name (trimmed, case-insensitive) against `repository.getAll()` and surface a duplicate-name error through the existing JSON error channel. Verify with unit tests: duplicate of bundled name rejected, second import of the same file rejected, distinct name accepted.
- [x] 7.2 Add the user-provided trash-can drawable (Google icons XML) to `composeResources/drawable/`; verify it resolves via `Res.drawable`. Blocked until the user supplies the icon XML.
- [x] 7.3 Add removal flow: `MainScreenEvent.RemoveAircraftConfirm(id)` / `RemoveAircraft(id)` + confirmation snackbar in `AircraftList`; trash-can `IconButton` next to the fuel calculator icon only when `photo` starts with `custom_`; handler deletes `custom_<id>.json`/`.jpg`, rewrites `.custom-index`, calls new `AircraftRepository.removeAircraft(id)`, refreshes list state. Verify with unit tests for file deletion, index rewrite, repository removal, and no-warning-after-restart; visually confirm bundled cards show no trash icon.
- [x] 7.4 Re-run `:shared:allTests`, `:desktop:build`, `:android:assembleDebug` and `openspec validate aircraft-file-catalog`; verify green.

## Workflow follow-up

- Android verification complete: SAF file picker, custom aircraft import end-to-end, and file-based icon rendering verified by the user on Android. Desktop end-to-end also verified by the user.
- The single `--strict` warning (Citation X requirement text >500 chars) is inherited from the existing main spec; left intact per the MODIFIED workflow. Split it in a separate change only if requested.
- Archive the change after the project's review requirements are satisfied.
- Verify the archived result.