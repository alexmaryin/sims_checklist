## 1. HTTP Routes

- [x] 1.1 Add AviationWeather.gov base URL and endpoint constants to `HttpRoutes.kt` (`AVIATION_WEATHER_BASE`, `AVIATION_WEATHER_METAR`, `AVIATION_WEATHER_TAF`)

## 2. AviationWeatherMetarService Implementation

- [x] 2.1 Create `AviationWeatherMetarService.kt` implementing `MetarService` interface with `HttpClient` constructor parameter
- [x] 2.2 Implement METAR fetch: call `/api/data/metar?ids={station}&format=raw`, map response to raw string, handle 204 as empty result
- [x] 2.3 Implement TAF fetch: call `/api/data/taf?ids={station}&format=raw`, map response to raw string, handle 204 as empty string
- [x] 2.4 Combine METAR and TAF into `MetarTaf(icao, metar, taf)` and return `Result.Success`; propagate errors per spec (METAR failure short-circuits, TAF failure returns error)

## 3. Dependency Injection

- [x] 3.1 Update Koin `apiModule.kt` to bind `AviationWeatherMetarService` as `MetarService` instead of `CheckWxMetarService`

## 4. Verification

- [x] 4.1 Build the project and verify compilation succeeds with no errors
