# Tasks

## 1. State plumbing for airports count

- [x] 1.1 Add `airportsCount: Long?` (default `null`) to `MainScreenViewState` and verify with `gradlew :shared:compileKotlinDesktop` (or equivalent compile task) succeeding.
- [x] 1.2 Populate `airportsCount` in `AircraftList`: from `getLastUpdate()?.airports` in the existing `doOnStart` coroutine (or `0`/`null` when the base is empty), from `LocalBaseConverter.UpdateResult.Success.count` after conversion, and `0` in `onDropBase` — verify by running the app on desktop/Android: snack behavior unchanged and state carries the count (observable via the menu footer in 3.2, or a temporary log).

## 2. Tools menu component

- [x] 2.1 Create `feature/mainScreen/ui/MainToolsMenu.kt`: `IconButton` with `Res.drawable.apps` as anchor plus local `remember { mutableStateOf(false) }` open state, and an empty `DropdownMenu` shell — verify the apps icon appears in the top bar and tapping toggles an (empty) menu with outside-tap/back dismissal.
- [x] 2.2 Add the four `DropdownMenuItem`s (icons `air`, `cloud_download`, `compress`, `snowflake`; labels "Weather and airport", "Airports database", "QFE helper", "Cold temperature corrector") firing the existing `MainScreenEvent.Select*` and closing the menu — verify each item navigates to the same screen as the old top-bar button and the menu closes on selection.
- [x] 2.3 Add footer rows inside the menu: airplanes-icon + "N airports in database" muted line, then muted centered `BuildKonfig.VER` line separated by extra top padding; handle `null`/empty as "No airports in database" — verify both lines render, are visually distinct from items, and tapping them does nothing (menu stays open, no navigation).

## 3. Wire-up and cleanup

- [x] 3.1 Replace the `actions` block in `AircraftListScreen` with `MainToolsMenu(component)` and delete `VersionBadge.kt` — verify compile succeeds and the top bar shows title + single apps button only.
- [x] 3.2 Run full verification on both targets: `gradlew :shared:build` (or project's build command) and manual pass through the spec scenarios — launcher opens/closes, all four tools navigate, version line and airports count render, count changes after database update and after "Drop airports database" (debug build), no references to `Res.drawable.airplanes`/`Badge` remain outside the new footer.

## Workflow follow-up

- Archive the change after review (`openspec archive replace-topbar-icons-with-apps-menu`) and verify the `main-screen-tools-menu` spec lands in `openspec/specs/`.
