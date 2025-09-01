package services.airportService.localService

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.flow
import services.airportService.LocalBaseConverter
import services.airportService.LocalBaseConverter.UpdateResult
import services.airportService.getFilePath
import services.airportService.model.LastUpdate
import services.airportService.model.room.AirportDatabase
import services.airportService.model.room.AirportEntity
import services.airportService.model.room.FrequencyEntity
import services.airportService.model.room.RunwayEntity
import services.airportService.model.room.MetadataEntity
import services.airportService.model.room.toDomain
import java.io.File
import java.util.UUID

class RoomConverter(private val database: AirportDatabase) : LocalBaseConverter {

    private suspend fun getLinesAsSequence(filename: String, process: suspend (Map<String, String>) -> Unit) {
        println("try to open ${getFilePath(filename)}")
        val headers = File(getFilePath(filename)).useLines {
            it.firstOrNull()?.replace("\"", "")?.split(",")
        } ?: throw RuntimeException("File $filename has not valid headers")

        File(getFilePath(filename)).useLines { file ->
            file.drop(1)
                .map { it.replace("\"", "").split(",") }
                .map { headers.zip(it).toMap() }
                .forEach { process(it) }
        }
    }

    override suspend fun convertFiles(scope: CoroutineScope) = flow {
        val startTick = System.currentTimeMillis()
        var count = 0L

        val airportDao = database.airportDao()
        val metadataDao = database.metadataDao()

        // Process airports
        getLinesAsSequence(Files.AIRPORTS.filename) { row ->
            val code = row["ident"] ?: throw NullPointerException("Row with empty ICAO code: $row")
            // Insert new or update existing airport
            val newAirport = AirportEntity(
                icao = code,
                type = row["type"] ?: "",
                name = row["name"] ?: "",
                latitude = row["latitude_deg"]?.toFloatOrNull() ?: 0f,
                longitude = row["longitude_deg"]?.toFloatOrNull() ?: 0f,
                elevation = row["elevation_ft"]?.toIntOrNull() ?: 0,
                webSite = row["home_link"]?.takeIf { it.isNotBlank() },
                wiki = row["wikipedia_link"]?.takeIf { it.isNotBlank() }
            )
            airportDao.insertOrUpdateAirport(newAirport)

            count++
            if (count % 1000 == 0L) {
                emit(UpdateResult.Progress("Processing airports", count))
            }
        }

        // Process runways
        emit(UpdateResult.Progress("Processing runways", count))
        getLinesAsSequence(Files.RUNWAYS.filename) { row ->
            val airportCode = row["airport_ident"] ?: return@getLinesAsSequence
            val runway = RunwayEntity(
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
            airportDao.insertRunways(listOf(runway))
        }

        // Process frequencies
        emit(UpdateResult.Progress("Processing frequencies", count))
        getLinesAsSequence(Files.FREQUENCIES.filename) { row ->
            val airportCode = row["airport_ident"] ?: return@getLinesAsSequence
            val frequency = FrequencyEntity(
                id = UUID.randomUUID().toString(),
                airportIcao = airportCode,
                type = row["type"] ?: "",
                description = row["description"]?.takeIf { it.isNotBlank() },
                valueMhz = row["frequency_mhz"]?.toFloatOrNull() ?: 0f
            )
            airportDao.insertFrequencies(listOf(frequency))
        }

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

    override suspend fun getLastUpdate(): LastUpdate? {
        val metadata = database.metadataDao().getMetadata()
        return metadata?.toDomain()
    }
}