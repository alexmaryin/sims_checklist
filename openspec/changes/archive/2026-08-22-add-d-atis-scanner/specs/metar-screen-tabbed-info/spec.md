# metar-screen-tabbed-info

## Purpose

Present METAR, TAF, and D-ATIS raw texts as a tabbed card on the Metar screen, with type-aware rendering for D-ATIS and parallel fetch integration.

## ADDED Requirements

### Requirement: Tabbed info card replaces stacked MetarInfo

The system SHALL replace the stacked `MetarInfo` (two `Text` blocks) on `MetarScreen` with a Material3 `PrimaryTabRow` tabbed card containing three tabs labeled `METAR`, `TAF`, `D-ATIS` in that order. The card is visible when any of the following conditions are true:
- `state.data.rawMetar.isNotBlank()`
- `state.data.rawTaf.isNotBlank()`
- `state.datis != null` (D-ATIS data present, even if empty list)
- `state.isLoadingAtis == true` (D-ATIS is loading)

Otherwise hidden. This extends the existing `AnimatedVisibility` policy to include D-ATIS states.

#### Scenario: Tabs visible after fetch

- **WHEN** ICAO `KJFK` is submitted and METAR arrives
- **THEN** the tabbed card SHALL become visible with `METAR` as the initially selected tab (index 0)

#### Scenario: Tab resets to METAR on new ICAO submission

- **WHEN** the user has selected `D-ATIS` tab (index 2) and submits a new ICAO
- **THEN** the selected tab SHALL reset to `METAR` (index 0) to avoid showing D-ATIS data from the previous station

#### Scenario: Switching tabs preserves state

- **WHEN** the user taps `TAF` then `D-ATIS`
- **THEN** the selected tab index SHALL update and persist in `MetarScreenViewState.selectedInfoTab` (serializable, survives process recreation via `saveableMutableValue`)

#### Scenario: Selectable text and scrolling

- **WHEN** any tab's text overflows the viewport or is long (e.g., ATIS 600+ chars)
- **THEN** the tab content SHALL be vertically scrollable and selectable/copyable, using `14sp` secondary color typography consistent with current `MetarInfo` style

### Requirement: Parallel D-ATIS fetch on ICAO submission

The system SHALL fetch D-ATIS in parallel with METAR, TAF, and Airport when `MetarUiEvent.SubmitICAO` is handled, using a 4th coroutine job `atisJob` in the same `SupervisorJob` scope. Aggregate `isLoading` (used for `IcaoInput` spinner) SHALL be the OR of all loading flags (`loadMetar || loadTaf || loadAirport || loadAtis`).

#### Scenario: Parallel launch

- **WHEN** `submitICAO("KLAX")` is invoked
- **THEN** the system SHALL set `loadAtis=true` and launch `atisJob = scope.launch { fetchAtis("KLAX") }` concurrently with the existing three jobs, without awaiting each other

#### Scenario: Cancellation on new ICAO or manual wind edit

- **WHEN** a new `SubmitICAO` is issued or `SubmitWindAngle`/`SubmitWindSpeed` is handled
- **THEN** any in-flight `atisJob` SHALL be cancelled (same as `metarJob/tafJob/airportJob` on wind edits)

#### Scenario: State update on success

- **WHEN** `AtisService.getDatis` returns `Success(list)` with entries
- **THEN** the system SHALL set `MetarScreenViewState.datis` to the mapped UI list, set `loadAtis=false`, recompute `isLoading`, and leave global `error` unchanged

#### Scenario: Empty result is not a global error

- **WHEN** `AtisService.getDatis` returns `Error(EMPTY_RESULT)` (non-US station like UWLW)
- **THEN** the system SHALL set `datis=null`, set `loadAtis=false`, recompute `isLoading`, and SHALL NOT set `MetarScreenViewState.error` nor show a snackbar

### Requirement: D-ATIS type-aware visual distinction

The system SHALL visually distinguish D-ATIS entries by `type` using both an icon and a color badge.

#### Scenario: Icons per type

- **WHEN** rendering a `AtisType.COMBINED` entry
- **THEN** the header SHALL show the `COMBINED` icon (e.g., merge/sync); for `AtisType.ARRIVAL` show arrival/landing icon; for `AtisType.DEPARTURE` show departure/takeoff icon — each from compose resources, tinted by the badge's content color

#### Scenario: Colors per type

- **WHEN** rendering a `AtisType.COMBINED` / `AtisType.ARRIVAL` / `AtisType.DEPARTURE` entry
- **THEN** the badge/background SHALL use distinct `MaterialTheme.colorScheme` containers: `COMBINED → secondaryContainer/onSecondaryContainer`, `ARRIVAL → primaryContainer/onPrimaryContainer`, `DEPARTURE → tertiaryContainer/onTertiaryContainer` (or theme-equivalent distinct containers), providing redundant encoding with the icon

#### Scenario: Multiple entries

- **WHEN** D-ATIS returns two entries (e.g., KATL `AtisType.ARRIVAL`+`AtisType.DEPARTURE`)
- **THEN** the D-ATIS tab content SHALL list both entries vertically, each with its own header `Row(Icon, Code, Type badge, Time)` and body `Text(datis)`, separated by a divider or spacing

### Requirement: D-ATIS tab empty, loading, and error states

The system SHALL render inline states inside the D-ATIS tab content, not as global errors.

#### Scenario: Loading

- **WHEN** `loadAtis` is true
- **THEN** the D-ATIS tab content SHALL show a centered `CircularProgressIndicator` (tab content only; `IcaoInput` spinner already reflects aggregate `isLoading`)

#### Scenario: Empty (no D-ATIS)

- **WHEN** loading is false and `datis` is null or empty after a fetch
- **THEN** the D-ATIS tab content SHALL show the text `No D-ATIS available for this station (US only)` styled `14sp` secondary, italic

#### Scenario: Network failure

- **WHEN** `AtisService.getDatis` returns `Error(NO_CONNECTION)` or `OTHER_*` / `UNKNOWN`
- **THEN** the D-ATIS tab content SHALL show `Failed to load D-ATIS: <message>` with an inline retry action that re-dispatches `SubmitICAO` for the last ICAO

### Requirement: Backward compatibility for non-US stations

The system SHALL preserve the existing UX for non-US stations: the `D-ATIS` tab is still present but shows the empty state; the `METAR` and `TAF` tabs continue to work as before; no snackbar or error banner appears solely due to missing D-ATIS.

#### Scenario: UWLW (Russia) flow

- **WHEN** ICAO `UWLW` is submitted (as in the Android screenshot reference)
- **THEN** METAR/TAF tabs show fetched data, the D-ATIS tab shows `No D-ATIS available for this station (US only)`, and no global `error` is set due to D-ATIS

### Requirement: Accessibility for tabbed info

The system SHALL provide accessible labels and interactions for the tabbed info card.

#### Scenario: Icon content descriptions

- **WHEN** rendering a D-ATIS entry with type icon
- **THEN** the icon SHALL have `contentDescription` set to "Combined ATIS", "Arrival ATIS", or "Departure ATIS" matching the type

#### Scenario: Tab switching announcements

- **WHEN** the user switches tabs
- **THEN** the screen reader SHALL announce the tab change (standard Material3 `PrimaryTabRow` semantics)

#### Scenario: Touch target sizes

- **WHEN** rendering tabs
- **THEN** each tab SHALL have a touch target ≥48dp (Material3 default)
