package services.airportService.localService

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.flow
import services.airportService.LocalBaseConverter
import services.airportService.LocalBaseConverter.UpdateResult
import services.airportService.getFilePath
import services.airportService.model.room.*
import java.io.File
import java.util.*

class RoomConverter(private val database: AirportDatabase) : LocalBaseConverter {

    private fun getCsvAsListOfMaps(filename: String): List<Map<String, String>> {
        println("try to open ${getFilePath(filename)}")
        val file = File(getFilePath(filename))
        val lines = file.readLines()
        val headers = lines.firstOrNull()?.replace("\"", "")?.split(",")
            ?: throw RuntimeException("File $filename has not valid headers")

        return lines.drop(1)
            .map { it.replace("\"", "").split(",") }
            .map { headers.zip(it).toMap() }
    }

    override suspend fun convertFiles(scope: CoroutineScope) = flow {
        val startTick = System.currentTimeMillis()

        val airportDao = database.airportDao()
        val metadataDao = database.metadataDao()

        // Process airports
        emit(UpdateResult.Progress("Processing airports", 0))
        val airports = getCsvAsListOfMaps(Files.AIRPORTS.filename).map { row ->
            val code = row["ident"] ?: throw NullPointerException("Row with empty ICAO code: $row")
            AirportEntity(
                icao = code,
                type = row["type"] ?: "",
                name = row["name"] ?: "",
                latitude = row["latitude_deg"]?.toFloatOrNull() ?: 0f,
                longitude = row["longitude_deg"]?.toFloatOrNull() ?: 0f,
                elevation = row["elevation_ft"]?.toIntOrNull() ?: 0,
                webSite = row["home_link"]?.takeIf { it.isNotBlank() },
                wiki = row["wikipedia_link"]?.takeIf { it.isNotBlank() }
            )
        }
        airportDao.insertOrUpdateAirports(airports)
        emit(UpdateResult.Progress("Processing airports", airports.size.toLong()))

        // Process runways
        emit(UpdateResult.Progress("Processing runways", 0))
        val runways = getCsvAsListOfMaps(Files.RUNWAYS.filename).mapNotNull { row ->
            val airportCode = row["airport_ident"] ?: return@mapNotNull null
            RunwayEntity(
                id = UUID.randomUUID().toString(),
                airportIcao = airportCode,
                lengthFeet = row["length_ft"]?.toIntOrNull(),
                widthFeet = row["width_ft"]?.toIntOrNull(),
                surface = row["surface"] ?: "",
                closed = row["closed"]?.toIntOrNull() == 1,
                lowNumber = row["le_ident"] ?: "",
                lowElevationFeet = row["le_elevation_ft"]?.toIntOrNull(),
                lowHeading = row["le_heading_degT"]?.toIntOrNull() ?: 0,
                highNumber = row["he_ident"] ?: "",
                highElevationFeet = row["he_elevation_ft"]?.toIntOrNull(),
                highHeading = row["he_heading_degT"]?.toIntOrNull() ?: 0
            )
        }
        airportDao.deleteRunways()
        airportDao.insertRunways(runways)
        emit(UpdateResult.Progress("Processing runways", runways.size.toLong()))

        // Process frequencies
        emit(UpdateResult.Progress("Processing frequencies", 0))
        val frequencies = getCsvAsListOfMaps(Files.FREQUENCIES.filename).mapNotNull { row ->
            val airportCode = row["airport_ident"] ?: return@mapNotNull null
            FrequencyEntity(
                id = UUID.randomUUID().toString(),
                airportIcao = airportCode,
                type = row["type"] ?: "",
                description = row["description"]?.takeIf { it.isNotBlank() },
                valueMhz = row["frequency_mhz"]?.toFloatOrNull() ?: 0f
            )
        }
        airportDao.deleteFrequencies()
        airportDao.insertFrequencies(frequencies)
        emit(UpdateResult.Progress("Processing frequencies", frequencies.size.toLong()))

        val finish = System.currentTimeMillis()
        println("Totally finished in ${finish - startTick} ms")

        // Update metadata
        val finalCount = airportDao.getAirportCount()
        val metadata = MetadataEntity(
            id = "main",
            updateTimestamp = finish,
            airportsCount = finalCount
        )
        metadataDao.insertOrUpdateMetadata(metadata)

        emit(UpdateResult.Success(finalCount))
    }
}