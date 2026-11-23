# main-screen-tools-menu Specification

## Purpose
Defines the main screen top-bar tools launcher: a single entry point that opens a menu of aviation tools, and the informational footer lines (app version, airports in database) shown inside that menu.

## Requirements

### Requirement: Single launcher button on the main screen top bar
The main screen top bar SHALL show exactly one tool-related action: an apps-grid icon button that opens the tools menu. Individual tool buttons and the version badge SHALL NOT appear in the top bar.

#### Scenario: Launcher visible
- **WHEN** the main screen is displayed
- **THEN** the top bar shows the apps icon button and no other tool icons or version badge

#### Scenario: Opening the menu
- **WHEN** the user taps the apps icon button
- **THEN** the tools menu opens anchored to that button

#### Scenario: Dismissing without action
- **WHEN** the menu is open and the user taps outside it or presses back
- **THEN** the menu closes and the screen does not navigate

### Requirement: Labeled tool entries in the tools menu
The tools menu SHALL list five entries — the four global tools (Weather and airport, Airports database, QFE helper, Cold temperature corrector) plus `Add custom aircraft` — each as a row with its dedicated icon and a visible text label.

#### Scenario: Selecting a tool
- **WHEN** the user taps a tool row in the menu
- **THEN** the menu closes and the app navigates to the corresponding tool screen, as it did from the former top-bar icon button

#### Scenario: All tools present
- **WHEN** the tools menu is opened
- **THEN** all five rows are visible with both icon and label

#### Scenario: Selecting Add custom aircraft
- **WHEN** the user taps the `Add custom aircraft` row
- **THEN** the menu closes and the app navigates to the custom aircraft import screen

### Requirement: Version footer line in the tools menu
The tools menu SHALL show the application version as the bottom-most line, visually separated from the tool rows, rendered as non-interactive information only.

#### Scenario: Version displayed
- **WHEN** the tools menu is opened
- **THEN** the current application version is shown on the last line in a muted style below the tool rows

#### Scenario: Version line is not clickable
- **WHEN** the user taps the version line
- **THEN** nothing happens: no navigation, no state change, and the menu stays open until dismissed normally

### Requirement: Airports count footer line in the tools menu
The tools menu SHALL show, above the version line, a non-interactive footer line with the number of airports stored in the local database, prefixed with the airplanes icon.

#### Scenario: Count displayed for a populated database
- **WHEN** the tools menu is opened and the local airports database contains N airports
- **THEN** the footer shows the airplanes icon and a line stating that N airports are in the database

#### Scenario: Empty database
- **WHEN** the tools menu is opened while the local airports database is empty
- **THEN** the footer shows the airports count line as zero (or otherwise indicates no database), and no error or crash occurs

#### Scenario: Count refreshed after update or drop
- **WHEN** the airports database is fully updated or dropped from the main screen
- **THEN** the count shown the next time the menu is opened reflects the new database state
