package services.airportService.model.room

import services.airportService.model.Airport
import services.airportService.model.Frequency
import services.airportService.model.HistoryAirport
import services.airportService.model.LastUpdate
import services.airportService.model.Runway
import services.airportService.model.enums.AirportType
import services.airportService.model.enums.FrequencyType
import services.airportService.model.enums.RunwaySurface
import utils.filterDigitsToInt
import utils.ifZero

fun AirportEntity.toDomain(
    frequencies: List<FrequencyEntity> = emptyList(),
    runways: List<RunwayEntity> = emptyList()
): Airport = Airport(
    icao = icao,
    type = AirportType.entries.firstOrNull { it.csv == type } ?: AirportType.SMALL,
    name = name,
    latitude = latitude,
    longitude = longitude,
    elevation = elevation,
    webSite = webSite,
    wiki = wiki,
    frequencies = frequencies.map(FrequencyEntity::toDomain),
    runways = runways.map(RunwayEntity::toDomain)
)

fun FrequencyEntity.toDomain(): Frequency = Frequency(
    type = FrequencyType.entries.firstOrNull { it.csv == type } ?: FrequencyType.TOWER,
    description = description,
    valueMhz = valueMhz
)

fun RunwayEntity.toDomain(): Runway = Runway(
    lengthFeet = lengthFeet,
    widthFeet = widthFeet,
    surface = RunwaySurface.entries.firstOrNull { it.csv == surface } ?: RunwaySurface.CONCRETE,
    closed = closed,
    lowNumber = lowNumber,
    lowElevationFeet = lowElevationFeet,
    lowHeading = lowHeading.ifZero { lowNumber.filterDigitsToInt() * 10 },
    highNumber = highNumber,
    highElevationFeet = highElevationFeet,
    highHeading = highHeading.ifZero { highNumber.filterDigitsToInt() * 10 }
)

fun HistoryAirportEntity.toDomain(): HistoryAirport = HistoryAirport(
    timestamp = timestamp,
    icao = icao,
    name = name
)

fun MetadataEntity.toDomain(): LastUpdate = LastUpdate(
    time = updateTimestamp,
    airports = airportsCount,
)