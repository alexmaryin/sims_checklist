## Context

Metar scan is a Decompose + Compose `commonMain` feature (`feature/metarscreen`). State is `MetarScreenViewState` (serializable, `saveableMutableValue`), logic in `MetarScanner` with Koin-injected `MetarService` (`AviationWeatherMetarService`) and `AirportService`. `submitICAO` launches three parallel coroutines (`metarJob/tafJob/airportJob`) with a `Loading(loadMetar,loadTaf,loadAirport)` OR flag and `isLoading`. UI (`MetarScreen.kt`) uses `AdaptiveLayout` (portrait `Column(verticalScroll)`, landscape `Row`) and shows `WindSegment`, digit fields, `IcaoInput`, `AirportInfo` chips, `RunwayWindInfo`, `MetarInfo` (two `Text`), and history.

D-ATIS is available via `GET https://atis.info/api/{ICAO}` (FAA Digital ATIS, ~600 US fields, no auth). Live probes: success → `List<DatisEntry{airport,type,code,datis,time,updatedAt}>` with `type` mapped to enum `AtisType` (`COMBINED`, `ARRIVAL`, `DEPARTURE`; API returns `combined`/`arr`/`dep`) (e.g., KATL returns 2 entries); miss (non-US or unknown) → `{"error":"No results found"}`. No rate limiting is required per stakeholders. The user chose tabbed presentation `METAR | TAF | D-ATIS` (Option D) and wants type-aware visual distinction via icons + color.

## Goals / Non-Goals

**Goals:**
- Fetch D-ATIS in parallel with METAR/TAF/airport on `SubmitICAO`, with independent loading/empty/error state that never pollutes the global `error` snackbar for non-US stations.
- Replace stacked `MetarInfo` with a Material3 tabbed card preserving existing typography (`14sp secondary`) while adding D-ATIS tab that handles 0/1/2 entries and type badges.
- Keep `commonMain` only, reuse shared CIO `HttpClient`, keep `MetarScreenViewState` serializable.
- Minimal visual change for non-US stations (extra tab with empty state, no snackbar).

**Non-Goals:**
- Rate limiting / debounce / caching for atis.info (explicitly out of scope).
- TTS / audio playback of ATIS, parsing runway from ATIS text, filtering NOTAMs.
- Modifying `aviationweather-metar-fetch` contract or replacing `AviationWeatherMetarService`.
- Desktop/Android platform-specific UI branches.

## Decisions

### 1. Separate `AtisService` vs extending `MetarService`
**Chosen:** new `services/atisService/AtisService` + `AtisInfoService`.
**Rationale:** different authority, base URL, response shape, and error semantics; keeps `MetarService` focused. Koin: `single<AtisService>{ AtisInfoService(httpClient) }`. Reuse same `HttpClient` (CIO, timeout 15s, `HttpRequestRetry`).
**Cross-cutting change:** add `ignoreUnknownKeys = true` to the `json {}` block in `apiModule.kt` (currently uses defaults). This affects all services (`AviationWeatherMetarService`, `AirportUpdateServiceImpl`) but is safe — makes deserialization forward-compatible.
**Network pre-check:** `AtisInfoService.getDatis()` SHALL call `isNetworkConnected()` before the HTTP request (matching `AviationWeatherMetarService` pattern at `AviationWeatherMetarService.kt:15-17`). Without this, offline users wait for the 15s timeout instead of getting an immediate `NO_CONNECTION` error.
**Alternative:** add `getDatis` to `MetarService` — rejected (violates SRP, muddles specs).

### 2. Fetch model and parsing
**Chosen:** `DatisEntry` KxS model + manual `bodyAsText` → try `Json.decodeFromString<List<DatisEntry>>` else parse `{"error":…}`. Return `Result<List<DatisEntry>>`: `Success(list)`, `Error(EMPTY_RESULT, "No D-ATIS…")` for miss, `Error(NO_CONNECTION|OTHER…)` for network.
**Rationale:** `requestFor<List<DatisEntry>>` fails on the error-object shape; explicit handling is testable. `ignoreUnknownKeys` future-proofs `datis` schema.
**`type: AtisType` enum:** `DatisEntry.type` is an enum `AtisType` with values `COMBINED`, `ARRIVAL`, `DEPARTURE`. The API returns lowercase strings (`combined`, `arr`, `dep`) which are mapped via `@SerialName` annotations. This provides compile-time safety (exhaustive `when` expressions in UI) with zero boilerplate.
**Alternative:** sealed `AtisResponse` with custom serializer — heavier, unnecessary.

### 3. State and concurrency
**Chosen:** extend `MetarScreenViewState` with `datis: DatisUi? = null` (see Decision 7 for field definition), `selectedInfoTab: Int = 0` (0=METAR,1=TAF,2=D-ATIS, default 0, persist via serialization). Extend `MetarScanner.Loading` (private dataclass in `MetarScanner`, NOT in ViewState) to 4 flags: `loadMetar`, `loadTaf`, `loadAirport`, `loadAtis`. `isLoading = OR` stays aggregate for `IcaoInput` spinner; tab content shows per-tab `CircularProgressIndicator` when `loadAtis`. In `MetarScanner`: new `atisJob: Job?`, `fetchAtis(station)` suspended helper like `fetchMetar`. `submitICAO` sets `loadAtis=true`, launches `atisJob = scope.launch{ fetchAtis(station) }`. Cancel `atisJob` on new ICAO and on `submitWindAngle/submitWindSpeed` (existing pattern `MetarScanner.kt:75-92`).

**Three error handling patterns (explicit comparison):**
- `fetchMetar` / `fetchAirport`: call `setErrorState(error)` → sets global `error`, clears `metar`/`data`. Used for critical data.
- `fetchTaf`: swallows ALL errors (returns `Success("")`). TAF is supplementary; failure is silent.
- `fetchAtis`: stores inline error in `datis` field (see Decision 7), NEVER calls `setErrorState`. D-ATIS is US-only; failure must not break non-US UX.

**Alternative:** separate `isLoadingAtis` boolean + combine — equivalent; keep single `Loading` dataclass for consistency.

### 4. UI: tabbed card replacing `MetarInfo`
**Chosen:** new `InfoTabs` composable (e.g., `feature/metarscreen/ui/InfoTabs.kt`) using `PrimaryTabRow` + `Tab` (Material3). For exactly 3 tabs, `PrimaryTabRow` is the correct choice (`ScrollableTabRow` is for 5+). Tabs show `METAR` / `TAF` / `D-ATIS` labels; badge/dot when data present (METAR/TAF `rawX.isNotBlank()`, D-ATIS `entries.isNotEmpty()`). Content: simple `when(selectedTab)` with `Column(verticalScroll)` and `SelectionContainer { Text(selectable, 14sp, secondary, lineHeight) }`. For D-ATIS with >1 entry, render each entry as a sub-card/segment: header `Row(Icon type, Text code, Badge type, Text time)` + `Text(datis, 14sp secondary)`. Icons: 3 XML vector drawables (matching existing `composeResources/drawable/` convention) — `COMBINED` (e.g., `call_merge`/`sync`), `ARRIVAL` (landing), `DEPARTURE` (takeoff). Colors: per-type `ColorScheme` mapping — `COMBINED → secondaryContainer/onSecondaryContainer`, `ARRIVAL → primaryContainer/onPrimaryContainer`, `DEPARTURE → tertiaryContainer/onTertiaryContainer` (or similar contrasts, verified against `SimColors`). Header badge uses `AssistChip`/`SuggestionChip` or custom `Surface(shape=CircleShape)`.

**Accessibility:** each type icon SHALL have `contentDescription` (e.g., "Combined ATIS", "Arrival ATIS", "Departure ATIS"). Tab switching SHALL use standard Material3 semantics (screen readers announce tab changes). Tab touch targets SHALL be ≥48dp (Material3 default).

**Rationale:** tabs save vertical space, match user request D, keep portrait/landscape `AdaptiveLayout` right column unchanged — card just swaps two Texts for a `TabRow`. Type distinction satisfies requirement 2 without adding typography debt.
**Alternative:** keep stacked blocks + add D-ATIS block — rejected per product (too tall, non-US empty space). BottomSheet — rejected (modal friction).

### 5. Empty / loading / error in D-ATIS tab
**Chosen:** `loadAtis` → `CircularProgressIndicator` centered; `datis==null && !loadAtis && !isInitial` (after first fetch) → `Text("No D-ATIS available for this station (US only)", 14sp, secondary, italic)`; network error → same inline `Text("Failed to load D-ATIS: …")` with retry tap calling `SubmitICAO` again. Never call `setErrorState` for D-ATIS `EMPTY_RESULT`.

**`AnimatedVisibility` condition:** extend current `visible = state.data.rawMetar.isNotBlank() || state.data.rawTaf.isNotBlank()` to include D-ATIS states:
```kotlin
visible = state.data.rawMetar.isNotBlank() || 
          state.data.rawTaf.isNotBlank() || 
          state.datis != null || 
          state.data.loadAtis  // Note: loadAtis is in MetarScanner.Loading, not ViewState; 
                               // need to expose via ViewState or compute isLoading differently
```
**Implementation note:** `loadAtis` is in `MetarScanner.Loading` (private), not in `MetarScreenViewState`. Two options:
1. Add `isLoadingAtis: Boolean` to `MetarScreenViewState` (computed from `combineLoading.loadAtis`)
2. Extend `AnimatedVisibility` to use `state.isLoading` (aggregate) — but this shows the card even when only METAR is loading

**Decision:** Option 1 — add `isLoadingAtis: Boolean = false` to `MetarScreenViewState`, updated by `MetarScanner` alongside `isLoading`. This keeps the visibility condition precise.

**Rationale:** avoids snackbar noise for Russian/EU ICAOs (screenshot UWLW case).

### 6. Persistence & navigation
`selectedInfoTab` is part of `MetarScreenViewState` → survives `saveableMutableValue`. No navigation change.

**Tab reset on new ICAO:** when `submitICAO` is called, reset `selectedInfoTab = 0` (METAR) to avoid confusing UX where user returns to a new station and sees D-ATIS tab from previous station. This is a UX improvement over strict persistence.

### 7. DatisUi model definition
**Chosen:** `DatisUi` is a thin wrapper around `List<DatisEntry>` with no transformation:
```kotlin
@Serializable
data class DatisUi(
    val entries: List<DatisEntry> = emptyList(),
    val error: String? = null  // inline error message for D-ATIS tab (e.g., "Failed to load D-ATIS: …")
)

@Serializable
enum class AtisType {
    @SerialName("combined") COMBINED,
    @SerialName("arr") ARRIVAL,
    @SerialName("dep") DEPARTURE
}
```
`DatisEntry` is the same model from `AtisService` (no separate `DatisEntryUi`). The `error` field stores inline error messages (never global `error`). When `entries.isEmpty() && error == null && !loadAtis` → show empty state. When `error != null` → show error with retry.

**Rationale:** avoids duplicating models; `DatisUi` adds only the `error` field for inline error handling (Decision 3 pattern).

### 8. Open questions resolved
- **Icon set:** XML vector drawables matching existing `composeResources/drawable/` convention (27 XML files already present). Material Symbols style (e.g., `flight_land`, `flight_takeoff`, `sync_alt`).
- **Auto-select D-ATIS tab:** No. Default stays METAR (tab 0) even if METAR/TAF empty but D-ATIS present. Product decision: pilots expect METAR first; auto-switch is confusing.
- **Time formatting:** Use raw `HHmmZ` format from `DatisEntry.time` (matches aviation convention already used in METAR/TAF). No local-time conversion.

## Risks / Trade-offs

- **Polymorphic JSON (array vs object)** → Mitigation: `bodyAsText` + tryParse both shapes, unit tests with mocked `HttpClient`.
- **Long ATIS strings (600+ chars, NOTAMs)** → Mitigation: `SelectionContainer`, `verticalScroll` inside tab content with `heightIn(max=320.dp)`, selectable/copy.
- **Tab badge confusion (METAR/TAF empty vs D-ATIS missing)** → Mitigation: badges only when non-empty; D-ATIS tab always visible but shows empty state.
- **Theme contrast for type colors on dark/light** → Mitigation: use `colorScheme` containers, manual contrast check on both themes; icons provide redundant encoding.
- **Additional network call per ICAO** → Extra latency without gating; mitigated by parallel launch, short timeout already (15s), no retry queue growth.
- **Serialization migration for `MetarScreenViewState`** → Adding optional fields with defaults keeps existing `saveable` JSON compatible; no migration needed.

## Migration Plan

1. Add `AtisService` + route + model + Koin binding — no migration.
2. Extend `MetarScreenViewState` with defaults — backward compatible.
3. Add `InfoTabs` and replace `MetarInfo` usage in `MetarScreen.kt` (feature-flag via tab default 0).
4. Extend `MetarScanner` with 4th job — behind same `submitICAO` path.
5. No DB / API version change. Rollback: revert `MetarScreen.kt` to `MetarInfo` and remove Koin binding; `datis` field ignored due to default.

## Open Questions

All open questions have been resolved (see Decision 8).
