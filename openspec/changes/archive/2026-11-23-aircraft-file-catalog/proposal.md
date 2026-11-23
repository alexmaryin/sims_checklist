# Proposal

## Why

All five aircraft live in a single monolithic `aircraft.json` compiled into app resources, so users cannot add their own aircraft, and aircraft data cannot evolve independently of app releases. At the same time, app file storage is scattered and non-persistent: the desktop airports database sits in `~/.simschecklist`, downloaded update files land in `java.io.tmpdir` (wiped by OS cleanup), and there is no writable location for user-provided content at all.

## What Changes

- Split the bundled catalog into one JSON file per aircraft under `composeResources/files/aircraft/`; remove the monolithic `aircraft.json`.
- On first run and on app version upgrade, copy bundled aircraft files from resources into the app data directory; read aircraft data from app files first, falling back per-file to bundled resources when an app file is missing or invalid.
- Introduce a single OS-conventional app data root (Windows `%APPDATA%/SimsChecklist`, macOS `~/Library/Application Support/SimsChecklist`, Linux `~/.local/share/SimsChecklist`, Android `filesDir`), following the pattern proven in the FollowMyMus project. **BREAKING** for desktop storage layout: migrate the existing `airports.db` from `~/.simschecklist` into the new root; route all app file IO (including airport update downloads) through the same root.
- Add an `Add custom aircraft` entry to the main screen tools menu opening a dedicated import screen: two separate file pickers (aircraft JSON + JPEG icon), full validation before anything is written, and a mandatory copyright attestation checkbox.
- Validation: strict JSON schema + semantic checks (performance values, checklist ids/categories/captions); image must be JPEG, ≤ 50 KB, ≤ 200 px per side, width/height difference ≤ 10%.
- On successful import the app assigns the aircraft id (next after the current maximum, replacing any user-supplied id), rewrites the `photo` label to the stored image file name, and saves both files into the app data directory; the aircraft list refreshes immediately.
- Custom aircraft icons load from app files via a new platform image-decoding path (bundled aircraft keep their drawable resources).
- Reject imports whose aircraft name duplicates an existing aircraft (case-insensitive); allow removing custom aircraft from their card via a trash-can icon with confirmation (bundled aircraft cannot be removed).

## Capabilities

### New Capabilities

- `app-data-storage`: one OS-conventional per-user app data root for all persistent and transient app files on every platform; migration of the existing desktop airports database into it.
- `aircraft-catalog-storage`: per-aircraft bundled JSON files, deployment copy into the app data directory, version-aware upgrade sync, and per-file fallback to bundled resources.
- `custom-aircraft-import`: tools-menu entry and import screen, two-file selection, syntax/semantic/image validation, copyright attestation, app-assigned ids, photo label rewriting, persistence and list refresh; duplicate-name rejection and confirmed removal of custom aircraft.

### Modified Capabilities

- `king-air-350-data`: the record location requirement changes from `files/aircraft.json` to the per-aircraft file `files/aircraft/king_air_350.json`; catalog-loading scenarios reference the split catalog.
- `piper-pa24-data`: same relocation to `files/aircraft/piper_pa24.json`; catalog-loading scenarios reference the split catalog.
- `citation-x-data`: the checklist-set requirement references the per-aircraft file `files/aircraft/citationx.json` instead of `files/aircraft.json`; the "Catalog stays valid" scenario references the split catalog.
- `main-screen-tools-menu`: the labeled-entries requirement grows from four tool rows to five, adding `Add custom aircraft` which navigates to the import screen.

## Impact

- **Resources**: `shared/src/commonMain/composeResources/files/aircraft.json` removed; five per-aircraft files added under `files/aircraft/`. Bundled drawables unchanged.
- **Shared code**: `AircraftBaseImpl` (multi-file load + fallback), `AircraftRepositoryImpl` (add-aircraft API, refresh), Koin `dbModule`, new storage service (`getAppDataDir` expect/actual or JVM helper + Android actual), new `FileHandler` expect/actual (file pickers), new image-from-file decoder expect/actual, `AircraftListScreen` photo loading, new import screen + Decompose configuration/navigation, tools menu.
- **Desktop**: `di/getRoomBuilder.kt` (DB path + one-time migration from `~/.simschecklist`), `services/airportService/getFilepath.kt` (app data root instead of tmpdir).
- **Android**: DB stays at the platform-standard `getDatabasePath`; aircraft files under `filesDir/aircraft/`; SAF-based pickers need activity binding as in FollowMyMus.
- **Tests**: `CitationXChecklistsTest`, `CatalogCategoriesTest`, `AircraftBaseTestImplTest` adapt to the split catalog; new validation and storage tests.
- **Specs**: `king-air-350-data`, `piper-pa24-data`, `citation-x-data`, `main-screen-tools-menu` deltas; `checklist-categories` unaffected (its requirements are layout-agnostic).