## Why

The current `CheckWxMetarService` depends on a paid CheckWX API key (`BuildKonfig.WXAPI_KEY`). AviationWeather.gov provides a free, public API from NOAA/NWS with worldwide METAR/TAF coverage and no authentication required. Switching to this source eliminates the API key dependency and reduces external service costs.

## What Changes

- Add a new `AviationWeatherMetarService` implementation of the existing `MetarService` interface
- Fetch raw METAR and TAF text from `https://aviationweather.gov/api/data/metar` and `/api/data/taf` endpoints using `format=raw`
- Parse the raw text responses into the existing `MetarTaf` data class (icao, metar string, taf string)
- The existing `metarkt` library continues to handle downstream parsing of raw METAR strings into structured data
- Wire the new implementation in Koin DI, replacing `CheckWxMetarService`
- Add HTTP route constants for AviationWeather.gov endpoints

## Capabilities

### New Capabilities
- `aviationweather-metar-fetch`: Fetch raw METAR/TAF text from AviationWeather.gov API and map responses into the existing `MetarTaf` model

### Modified Capabilities

## Impact

- **Code**: New file `AviationWeatherMetarService.kt` in `services/metarService/`; updated `HttpRoutes.kt`; updated Koin DI module `di/apiModule.kt`
- **Dependencies**: No new dependencies — uses existing Ktor HTTP client and `metarkt` parser
- **APIs**: Replaces CheckWX API calls with AviationWeather.gov API calls; no change to `MetarService` interface or downstream consumers
- **Config**: Removes dependency on `BuildKonfig.WXAPI_KEY` for METAR fetching (key may still be used elsewhere)
