# d-atis-fetch

## Purpose

Fetch Digital ATIS (D-ATIS) for a given ICAO from `https://atis.info/api` and expose it as a typed service for the Metar screen. Handles US-only availability gracefully.

## ADDED Requirements

### Requirement: Fetch D-ATIS from atis.info API

The system SHALL fetch D-ATIS for a given ICAO station by calling `GET https://atis.info/api/{ICAO}` (ICAO uppercased) using the shared Ktor `HttpClient`. Before making the HTTP request, the system SHALL check `isNetworkConnected()` (matching `AviationWeatherMetarService` pattern). If offline, return `Result.Error(ErrorType.NO_CONNECTION)` immediately without attempting the network call.

#### Scenario: Single combined ATIS available

- **WHEN** a valid US ICAO with D-ATIS is requested and the API returns a JSON array with one entry `type=combined`
- **THEN** the system SHALL return `Result.Success` containing a list with one `DatisEntry` populated (`airport`, `type=AtisType.COMBINED`, `code`, `datis`, `time`, `updatedAt`)

#### Scenario: Split arrival/departure ATIS

- **WHEN** the API returns a JSON array with two entries (`type=arr` and `type=dep`, e.g., KATL)
- **THEN** the system SHALL return `Result.Success` containing both entries in the order returned by the API, with `type` mapped to `AtisType.ARRIVAL` and `AtisType.DEPARTURE` respectively

#### Scenario: Station has no D-ATIS (non-US or unknown ICAO)

- **WHEN** the API returns a JSON object `{"error":"No results found"}` (e.g., UWLW, XXXX)
- **THEN** the system SHALL return `Result.Error` with `ErrorType.EMPTY_RESULT` and message indicating no D-ATIS for that station

#### Scenario: Network error during D-ATIS fetch

- **WHEN** there is no internet connection
- **THEN** the system SHALL return `Result.Error` with `ErrorType.NO_CONNECTION`

#### Scenario: HTTP or parsing error

- **WHEN** the API returns a non-2xx status other than the error-object case, or the body cannot be parsed as either an array or error object
- **THEN** the system SHALL return `Result.Error` with `ErrorType` appropriate to the failure (`OTHER_CLIENT_ERROR`, `OTHER_SERVER_ERROR`, or `UNKNOWN`) and include the underlying message

### Requirement: Model D-ATIS response

The system SHALL define a serializable `DatisEntry` data class with fields `airport: String`, `type: AtisType` (enum: `COMBINED`, `ARRIVAL`, `DEPARTURE`), `code: String` (ATIS letter), `datis: String` (full transcript), `time: String` (HHmm), `updatedAt: String` (ISO instant). The `AtisType` enum SHALL use `@SerialName` annotations to map API values (`combined`, `arr`, `dep`) to enum constants.

#### Scenario: Deserialization tolerates unknown fields

- **WHEN** the API adds new JSON fields to an entry
- **THEN** deserialization SHALL succeed by ignoring unknown keys

### Requirement: No rate limiting for D-ATIS API

The system SHALL NOT apply rate limiting, debouncing, or client-side caching to D-ATIS requests; each `SubmitICAO` triggers a fresh network call.

#### Scenario: Rapid ICAO submissions

- **WHEN** two different ICAOs are submitted in quick succession
- **THEN** the system SHALL issue a separate `GET https://atis.info/api/{ICAO}` for each, cancelling only via the ViewModel job scope (no throttling)

### Requirement: DI wiring for AtisService

The system SHALL register `AtisInfoService` as the `AtisService` binding in the Koin `apiModule`, using the shared `HttpClient` instance (CIO, `ContentNegotiation` lenient, `HttpTimeout`, `HttpRequestRetry`).

#### Scenario: Koin provides AtisService

- **WHEN** a component requests `AtisService` from Koin
- **THEN** Koin SHALL provide an instance of `AtisInfoService`

### Requirement: Route constant for atis.info

The system SHALL define `HttpRoutes.ATIS_INFO = "https://atis.info/api"` and use it as the base for D-ATIS calls.

#### Scenario: Route used for fetch

- **WHEN** `AtisService.getDatis("KJFK")` is called
- **THEN** the HTTP request SHALL be issued to `https://atis.info/api/KJFK`
