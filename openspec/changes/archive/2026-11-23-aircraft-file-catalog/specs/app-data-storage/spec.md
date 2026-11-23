# Spec Delta

## Purpose

Defines a single OS-conventional per-user storage root for all app files on every platform, and the migration of existing desktop data into that root.

## ADDED Requirements

### Requirement: OS-conventional app data root

The system SHALL store all app files under one per-user root that follows OS conventions: `%APPDATA%/SimsChecklist` on Windows, `~/Library/Application Support/SimsChecklist` on macOS, `~/.local/share/SimsChecklist` on Linux, and the app's internal files directory on Android. The root SHALL be created automatically if it does not exist.

#### Scenario: Windows root

- **WHEN** the app runs on Windows and writes any app file
- **THEN** the file SHALL be located under `%APPDATA%/SimsChecklist`

#### Scenario: macOS root

- **WHEN** the app runs on macOS and writes any app file
- **THEN** the file SHALL be located under `~/Library/Application Support/SimsChecklist`

#### Scenario: Linux root

- **WHEN** the app runs on Linux and writes any app file
- **THEN** the file SHALL be located under `~/.local/share/SimsChecklist`

#### Scenario: Android root

- **WHEN** the app runs on Android and writes aircraft files
- **THEN** the files SHALL be located in the app's internal files directory, inaccessible to other apps

#### Scenario: Root created on demand

- **WHEN** the app starts and the root directory does not exist
- **THEN** the directory SHALL be created before any file is written, without user interaction

### Requirement: Desktop airports database relocation

On desktop, the airports database SHALL reside in the app data root. On first start after upgrade, if a legacy database exists at `~/.simschecklist/airports.db` and no database exists in the new root, the system SHALL migrate it to the new root without data loss and remove the legacy file after successful migration.

#### Scenario: Upgrade with legacy database

- **WHEN** the app starts with a legacy database at `~/.simschecklist/airports.db` and no database in the new root
- **THEN** the database SHALL be moved into the app data root with all airports, runways, frequencies and metadata intact
- **AND** the legacy file SHALL no longer exist

#### Scenario: Fresh install

- **WHEN** the app starts for the first time with no legacy database
- **THEN** a new empty database SHALL be created in the app data root and no migration SHALL occur

#### Scenario: Database already in new root

- **WHEN** the app starts and a database already exists in the new root
- **THEN** that database SHALL be used and any legacy file SHALL be left untouched

#### Scenario: Android database location unchanged

- **WHEN** the app runs on Android
- **THEN** the airports database SHALL remain at the platform-standard database path and SHALL NOT be migrated

### Requirement: Transient update files under the app data root

Airport update downloads SHALL be written under the app data root instead of the OS temporary directory, and SHALL still be removed after a successful database conversion.

#### Scenario: Download location

- **WHEN** the user updates the airports database
- **THEN** the downloaded CSV files SHALL be written under the app data root

#### Scenario: Cleanup after conversion

- **WHEN** the downloaded files have been converted into the database
- **THEN** the downloaded files SHALL be deleted from the app data root