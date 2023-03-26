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
    type = AirportType.values().first { it.name == type },
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
    type = FrequencyType.values().first { it.name == type },
    description = description,
    valueMhz = valueMhz
)

fun RunwayRealm.toDomain(): Runway = Runway(
    lengthFeet = lengthFeet,
    widthFeet = widthFeet,
    surface = RunwaySurface.values().first { it.name == surface },
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