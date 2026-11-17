# Proposal

## Why

The main screen top bar crowds six actions into the `actions` slot: a decorative version badge plus four icon-only tool buttons that are hard to discover (icons carry meaning only via `contentDescription`). Collapsing the tools into a single popup menu frees the bar, makes each tool label-readable, and finally uses the already-present `apps.xml` icon. The version number moves from a clickable-looking badge into plainly readable text.

## What Changes

- Replace the four tool `IconButton`s (METAR, Airports database, QFE helper, Cold temperature corrector) in `AircraftListScreen`'s `TopAppBar` with a single `apps` icon button that opens a dropdown menu.
- Menu items show the same icons (`air`, `cloud_download`, `compress`, `snowflake`) plus text labels, and fire the existing `MainScreenEvent.Select*` events unchanged.
- Add a muted, non-clickable footer line in the menu showing the app version (`BuildKonfig.VER`), separated from the item rows.
- Add a second muted, non-clickable footer line showing the number of airports in the database, prefixed with the `airplanes` icon (which is currently used only by the old `VersionBadge`).
- Remove `VersionBadge` from the top bar; the badge widget itself is dropped.
- Expose the airports count through `MainScreenViewState` (populated in `AircraftList` from `AirportService.getLastUpdate()`; refreshed after database update/drop).

Not changed: tool navigation events, Decompose wiring, the per-aircraft fuel calculator icon on aircraft cards.

## Capabilities

### New Capabilities
- `main-screen-tools-menu`: the main screen top-bar launcher — a single apps-icon button opening a menu with labeled tool entries and non-interactive informational footer lines (app version, airports in database).

### Modified Capabilities
<!-- None: existing specs cover weather data fetching and aircraft data, not main-screen UI. -->

## Impact

- `shared/src/commonMain/kotlin/feature/mainScreen/ui/AircraftListScreen.kt` — top bar `actions` reworked to apps button + `DropdownMenu`.
- `shared/src/commonMain/kotlin/feature/mainScreen/ui/VersionBadge.kt` — deleted.
- `shared/src/commonMain/kotlin/feature/mainScreen/MainScreenViewState.kt` — new `airportsCount` field.
- `shared/src/commonMain/kotlin/feature/mainScreen/AircraftList.kt` — populate/refresh `airportsCount`.
- Resources: uses existing `Res.drawable.apps` and `Res.drawable.airplanes`; no new assets.
- No API, dependency, or database-schema changes. First use of Material3 `DropdownMenu` in the codebase (commonMain-safe).
