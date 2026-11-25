# Spec Delta

## Purpose

Defines how the app introduces features through short, one-time onboarding hints that point at a specific UI element, are shown in a deliberate order, and are remembered across launches.

## ADDED Requirements

### Requirement: Central hint catalog

The system SHALL maintain a single catalog of onboarding hints. Each hint SHALL have a stable identifier, a target screen, a presentation order within that screen, and display text.

#### Scenario: Hints are grouped by screen

- **WHEN** a hint is defined
- **THEN** it declares exactly one target screen and a position in that screen's order

#### Scenario: Stable identity

- **WHEN** the app is updated and a previously defined hint keeps its identifier
- **THEN** the app continues to treat that hint as the same hint for shown-state purposes

#### Scenario: New hint in a later release

- **WHEN** a release adds a new hint to the catalog
- **THEN** that hint is treated as not yet shown, regardless of which other hints were already shown

### Requirement: Unshown hints are presented on screen entry

The system SHALL present the hints belonging to a screen that have not yet been marked as shown when the user first visits that screen. Hints SHALL be presented one at a time in catalog order.

#### Scenario: First visit with pending hints

- **WHEN** the user opens a screen that has one or more hints not yet shown
- **THEN** the first pending hint for that screen is presented automatically

#### Scenario: Advancing through a screen's hints

- **WHEN** the user advances from a presented hint
- **THEN** the next pending hint for the same screen is presented

#### Scenario: No pending hints

- **WHEN** the user opens a screen and every hint for that screen has already been shown
- **THEN** no hint overlay is presented and the screen behaves normally

#### Scenario: Returning to a screen

- **WHEN** the user leaves a screen and later returns while the current hint was completed or skipped
- **THEN** that hint is not presented again

### Requirement: Consistent hint overlay

Every presented hint SHALL use the same overlay presentation: the hint text, the position in the tour, a control to advance, and a control to skip the remaining hints on that screen.

#### Scenario: Intermediate hint

- **WHEN** a hint that is not the last pending hint for the screen is presented
- **THEN** the overlay shows its text, a step indicator, a next control, and a skip control

#### Scenario: Last hint

- **WHEN** the last pending hint for the screen is presented
- **THEN** the overlay indicates completion instead of offering another hint

#### Scenario: Advancing

- **WHEN** the user activates the next control
- **THEN** the current hint is closed and the next pending hint, if any, is presented

#### Scenario: Skipping

- **WHEN** the user activates the skip control
- **THEN** the current hint and every remaining hint for that screen are marked as shown and no further hints are presented during that visit

#### Scenario: Tapping outside the highlighted target

- **WHEN** the user taps the overlay outside the highlighted target
- **THEN** the current hint and every remaining hint for that screen are marked as shown and the overlay closes

### Requirement: Highlighted target interaction

While a hint is presented, the highlighted target SHALL be visually emphasized and SHALL be used to advance the tour. Its normal action SHALL NOT run from that tap.

#### Scenario: Tapping the highlighted target

- **WHEN** a hint is presented and the user taps the highlighted element
- **THEN** the tour advances to the next hint, or closes if it was the last one

#### Scenario: Normal action after the tour

- **WHEN** no hint is presented
- **THEN** tapping the element performs its normal action

### Requirement: Hint state is persisted

The system SHALL persist the identifiers of hints that were completed or skipped. A hint whose identifier is not persisted SHALL be treated as not yet shown.

#### Scenario: Restart preserves progress

- **WHEN** the user completes or skips a hint and later restarts the app
- **THEN** that hint is not presented again

#### Scenario: Unfinished hint after leaving

- **WHEN** the user leaves a screen while a hint is presented without completing or skipping it
- **THEN** that hint remains not yet shown and may be presented on a later visit

#### Scenario: App exits during a hint

- **WHEN** the app is terminated while a hint is presented without the user advancing or skipping
- **THEN** that hint remains not yet shown after the next launch

### Requirement: Unavailable target handling

If the element targeted by a pending hint is not present or not visible, the system SHALL NOT mark that hint as shown and SHALL end the tour for the current visit.

#### Scenario: Target is behind a closed menu or dialog

- **WHEN** a pending hint targets an element that exists only after the user opens a menu or dialog
- **THEN** the hint is not marked as shown and no overlay is left blocking the screen

#### Scenario: Target is off-screen in a lazy list

- **WHEN** a pending hint targets a list item that is currently outside the composed or visible area
- **THEN** the hint remains not yet shown and may be presented on a later visit when the target is available

### Requirement: At most one active hint

The system SHALL present at most one onboarding hint at a time.

#### Scenario: Navigating away from an active hint

- **WHEN** the user navigates to another screen while a hint overlay is open
- **THEN** the previous overlay is dismissed and at most one hint is visible

#### Scenario: Starting a new tour

- **WHEN** a new hint is presented while another hint is still active
- **THEN** the previous hint is dismissed without being marked as shown

### Requirement: Reset shown hints

The system SHALL provide a way to clear the stored shown-hint state so hints can be presented again.

#### Scenario: Reset

- **WHEN** the user or a developer clears the shown-hint state
- **THEN** every hint in the catalog is treated as not yet shown

#### Scenario: Hints after reset

- **WHEN** the shown-hint state is cleared and the user opens a screen with hints
- **THEN** that screen's hints are presented again in catalog order
