## ADDED Requirements

### Requirement: Fetch raw METAR from AviationWeather.gov
The system SHALL fetch raw METAR text for a given ICAO station identifier by calling `GET https://aviationweather.gov/api/data/metar?ids={station}&format=raw`.

#### Scenario: Successful METAR fetch
- **WHEN** a valid ICAO station identifier is provided and the API returns raw METAR text
- **THEN** the system SHALL return `Result.Success` containing a `MetarTaf` with the `metar` field populated with the raw observation string and `icao` set to the requested station

#### Scenario: Station has no METAR data
- **WHEN** the API returns HTTP 204 (No Content) for the requested station
- **THEN** the system SHALL return `Result.Error` with `ErrorType.EMPTY_RESULT`

#### Scenario: Network error during METAR fetch
- **WHEN** a network error or non-2xx/204 response occurs
- **THEN** the system SHALL return `Result.Error` with the appropriate `ErrorType` as mapped by the existing `requestFor` helper

### Requirement: Fetch raw TAF from AviationWeather.gov
The system SHALL fetch raw TAF text for a given ICAO station identifier by calling `GET https://aviationweather.gov/api/data/taf?ids={station}&format=raw`.

#### Scenario: Successful TAF fetch
- **WHEN** a valid ICAO station identifier is provided and the API returns raw TAF text
- **THEN** the system SHALL populate the `taf` field of the `MetarTaf` result with the raw forecast string

#### Scenario: Station has no TAF data
- **WHEN** the API returns HTTP 204 (No Content) for the TAF request
- **THEN** the system SHALL set the `taf` field to an empty string in the returned `MetarTaf`

### Requirement: Combine METAR and TAF into MetarTaf result
The system SHALL combine the fetched METAR and TAF raw strings into a single `MetarTaf` data class, making both HTTP calls within the same `getMetar` invocation.

#### Scenario: Both METAR and TAF available
- **WHEN** both METAR and TAF fetches succeed with non-empty data
- **THEN** the system SHALL return `Result.Success(MetarTaf(icao=station, metar=rawMetar, taf=rawTaf))`

#### Scenario: METAR succeeds but TAF fails
- **WHEN** METAR fetch succeeds but TAF fetch returns an error
- **THEN** the system SHALL return the TAF error as `Result.Error`

#### Scenario: METAR fails
- **WHEN** METAR fetch returns an error
- **THEN** the system SHALL return the METAR error as `Result.Error` without attempting the TAF call

### Requirement: DI wiring for AviationWeatherMetarService
The system SHALL register `AviationWeatherMetarService` as the `MetarService` binding in the Koin dependency injection module, using the shared `HttpClient` instance.

#### Scenario: Koin provides AviationWeatherMetarService
- **WHEN** a component requests `MetarService` from Koin
- **THEN** Koin SHALL provide an instance of `AviationWeatherMetarService`
