## 1. Service & model — d-atis-fetch

- [x] 1.1 Add `HttpRoutes.ATIS_INFO = "https://atis.info/api"` to `shared/src/commonMain/kotlin/services/metarService/HttpRoutes.kt`.
- [x] 1.2 Create `shared/src/commonMain/kotlin/services/atisService/model/DatisEntry.kt` with `@Serializable` data class and `AtisType` enum:
  ```kotlin
  @Serializable
  data class DatisEntry(
      val airport: String,
      val type: AtisType,
      val code: String,
      val datis: String,
      val time: String,
      val updatedAt: String
  )
  
  @Serializable
  enum class AtisType {
      @SerialName("combined") COMBINED,
      @SerialName("arr") ARRIVAL,
      @SerialName("dep") DEPARTURE
  }
  ```
  Use `ignoreUnknownKeys` at the Json level (see task 1.5).
- [x] 1.3 Create `shared/src/commonMain/kotlin/services/atisService/AtisService.kt` interface `suspend fun getDatis(icao: String): Result<List<DatisEntry>>`.
- [x] 1.4 Create `shared/src/commonMain/kotlin/services/atisService/AtisInfoService.kt` implementing `AtisService` via `GET ${ATIS_INFO}/{ICAO}` with shared CIO `HttpClient`. **Pre-check:** call `isNetworkConnected()` before HTTP request (matching `AviationWeatherMetarService.kt:15-17` pattern). Parse `bodyAsText` → try `List<DatisEntry>` else `{"error":…}` → map to `Result` (`EMPTY_RESULT` for miss, `NO_CONNECTION` for offline, other mappings).
- [x] 1.5 **Cross-cutting:** Add `ignoreUnknownKeys = true` to the `json {}` block in `shared/src/commonMain/kotlin/di/apiModule.kt`. This affects all services (`AviationWeatherMetarService`, `AirportUpdateServiceImpl`) but is safe — makes deserialization forward-compatible. Verify existing tests still pass.
- [x] 1.6 Register Koin binding in `shared/src/commonMain/kotlin/di/apiModule.kt`: `single<AtisService>{ AtisInfoService(httpClient) }`.
- [x] 1.7 Add `commonTest` mock tests for `AtisInfoService` (KJFK `COMBINED`, KATL `ARRIVAL`+`DEPARTURE`, UWLW empty error, network error, offline pre-check) using `ktor-client-mock`.

## 2. State & ViewModel — parallel fetch

- [x] 2.1 Create `shared/src/commonMain/kotlin/feature/metarscreen/model/DatisUi.kt` with explicit fields:
  ```kotlin
  @Serializable
  data class DatisUi(
      val entries: List<DatisEntry> = emptyList(),
      val error: String? = null  // inline error message for D-ATIS tab
  )
  ```
  `DatisEntry` (with `AtisType` enum: `COMBINED`, `ARRIVAL`, `DEPARTURE`) is imported from `services/atisService.model`. No separate `DatisEntryUi` — reuse the service model directly.
- [x] 2.2 Extend `shared/src/commonMain/kotlin/feature/metarscreen/MetarScreenViewState.kt` with `datis: DatisUi? = null`, `selectedInfoTab: Int = 0`, and `isLoadingAtis: Boolean = false` (defaults keep `saveableMutableValue` compatible); verify serialization.
- [x] 2.3a Extend `shared/src/commonMain/kotlin/feature/metarscreen/MetarScanner.kt` `Loading` dataclass (line 38-40) to 4 flags: add `var loadAtis: Boolean = false`. Update `state` getter to include `loadAtis`. Add `private var atisJob: Job? = null` and inject `AtisService` via Koin.
- [x] 2.3b Implement `fetchAtis(station)` suspended helper in `MetarScanner` (pattern: `val response = atisService.getDatis(station); combineLoading.loadAtis = false; state.update { ... }`). On `Success` → set `datis = DatisUi(entries)`, `isLoadingAtis = false`. On `Error(EMPTY_RESULT)` → set `datis = null`, `isLoadingAtis = false`, NO global `error`. On other `Error` → set `datis = DatisUi(error = "Failed to load D-ATIS: ${error.message}")`, `isLoadingAtis = false`, NO global `error`.
- [x] 2.3c Launch `atisJob = scope.launch { fetchAtis(station) }` in `submitICAO` alongside 3 existing jobs. Add `atisJob?.cancel().also { atisJob = null }` to `submitWindAngle`, `submitWindSpeed`, and `submitICAO` (before launching new jobs). Reset `selectedInfoTab = 0` in `submitICAO` (UX: avoid showing D-ATIS tab from previous station).
- [x] 2.4 Add unit tests for `MetarScanner` parallel fetch: success populates `datis`, empty does not set `error`, cancellation on wind edit, aggregate `isLoading` OR with `loadAtis`.

## 3. UI — tabbed METAR/TAF/D-ATIS

- [x] 3.1 Add 3 type icons for D-ATIS (`COMBINED`/`ARRIVAL`/`DEPARTURE`) as **XML vector drawables** (matching existing `composeResources/drawable/` convention — 27 XML files present) to `shared/src/commonMain/composeResources/drawable`. Use Material Symbols style: `COMBINED` (no icon shown), `ARRIVAL` (`flight_land`), `DEPARTURE` (`flight_takeoff`). Wire to `composeResources` generated accessors.
- [x] 3.2 Create `shared/src/commonMain/kotlin/feature/metarscreen/ui/InfoTabs.kt` (or `InfoTabsCard.kt`) — Material3 `PrimaryTabRow` + `Tab` for `METAR|TAF|D-ATIS` with badge/dot when data present, `when(selectedTab)` content: selectable `Text` `14sp secondary` scrollable, per-tab loading/empty handling.
- [x] 3.3 Implement D-ATIS tab entry rendering in `InfoTabs`: header `Row(Icon(type)+color badge, Text(code), Badge(type label), Text(time))` with `primary/secondary/tertiaryContainer` colors per `AtisType.COMBINED/ARRIVAL/DEPARTURE`, body `Text(datis)`, `Divider` between entries; handle 0/1/2 entries. Use exhaustive `when(atyEntry.type)` for icon/color mapping.
- [x] 3.4 Implement D-ATIS tab states inside `InfoTabs`: `loadAtis → CircularProgressIndicator`, `empty → "No D-ATIS available for this station (US only)"` italic secondary, `network error → "Failed to load D-ATIS: …"` + retry `onClick { submitICAO(lastIcao) }`.
- [x] 3.5 Replace `MetarInfo(state.data)` in `shared/src/commonMain/kotlin/feature/metarscreen/ui/MetarScreen.kt` with `InfoTabs` (selected index from `state.selectedInfoTab`, `onSelectTab -> state update` or UI event), adjust `AnimatedVisibility` condition to `visible = state.data.rawMetar.isNotBlank() || state.data.rawTaf.isNotBlank() || state.datis != null || state.isLoadingAtis`, keep `AdaptiveLayout` portrait/landscape behavior, verify against UWLW and KJFK/KATL previews.
- [x] 3.6 **Accessibility:** add `contentDescription` to each type icon in `InfoTabs` (e.g., "Combined ATIS", "Arrival ATIS", "Departure ATIS"). Verify tab switching announces correctly with screen reader (TalkBack on Android, VoiceOver on Desktop if applicable). Verify tab touch targets ≥48dp.

## 4. Polish & validation

- [x] 4.1 Run `./gradlew :shared:check` / desktop & Android unit tests; fix serialization/theme contrasts in both light/dark with the new badge colors.
- [x] 4.2 Manual verification: `KJFK` (`COMBINED`), `KATL` (`ARRIVAL`+`DEPARTURE`), `UWLW` (empty non-US) — tabs, icons/colors, parallel load not blocking METAR/TAF, no snackbar for D-ATIS empty. Test offline scenario (airplane mode) — should get immediate `NO_CONNECTION` error in D-ATIS tab, not 15s timeout.
