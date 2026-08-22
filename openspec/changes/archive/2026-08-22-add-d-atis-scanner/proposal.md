## Why

Pilots using the Metar scan screen need real-world Digital ATIS (D-ATIS) to confirm runways in use, approaches in use, and NOTAM-like advisories. US airports publish D-ATIS via `https://atis.info/api/{ICAO}` (FAA Digital ATIS, ~600 airports). The current Metar screen only shows METAR/TAF raw text and wind/runway calculator. Adding a parallel D-ATIS fetch and displaying it tabbed with METAR/TAF completes the pre-flight briefing workflow without extra user steps.

## What Changes

- Add `AtisService` / `AtisInfoService` that fetches D-ATIS from `https://atis.info/api/{ICAO}` (no auth, JSON array on success, `{"error":"No results found"}` on miss). Fetch is fire-and-forget parallel to METAR/TAF/airport when ICAO is submitted.
- Extend `MetarScanner` state and loading model to launch a 4th coroutine job (`atisJob`) alongside `metarJob/tafJob/airportJob` using the existing `SupervisorJob` scope. Cancel on new ICAO or manual wind edits (existing pattern).
- Extend `MetarScreenViewState` (serializable) with D-ATIS UI state: list of entries (`airport`, `type: AtisType` enum (`COMBINED|ARRIVAL|DEPARTURE`), `code` letter, `datis` text, `time`, `updatedAt`), selected tab index. Extend `MetarScanner.Loading` (private dataclass in `MetarScanner`, NOT in ViewState) with 4th flag `loadAtis`; aggregate `isLoading = OR` stays in ViewState.
- Redesign `MetarScreen.kt` raw-text area: replace the current stacked `MetarInfo` (two `Text` blocks) with a **tabbed card** `METAR | TAF | D-ATIS`. Tabs use Material3 `TabRow`/`ScrollableTabRow` (or `PrimaryTabRow`). Content is scrollable selectable text. Tab badge/dot indicates availability.
- For D-ATIS tab: when multiple entries exist (e.g., KATL `ARRIVAL`+`DEPARTURE`), show sub-segments per entry with type icon + color badge and letter code (e.g., `ARR · C · 0852Z`, `DEP · P`). `COMBINED` uses a neutral/combined icon+color. Colors are distinct per type and sourced from `MaterialTheme.colorScheme` (no new design tokens). Icons are small vector drawables (arrival/ departure / combined). When `error == No results` → inline empty state "No D-ATIS available for this station (US only)" — **not** a snackbar error. When loading → inline progress indicator in the tab content.
- Add `HttpRoutes.ATIS_INFO = "https://atis.info/api"` and Koin binding `single<AtisService>{ AtisInfoService(httpClient) }` reusing the shared CIO `HttpClient`. **Cross-cutting change:** add `ignoreUnknownKeys = true` to the `json {}` block in `apiModule.kt` (currently uses defaults; required for D-ATIS schema forward-compat and affects all services).
- No rate limiting / debouncing for the D-ATIS endpoint (per product decision).

## Capabilities

### New Capabilities
- `d-atis-fetch`: Fetch and model D-ATIS from atis.info API, including error mapping and DI wiring.
- `metar-screen-tabbed-info`: Tabbed presentation of METAR / TAF / D-ATIS raw texts on the Metar screen with type-aware D-ATIS rendering (icons, colors, empty/loading states).

### Modified Capabilities
- `aviationweather-metar-fetch`: No requirement change — remains as-is. The parallel D-ATIS fetch is additive; METAR/TAF contracts unchanged. Listed here only for traceability, no delta spec required.

## Impact

- Code: `shared/src/commonMain/kotlin/services/atisService/**`, `shared/src/commonMain/kotlin/feature/metarscreen/**` (ViewState, Scanner, UI), `shared/src/commonMain/kotlin/di/apiModule.kt`, `shared/src/commonMain/kotlin/services/metarService/HttpRoutes.kt`, resources for 3 type icons.
- Dependencies: reuse existing `ktor-client-core/cio`, `kotlinx-serialization-json` (add `ignoreUnknownKeys` if not already), `compose-material3` tabs. No new external deps.
- Platforms: Android + Desktop (KMP `commonMain` only).
- Breaking: none. D-ATIS failure never surfaces as global error; non-US stations unchanged except extra tab showing empty state.
