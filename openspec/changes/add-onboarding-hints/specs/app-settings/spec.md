# Spec Delta

## Purpose

Defines how the app stores small user-level preferences locally so they survive restarts, including the progress of one-time onboarding hints.

## ADDED Requirements

### Requirement: Local settings persistence

The system SHALL persist app settings in a single settings file under the existing app data root. Settings SHALL survive application restarts on every supported platform.

#### Scenario: First launch

- **WHEN** the app starts and no settings file exists
- **THEN** the system uses default settings without creating an error condition

#### Scenario: Restart

- **WHEN** the app writes a setting and is restarted
- **THEN** the written value is available after the restart

### Requirement: Defaults for missing or unreadable settings

If the settings file is missing, empty, unreadable, or not valid for the current settings model, the system SHALL fall back to default settings without crashing and without affecting other app data.

#### Scenario: Missing file

- **WHEN** the settings file does not exist
- **THEN** all settings take their default values

#### Scenario: Corrupt file

- **WHEN** the settings file contains content that cannot be read as settings
- **THEN** the app starts normally with default settings

#### Scenario: Unrelated app data is untouched

- **WHEN** the settings file cannot be read
- **THEN** the aircraft files, airports database, and other app data remain unchanged

### Requirement: Forward-compatible settings reading

The system SHALL ignore settings fields it does not recognize and SHALL continue reading the settings it does recognize.

#### Scenario: Settings written by a newer version

- **WHEN** the settings file contains additional fields introduced by a newer app version
- **THEN** the current version reads its known settings without error

#### Scenario: Older field removed

- **WHEN** the settings file contains a field that the current model no longer uses
- **THEN** the current version ignores that field without error

### Requirement: Shown onboarding hints are stored in settings

The settings SHALL contain the set of onboarding hint identifiers that have been shown. An absent or empty set SHALL mean that no hint has been shown.

#### Scenario: Marking a hint shown

- **WHEN** a hint is completed or skipped
- **THEN** its identifier is added to the persisted shown-hint set

#### Scenario: Marking the same hint twice

- **WHEN** an already shown hint is marked as shown again
- **THEN** the persisted shown-hint set remains unchanged

#### Scenario: Hint not yet shown

- **WHEN** a hint identifier is not present in the persisted shown-hint set
- **THEN** the system treats that hint as not yet shown

### Requirement: Resetting onboarding progress

The system SHALL allow the shown onboarding hints to be cleared without changing other persisted settings.

#### Scenario: Reset shown hints

- **WHEN** onboarding progress is reset
- **THEN** the persisted shown-hint set becomes empty

#### Scenario: Other settings survive reset

- **WHEN** onboarding progress is reset
- **THEN** other persisted settings keep their current values

### Requirement: Durable and atomic updates

Settings updates SHALL be written so that an interrupted or failed write does not corrupt previously stored settings. After a successful update, subsequent reads in the same session SHALL return the updated values.

#### Scenario: Interrupted write

- **WHEN** the app is terminated while a settings update is being written
- **THEN** the next launch reads either the previous valid settings or the fully written new settings, never a partial file

#### Scenario: Failed write

- **WHEN** a settings write fails
- **THEN** the app continues to operate with the last successfully stored settings

#### Scenario: Read after update

- **WHEN** a settings update succeeds
- **THEN** a subsequent read in the same session reflects the new value

#### Scenario: Concurrent updates

- **WHEN** two hint identifiers are marked as shown in quick succession
- **THEN** both identifiers are present after the writes complete
