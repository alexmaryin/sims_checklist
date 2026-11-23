# Spec Delta

## Purpose

Stores each bundled aircraft as an individual JSON file, deploys those files into writable app storage, and defines the read order with per-file fallback to bundled resources.

## ADDED Requirements

### Requirement: Per-aircraft bundled catalog files

The bundled catalog SHALL consist of one JSON file per aircraft under resources `files/aircraft/` (`cessna172.json`, `cirrus_sr22.json`, `citationx.json`, `king_air_350.json`, `piper_pa24.json`), each containing a single `Aircraft` record. The monolithic `files/aircraft.json` SHALL be removed. All five records SHALL keep their existing ids `0`–`4`, names, performance values, checklists and photo bindings.

#### Scenario: Catalog loads five aircraft

- **WHEN** the bundled catalog is loaded and deserialized
- **THEN** parsing SHALL succeed and produce five `Aircraft` objects identical in content to the former monolithic catalog

#### Scenario: Each file stands alone

- **WHEN** any single per-aircraft file is deserialized on its own
- **THEN** it SHALL produce exactly one valid `Aircraft` record

### Requirement: Deployment copy with version-aware sync

On first start, and whenever the app version differs from the version that last deployed the catalog, the system SHALL copy the bundled aircraft files from resources into the aircraft directory under the app data root, overwriting existing bundled copies. User-added aircraft files SHALL NOT be overwritten or deleted by this sync.

#### Scenario: First-run copy

- **WHEN** the app starts for the first time and the aircraft directory is empty
- **THEN** all five bundled aircraft files SHALL be copied from resources into the aircraft directory

#### Scenario: Upgrade refreshes bundled files

- **WHEN** the app starts after a version upgrade with changed bundled aircraft data
- **THEN** the bundled copies in the aircraft directory SHALL be replaced with the new resource content

#### Scenario: Custom files survive sync

- **WHEN** the deployment sync runs while user-added aircraft files exist in the aircraft directory
- **THEN** the user-added files SHALL remain byte-for-byte unchanged

#### Scenario: Same version skips sync

- **WHEN** the app starts and the recorded deployment version equals the current app version
- **THEN** no bundled file SHALL be re-copied

### Requirement: App files first with per-file resource fallback

The system SHALL read each aircraft from its file in the app data directory. If that file is missing or fails deserialization/validation, the system SHALL fall back to the bundled resource for that aircraft. If neither source is available (a user-added aircraft whose file was removed), that aircraft SHALL be skipped and the user SHALL be notified. A failure of one file SHALL NOT prevent the remaining aircraft from loading.

#### Scenario: Valid app file is used

- **WHEN** an aircraft file exists in the app data directory and is valid
- **THEN** the aircraft SHALL be loaded from that file, not from resources

#### Scenario: Deleted app file falls back

- **WHEN** a bundled aircraft's app file has been deleted
- **THEN** the aircraft SHALL be loaded from its bundled resource and remain fully functional

#### Scenario: Corrupt app file falls back

- **WHEN** a bundled aircraft's app file contains invalid JSON
- **THEN** the aircraft SHALL be loaded from its bundled resource instead

#### Scenario: Missing custom aircraft

- **WHEN** a user-added aircraft's file is missing or invalid and no bundled resource exists for it
- **THEN** that aircraft SHALL be absent from the list and the user SHALL see a notification about the skipped aircraft

#### Scenario: One bad file does not break the catalog

- **WHEN** one aircraft file is corrupt while all others are valid
- **THEN** all other aircraft SHALL load normally