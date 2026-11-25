# Proposal

## Why

The app now has a growing set of tools and screens, but onboarding is a single hardcoded hint inside the tools menu. It has no persistence, so it shows on every launch, and adding another hint would duplicate Reveal setup and produce inconsistent overlays. A reusable onboarding layer is needed so each feature hint can be added with one catalog entry and one anchor modifier, shown once per install, and remembered in app settings.

## What Changes

- Introduce a single onboarding hint catalog with stable IDs, grouped by screen and ordered for sequential presentation.
- Add one reusable `OnboardingHost` per screen that owns the Reveal state, finds not-yet-shown hints for that screen, reveals them in order, and renders one consistent balloon with step counter, `Next`, and `Skip`.
- Add a `Modifier.onboardingHint(...)` anchor API so feature composables do not import or configure Reveal directly.
- Move the existing tools-menu hint out of `MainToolsMenu` into the screen-level host; the menu keeps only the anchor modifier.
- Persist shown hint IDs in a new app settings file (`settings.json`) under the existing app data root. Not-yet-shown hints are derived as `catalog - shown`, so hints added in future releases appear automatically without migration.
- Expose onboarding through the existing Decompose feature components and Koin DI.
- Provide a reset action for development/testing so already-shown hints can be shown again.
- Handle anchors that are not currently composed (lazy lists, closed menus, hidden tabs): do not mark the hint as shown, stop the tour for that visit, and retry on a later visit.
- Add the first migrated hint for the main screen tools launcher and document the three-step process for adding future hints.

## Capabilities

### New Capabilities

- `onboarding-hints`: One-time, per-screen onboarding hints: hint catalog and ordering, automatic presentation of unshown hints, consistent overlay controls, dismissal semantics, anchor registration, missing-anchor handling, and reset behavior.
- `app-settings`: Local persistent app settings: settings file location and format, default values, atomic updates, forward-compatible reading, shown-hint storage, and reset.

### Modified Capabilities

<!-- No existing spec-level requirements change. The new settings file is still covered by app-data-storage's "all app files under the per-user root" requirement. -->

## Impact

- **New code areas:** `commonUi/onboarding/*` (catalog, host, balloon, anchor modifier), `services/settings/*` (settings model and repository), `di/settingsModule.kt`.
- **Modified code areas:** `AircraftListScreen.kt` (wrap in host), `MainToolsMenu.kt` (replace local Reveal with anchor modifier), `MainEventExecutor`/`AircraftList` (expose onboarding controller), Android and desktop Koin startup, `docs/storage.md`.
- **Dependencies:** none new. Uses the existing Reveal 5.2.3, `kotlinx.serialization`, Koin, and `getAppDataDir()`.
- **Storage:** new `settings.json` under the existing app data root on desktop and `filesDir` on Android.
- **Behavioral impact:** the tools-menu hint stops showing unconditionally and is shown once, then recorded as shown.
- **Out of scope:** a single guided tour that spans multiple screens and drives navigation; a general settings UI. Both can reuse the catalog and settings layer later.
