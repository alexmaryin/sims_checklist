# Design

## Context

See `proposal.md` for motivation and `specs/onboarding-hints/spec.md` / `specs/app-settings/spec.md` for the behavior contract.

Constraints from the current codebase:

- Reveal 5.2.3 has no `RevealCanvas`. Each `Reveal` renders its own fullscreen `Popup`; several can exist, but only one should be active. `revealState.reveal()` is `suspend` and requires the target to have been registered by layout; `tryReveal()` is the non-throwing variant.
- Reveal keys may be any type but enums are the recommended stable key.
- Decompose composes only the active child of the stack. Inactive screens are disposed.
- Features follow the existing pattern: an interface plus `MutableValue` state, a Koin-injected implementation, and a composable screen.
- There is no settings library. `getAppDataDir()` already provides the per-user root, `commonMain` is JVM-only, and `kotlinx.serialization` and Koin are available.
- The existing tools-menu hint owns a local `Reveal`, always fires on composition, and has no persistence.

## Goals / Non-Goals

**Goals:**

- Adding a hint requires one catalog entry and one anchor modifier.
- Hints are shown once per install, in a deterministic per-screen order.
- One shared overlay implementation and one shared Reveal setup per screen.
- Persistence survives restarts and supports resetting progress.
- Missing targets never cause the tour to get stuck or be marked as shown.
- No new dependencies.

**Non-Goals:**

- A cross-screen guided tour that drives navigation.
- A full settings screen or user-facing onboarding management UI.
- Localization of hint text beyond the app's current hardcoded-string style.
- Analytics, A/B testing, or per-user hint targeting.
- Per-hint custom overlay implementations beyond placement and styling parameters.

## Decisions

### 1. Per-screen host rather than a root-level host

`OnboardingHost` wraps the root content of each screen and owns one `RevealState`. It provides that state through `LocalOnboardingRevealState`, and anchors use `Modifier.onboardingHint(...)`.

Rationale:

- Decompose disposes inactive screens, so a root host would require all anchors across screens to remain registered in one `RevealState`.
- Reveal's popup is fullscreen, so the host does not need to wrap the whole window to render correctly.
- A per-screen host naturally scopes one screen's hint sequence.

Alternatives rejected:

- Root-level host: simpler for a global tour, but requires navigation-aware tour state and keeps every screen's anchors alive.
- One `Reveal` per anchor (the current pattern): duplicates the overlay and prevents sequencing across elements on the same screen.

### 2. Single enum catalog with stable IDs

`OnboardingHint` is an enum with `screen`, `order`, optional `title`, text, and optional placement. The enum constant name is the persisted identifier. `OnboardingScreen` groups hints by screen.

Rationale:

- Enum names are compile-time unique and stable across serialization.
- `entries` gives the catalog without extra registration.
- Renaming a constant deliberately resets that hint, which is a useful way to re-show a redesigned hint.
- New enum entries are automatically treated as not shown.

Alternatives rejected:

- String IDs: no compile-time uniqueness, easy to typo.
- Sealed class instances: more flexible but no free stable name and more boilerplate.
- Database-backed hint definitions: unnecessary for content that changes with app releases.

One hint maps to one anchor per screen. Duplicate keys in the same `RevealState` overwrite, so the catalog must not be used on two anchors at once.

### 3. Persist the shown set, not the not-shown set

`settings.json` stores `shownOnboardingHints: Set<String>`. Unshown hints are derived as `catalogFor(screen) - shown`.

Rationale:

- A hint introduced in a later release has no entry in the shown set, so it is shown without a migration.
- Reset is a single empty set.
- The persisted data stays small and independent of catalog size.

Alternatives rejected:

- Persisting a list of unseen hints: every new hint requires a migration, and deleted hints leave stale entries.
- Per-hint version numbers: more expressive, but not needed while renaming a constant can express re-show.

### 4. JSON settings file in the existing app data root

`AppSettingsRepository` loads and saves `<appDataDir>/settings.json`:

```json
{
  "shownOnboardingHints": ["MainMenu"]
}
```

Data model:

```kotlin
@Serializable
data class AppSettings(
    val shownOnboardingHints: Set<String> = emptySet(),
)
```

Rationale:

- No new dependency; the project already has `kotlinx.serialization` and file storage.
- `commonMain` targets are JVM-only, so `java.io`/`java.nio` work without expect/actual.
- Writes happen only when a hint is completed or skipped, so synchronous writes of a tiny file are acceptable.

Alternatives rejected:

- `multiplatform-settings`: less code, but a new dependency and another platform abstraction for a file that is already easy to manage.
- DataStore: reactive and asynchronous, but adds a dependency and driver setup for a few bytes of state.
- Room: already present, but a schema, migration, and DAO are disproportionate for one set of strings.

Write protocol:

1. Apply the update under a lock.
2. Encode the complete new settings object.
3. Write to `settings.json.tmp`.
4. Atomically move the temp file over `settings.json`, with a direct-write fallback if atomic move is unavailable.
5. Publish the new cache only after the write succeeds.

This makes concurrent updates serialize, prevents partial files, and keeps reads consistent with the last successful write.

Unknown fields are ignored on read. They are not preserved on rewrite; a downgrade can therefore drop settings introduced by a newer version. That is acceptable for the current single-field model and is recorded as a trade-off.

### 5. Settings repository plus onboarding controller

- `AppSettingsRepository` owns the file, cache, atomic writes, and the shown-hint set.
- `OnboardingController` is the thin feature-facing API: `isShown(hint)`, `markShown(hint)`, `reset()`.

The controller is registered in Koin and exposed by each feature component that hosts hints, for example as `val onboarding: OnboardingController`. UI code receives it through the component, not through a global service locator.

Rationale:

- Keeps file I/O out of composables.
- Keeps the MVI boundary reasonable: onboarding is presentation-only state, not business state in the feature view model.
- Makes repository and controller unit-testable with a temporary settings file.

Alternative rejected: putting onboarding state into every feature's `ViewState` with UI events. That is the strict MVI option, but it adds boilerplate to every feature and duplicates the same state machine. If strict MVI is required later, the controller can be wrapped in a small `OnboardingComponent` without changing storage or the catalog.

### 6. Host state machine

```
screen enters
    |
    v
pending = hintsFor(screen) - shown, sorted by order
    |
    v
wait ~350 ms for layout/enter animation
    |
    v
current = pending.removeFirstOrNull()
    |
    v
tryReveal(current) with retry (about 20 x 100 ms)
    |                                   |
    success                             target never registered
    |                                   |
    v                                   v
show overlay                     current = null, stop tour,
    |                            leave hint unshown
    +-- Next ---------> mark shown, current = pending.next()
    +-- Skip ---------> mark current + pending shown, hide
    +-- click target --> same as Next
    +-- tap outside ---> mark current + pending shown, hide
```

The pending/current/advance logic is extracted into a small plain state holder so sequencing, skip, completion, and missing-target outcomes can be unit-tested without Compose. `OnboardingHost` owns the holder, the `RevealState`, and the reveal retry loop.

Key points:

- A hint is marked shown only on advance or skip, never on reveal. If the app dies or the user leaves mid-hint, it is shown again later.
- Skip and outside-tap mark the remaining hints for that screen shown, so the user is not interrupted again on the same screen.
- If the target is unavailable, the tour stops without marking anything shown. The hint is retried on a later visit.
- A short retry window handles layout settling and targets that register slightly late. It does not wait indefinitely for off-screen lazy items.
- Reveal consumes clicks on the highlighted target, so tapping it advances the tour instead of running the target's normal action.
- Navigating away disposes the host and hides the overlay; the unfinished hint stays unshown.

### 7. UI composition

```kotlin
@Composable
fun AircraftListScreen(component: MainEventExecutor) {
    OnboardingHost(component.onboarding, OnboardingScreen.Main) {
        Scaffold(/* ... */) { /* ... */ }
    }
}
```

Anchors:

```kotlin
IconButton(
    modifier = Modifier.onboardingHint(OnboardingHint.MainMenu),
    onClick = { /* normal action */ },
)
```

`OnboardingHost` renders one shared balloon with:

- optional title,
- hint text,
- `step / total`,
- `Skip`,
- `Next` or a completion label on the last hint.

Per-hint placement is a catalog property mapped to `RevealOverlayArrangement` and the matching `Arrow` direction. Defaults are bottom placement with a top arrow. This keeps the overlay consistent while allowing the balloon to stay on-screen near top bars, bottom bars, and corners.

### 8. DI and lifecycle

`settingsModule` registers the repository and controller as singles. It is added to both `AppAndroid` and desktop `main.kt`.

On Android, `getAppDataDir()` depends on `AppAndroid.instance`, which is initialized before `startKoin`, so eager or lazy creation is safe.

The feature component injects the controller. The host is a composable concern; the controller has no Decompose lifecycle, which is correct because settings progress is app-wide rather than screen-scoped.

### 9. Reset exposure

A debug-only action (guarded by `BuildKonfig.DEBUG`, next to the existing debug-only database action) calls `controller.reset()`. The reset takes effect on the next visit to a screen, because the host computes its pending list on entry. No user-facing settings screen is added in this change.

## Risks / Trade-offs

- **Synchronous file write on the UI call path** → The file is tiny and writes happen once per completed hint. If this becomes noticeable, move the write behind an IO dispatcher without changing the controller API.
- **Unknown settings fields are dropped on rewrite** → Acceptable for one field; document it. A future settings model can retain a raw JSON object if downgrade compatibility matters.
- **Lazy-list or conditionally composed anchors are not found** → The hint stays unshown and retries on a later visit. Product guidance: prefer always-visible anchors; do not anchor inside a menu that is closed by default.
- **Duplicate enum keys on two anchors** → Reveal keeps only the last registration. One hint must have exactly one anchor per screen.
- **Click on the highlighted target is consumed** → This is intended for advancing the tour. If a specific hint must allow the real action, expose a passthrough option in its catalog metadata.
- **Android back is not intercepted** → Back navigates away and the hint remains unshown. If back should skip the tour, add an explicit back handler later.
- **Two hosts active during navigation animation** → Only one overlay is visible, but the hidden host may still hold state. Avoid starting nested hosts; one host per screen root.
- **Reset while a host is active** → The host keeps its pending list until re-entry. This is acceptable for a debug-only action; a reactive controller can be added later.
- **First launch after upgrade** → The migrated tools-menu hint has no persisted entry, so existing users see it once. This is expected and communicated in the migration plan.
- **Timing constants** → The 350 ms initial delay and retry window may need tuning after visual review on Android and desktop.

## Migration Plan

1. Add the settings model, repository, controller, and Koin module.
2. Add the hint catalog and the shared host/balloon/anchor API.
3. Wrap the main screen in `OnboardingHost` and replace the local Reveal in `MainToolsMenu` with `Modifier.onboardingHint(OnboardingHint.MainMenu)`.
4. Remove the old `commonUi/reveals/Keys.kt` once nothing references it.
5. Update `docs/storage.md` with `settings.json`.
6. Verify on Android and desktop: first-run hint, next/skip, restart, leaving mid-hint, and reset.
7. Rollback: remove the host wrappers and catalog; the old app ignores `settings.json` and the old unconditional hint returns. No other app data is affected.

No database migration is required.

## Open Questions

- The exact overlay delay and maximum retry window should be confirmed during visual review; they do not change the architecture or the specs.
