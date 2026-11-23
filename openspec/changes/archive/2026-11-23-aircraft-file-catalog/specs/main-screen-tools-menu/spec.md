# Spec Delta

## MODIFIED Requirements

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