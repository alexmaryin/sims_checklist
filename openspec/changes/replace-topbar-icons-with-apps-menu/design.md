# Design

## Context

See `proposal.md` — Why. Current state: `AircraftListScreen`'s `TopAppBar.actions` holds `VersionBadge` plus four `IconButton`s firing `MainScreenEvent.SelectMetar / SelectAirportsBase / SelectQFEHelper / SelectColdTemperature`. Decompose (`AircraftList`) already queries `AirportService.getLastUpdate()` (returns `LastUpdate(time, airports: Long)`) at lifecycle start for the snack message, so the airports count is available without new service methods. `Res.drawable.apps` exists but is unused; `Res.drawable.airplanes` is used only by `VersionBadge`.

## Goals / Non-Goals

**Goals:**
- One anchored popup menu, keyboard/back-dismissible, identical navigation behavior to today's icon buttons.
- Footer info lines that cannot receive click semantics.
- Keep the change inside the existing Decompose contract (view + view state only).

**Non-Goals:**
- No new `MainScreenEvent` types, no navigation graph changes.
- No redesign of other screens' top bars.
- No i18n migration of the hardcoded UI strings (consistent with current code).

## Decisions

**1. Material3 `DropdownMenu` instead of a raw `Popup`.**
`DropdownMenu` (with `DropdownMenuAnchor` over the `IconButton` / `Box` + `Box` scope, whichever compiles cleanly with the project's Compose Multiplatform version) gives anchoring, elevation, shape, scrim, and back-press dismissal for free — matching `TopAppBar` visuals via `MenuDefaults`. A custom `Popup` was considered for maximum footer layout freedom but reimplements dismissal logic and menu styling for two text rows. `MenuScope` restricts direct `HorizontalDivider` placement, so separation is done with padding/typography, not dividers.

**2. Menu open/close is local Compose state.**
`val expanded = remember { mutableStateOf(false) }` in `AircraftListScreen`. Nothing goes into `MainScreenViewState`: menu visibility is not app state and the pattern matches other transient UI state in this codebase (none exists — this is fine because it dies with the composable).

**3. Tool rows: `DropdownMenuItem` with `text` + `leadingIcon`.**
Leading icons are the existing `air`, `cloud_download`, `compress`, `snowflake`; labels reuse today's `contentDescription` strings ("Weather and airport", "Airports database", "QFE helper", "Cold temperature corrector"). `onClick` closes the menu and forwards the unchanged `MainScreenEvent`.

**4. Airports count travels through `MainScreenViewState.airportsCount: Long?`.**
- `null` means "unknown/not loaded yet"; `0` after drop.
- Populated in `AircraftList`: existing `doOnStart(true)` coroutine already awaits `getLastUpdate()` — copy `it.airports` into state alongside the snack.
- Updated after a database conversion (`LocalBaseConverter.UpdateResult.Success.count`) and set to `0` in `onDropBase` / `null` when base is empty.
- **Refreshed every time the tools menu opens** via `MainScreenEvent.OpenToolsMenu` → re-query `getLastUpdate()`. Needed because the AirportsBase feature has its own converter flow that main-screen state cannot observe, and re-entry does not reliably re-run `doOnStart`. This matches the spec: the count shown the next time the menu opens reflects the actual database.
- Alternative considered: `AirportService.getAirportsCount()` as a new suspend call from a menu-open side effect — rejected as an extra service API, extra async state, and a query on every menu open; the count is already fetched on entry and changes only via update/drop, which this component drives.

**5. Footer rows are plain `Column`/`Row` content inside the `DropdownMenu` composable, below the items.**
- Row 1: `Icon(Res.drawable.airplanes)` + muted `Text("N airports in database")`.
- Row 2: muted `Text(BuildKonfig.VER)`, separated by extra top padding.
- All footer content is start-aligned (padding 12.dp to line up with `DropdownMenuItem` content padding), so items and info share one left edge.
- Muted styling: `MaterialTheme.colorScheme.onSurfaceVariant`, `bodySmall` typography, not wrapped in any clickable modifier — satisfying "unclickable" by construction.
- When `airportsCount == null` the row shows "No airports in database" (empty/unknown is deterministic, per spec scenario).

**6. `VersionBadge.kt` deleted; `airplanes.xml` retained** as the footer icon. `Badge`/`BadgedBox` imports disappear with the file — no other usages exist.

**7. New small composable file `MainToolsMenu.kt` in `feature.mainScreen.ui`** owning the anchor button + menu + footer, so `AircraftListScreen` keeps its current shape (top bar calls one composable) and the screen file doesn't grow 80 lines.

## Risks / Trade-offs

- [Menu covers content on small screens / long labels] → four short labels + two footer lines fit comfortably; menu width can be set via `Modifier.width` if needed.
- [`MenuScope` API drift between M3 versions on Compose Multiplatform] → check the project's Compose version; `DropdownMenuItem` in `RowColumnScope` is stable, and the footer uses raw composables allowed as menu content.
- [Stale count if database changes without this component knowing] → all mutation paths (update/drop) run inside `AircraftList`, which updates the state; no external writers exist.
- [Desktop focus/back behavior of `DropdownMenu`] → M3 handles Esc/back dismissal; verify manually on the desktop target during implementation (project ships Android + desktop).

## Migration Plan

Pure UI change: no data migration. Rollback = restore previous `AircraftListScreen` top bar and `VersionBadge`.
