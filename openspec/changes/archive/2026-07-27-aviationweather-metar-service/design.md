## Context

The app currently uses `CheckWxMetarService` to fetch METAR/TAF data from api.checkwx.com. This requires a paid API key stored in `BuildKonfig.WXAPI_KEY`. The `MetarService` interface returns `Result<MetarTaf>` where `MetarTaf` contains raw METAR and TAF strings, parsed downstream by the `metarkt` library.

AviationWeather.gov (NOAA/NWS) provides a free public API with worldwide METAR/TAF coverage. The `/api/data/metar` and `/api/data/taf` endpoints support `format=raw` which returns plain text observations — exactly what we need to feed into `metarkt`.

## Goals / Non-Goals

**Goals:**
- Implement `AviationWeatherMetarService` conforming to the existing `MetarService` interface
- Fetch raw METAR and TAF text for a given ICAO station
- Map responses into the existing `MetarTaf` data class
- Wire the new implementation in Koin DI, replacing `CheckWxMetarService`

**Non-Goals:**
- Changing the `MetarService` interface or `MetarTaf` model
- Using decoded/JSON format from AviationWeather.gov (we only need raw text)
- Implementing caching, retry logic, or rate limiting beyond what Ktor already provides
- Removing `CheckWxMetarService` source file (keep as fallback reference)

## Decisions

**1. Use `format=raw` with separate METAR and TAF calls**
The API supports `taf=true` on the METAR endpoint to include TAF in a single call, but the combined raw text response mixes METAR and TAF blocks with no clear delimiter. Two separate calls (`/api/data/metar?ids=X&format=raw` and `/api/data/taf?ids=X&format=raw`) return clean, predictable text. The overhead of a second HTTP call is negligible for single-station lookups.

*Alternative considered*: Single call with `taf=true` and parsing the combined response. Rejected because the raw text format concatenates METAR and TAF without a reliable separator, making splitting fragile.

**2. Use `requestFor<String>` for raw text responses**
The existing `HttpClient.requestFor<T>()` extension can deserialize `String` responses directly. This avoids creating intermediate response model classes (unlike CheckWX which needed `WxMetar`/`WxTaf` wrappers for JSON).

**3. Handle 204 No Content as empty result**
AviationWeather.gov returns HTTP 204 when no data is available for a station. The `requestFor` helper treats non-2xx as errors, so we need to handle this case explicitly — map 204 to `Result.Error(ErrorType.EMPTY_RESULT)`.

**4. Add base URL constant to HttpRoutes**
Follow the existing pattern in `HttpRoutes.kt` where each provider has its own base URL and endpoint constants.

## Risks / Trade-offs

- **[Rate limiting]** AviationWeather.gov limits to 100 requests/minute. → For single-station lookups in an interactive app this is unlikely to be hit. If needed, add request throttling later.
- **[No CORS]** The API does not support CORS. → Not an issue for native (Android/Desktop) clients; only affects browser-based clients which this app is not.
- **[Service availability]** Government services can have downtime. → Keep `CheckWxMetarService` source available for quick re-enablement if needed.
- **[204 handling]** The current `requestFor` may not handle 204 gracefully since it expects a body. → Need to verify Ktor behavior with 204 and add explicit handling if needed.
