# Tasks

## 1. Settings persistence and onboarding state

- [ ] 1.1 Add `AppSettings` with `shownOnboardingHints` and `AppSettingsRepository` using `<appDataDir>/settings.json`, including defaults for missing/corrupt data, cached reads, synchronized updates, temp-file plus atomic replace, and reset. Verify `AppSettingsRepositoryTest` covers first run, restart persistence, corrupt/empty file fallback, unknown fields, concurrent marks, reset, and a failed write leaving the previous settings readable.
- [ ] 1.2 Add `OnboardingController` with `isShown`, `markShown`, and `reset`. Verify unit tests cover a hint becoming shown, duplicate marking being a no-op, and reset clearing only onboarding progress.
- [ ] 1.3 Add `di/settingsModule.kt` and register it in both `AppAndroid` and desktop `main.kt`. Verify the desktop app starts without Koin resolution errors and the shared module compiles.
- [ ] 1.4 Update `docs/storage.md` with `settings.json`, its purpose, and its shape. Verify the documented path and JSON example match the implemented model.

## 2. Hint catalog and reusable host

- [ ] 2.1 Add `OnboardingScreen` and the `OnboardingHint` enum with the existing `MainMenu` hint, including screen, order, text, optional title, and placement. Verify a unit test asserts unique enum IDs, non-blank text, and stable ordering within each screen.
- [ ] 2.2 Extract the pending/current/advance/skip/completion logic into a plain state holder. Verify unit tests cover first hint selection, advancing, skipping the rest, finishing the last hint, marking only on advance or skip, and leaving a hint unshown after `onTargetUnavailable`.
- [ ] 2.3 Add `LocalOnboardingRevealState` and `Modifier.onboardingHint(...)` with a no-op fallback outside a host. Verify the shared module compiles and the modifier resolves the host state in a desktop smoke check.
- [ ] 2.4 Implement `OnboardingHost` with the state holder, one `Reveal` instance, retry via `tryReveal` for a bounded window, `Next`/`Skip`/outside-tap handling, and target-tap advancement. Verify the host compiles and its state-holder tests pass.
- [ ] 2.5 Implement the shared balloon with optional title, text, step indicator, `Skip`, and `Next`/completion label, plus placement mapping to the matching arrow direction. Verify the rendering compiles and the placement mapping has unit coverage.
- [ ] 2.6 Wire the host's reveal failure into `onTargetUnavailable` so the overlay closes, the tour stops, and the hint stays unshown. Verify with a temporary conditional anchor that the hint is not marked shown and appears again on a later visit.

## 3. Main screen migration and developer workflow

- [ ] 3.1 Expose `val onboarding: OnboardingController` on `MainEventExecutor` and inject it in `AircraftList`. Verify the shared module compiles and the main screen can read the controller.
- [ ] 3.2 Wrap `AircraftListScreen` content in `OnboardingHost(..., OnboardingScreen.Main)`. Verify on desktop that the first launch shows the main-menu hint once and a restart after completion shows no hint.
- [ ] 3.3 Remove the local `Reveal`, `LaunchedEffect`, and inline balloon from `MainToolsMenu`, and add `Modifier.onboardingHint(OnboardingHint.MainMenu)` to the launcher. Verify tapping the highlighted launcher advances the hint without opening the menu, and the menu opens normally after the tour is completed.
- [ ] 3.4 Remove the obsolete `commonUi/reveals/Keys.kt` and any remaining references. Verify a repository search finds no `commonUi.reveals` usage and the shared module builds.
- [ ] 3.5 Add a `BuildKonfig.DEBUG`-guarded reset action near the existing debug database action. Verify reset makes the main-menu hint appear again on the next screen entry.
- [ ] 3.6 Document the three-step process for adding a hint (catalog entry, anchor modifier, host wrapper if the screen has none) in `docs/onboarding.md`. Verify the examples match the implemented APIs and the screen-wrapping rule is stated.

## 4. Integration verification

- [ ] 4.1 Run `.\gradlew.bat :shared:desktopTest` and confirm all settings, controller, state-holder, and catalog tests pass.
- [ ] 4.2 Run `.\gradlew.bat :android:assembleDebug` and confirm the Android build succeeds with the new DI module and host.
- [ ] 4.3 Run the scenario matrix on desktop and Android: first visit shows the hint; `Next`/target tap completes it; `Skip` and outside tap complete it; restart does not repeat it; leaving mid-hint repeats it later; reset replays it; a missing target leaves it unshown; a screen with no pending hints shows no overlay.
- [ ] 4.4 Run `openspec validate "add-onboarding-hints" --strict` and confirm the change remains valid after implementation.

## Workflow follow-up

- Archive the change after the project's review requirements are satisfied.
- Verify the archived onboarding and settings specs are present under `openspec/specs/`.
