package services.airportService.model.realm

import services.airportService.model.Airport
import services.airportService.model.Frequency
import services.airportService.model.LastUpdate
import services.airportService.model.Runway
import services.airportService.model.enums.AirportType
import services.airportService.model.enums.FrequencyType
import services.airportService.model.enums.RunwaySurface

fun AirportRealm.toDomain(): Airport = Airport(
    icao = icao,
    type = AirportType.entries.firstOrNull { it.csv == type } ?: AirportType.SMALL,
    name = name,
    latitude = latitude,
    longitude = longitude,
    elevation = elevation,
    webSite = webSite,
    wiki = wiki,
    frequencies = frequencies.map(FrequencyRealm::toDomain),
    runways = runways.map(RunwayRealm::toDomain)
)

fun FrequencyRealm.toDomain(): Frequency = Frequency(
    type = FrequencyType.entries.firstOrNull { it.csv == type } ?: FrequencyType.TOWER,
    description = description,
    valueMhz = valueMhz
)

fun RunwayRealm.toDomain(): Runway = Runway(
    lengthFeet = lengthFeet,
    widthFeet = widthFeet,
    surface = RunwaySurface.entries.firstOrNull { it.csv == surface } ?: RunwaySurface.CONCRETE,
    closed = closed,
    lowNumber = lowNumber,
    lowElevationFeet = lowElevationFeet,
    lowHeading = lowHeading,
    highNumber = highNumber,
    highElevationFeet = highElevationFeet,
    highHeading = highHeading
)

fun MetadataRealm.toDomain(): LastUpdate = LastUpdate(
    time = updateTimestamp, airports = airportsCount

)