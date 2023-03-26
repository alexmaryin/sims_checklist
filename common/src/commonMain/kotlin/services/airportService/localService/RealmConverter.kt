package services.airportService.localService

import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import io.realm.kotlin.query.find
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import services.airportService.LocalBaseConverter
import services.airportService.LocalBaseConverter.UpdateResult
import services.airportService.model.LastUpdate
import services.airportService.model.realm.*
import java.io.File
import java.nio.file.Paths

class RealmConverter(private val realm: Realm) : LocalBaseConverter {

    private fun getLinesAsSequence(filename: String, process: (Map<String, String>) -> Unit) {
        val headers = File(filename).useLines {
            it.firstOrNull()?.replace("\"", "")?.split(",")
        } ?: throw RuntimeException("File $filename has not valid headers")
        File(filename).useLines { file ->
            file.drop(1)
                .map { it.replace("\"", "").split(",") }
                .map { headers.zip(it).toMap() }
                .forEach { process(it) }
        }
    }

    override suspend fun convertFiles(scope: CoroutineScope) = flow {
        val startTick = System.currentTimeMillis()
        var count = 0L
        realm.write {
            getLinesAsSequence(Files.AIRPORTS.filename) { row ->
                val code = row["ident"] ?: throw NullPointerException("Row with empty ICAO code: $row")
                val airport = query<AirportRealm>("icao = '$code'").first().find()
                if (airport != null) {
                    airport.name = row["name"] ?: ""
                    airport.type = row["type"] ?: "small_airport"
                    airport.webSite = row["home_link"]
                    airport.wiki = row["wikipedia_link"]
                    airport.elevation = row["elevation_ft"]?.toIntOrNull() ?: 0
                    airport.latitude = row["latitude_deg"]?.toFloatOrNull() ?: 0.0f
                    airport.longitude = row["longitude_deg"]?.toFloatOrNull() ?: 0.0f
                } else {
                    copyToRealm(AirportRealm().apply {
                        icao = code
                        name = row["name"] ?: ""
                        type = row["type"] ?: "small_airport"
                        webSite = row["home_link"]
                        wiki = row["wikipedia_link"]
                        elevation = row["elevation_ft"]?.toIntOrNull() ?: 0
                        latitude = row["latitude_deg"]?.toFloatOrNull() ?: 0.0f
                        longitude = row["longitude_deg"]?.toFloatOrNull() ?: 0.0f
                    })
                }
                count++
            }
        }
        emit(UpdateResult.Progress("airports in database", count))
        val runways = convertRunways()
        println("Updated $runways runways")
        emit(UpdateResult.Progress("runways in database", count))
        val freqs = convertFrequencies()
        println("Updated $freqs frequencies")
        val finish = System.currentTimeMillis()
        println("Totally finished in ${finish - startTick} ms")
        realm.write {
            copyToRealm(MetadataRealm().apply {
                updateTimestamp = finish
                airportsCount = count
            })
        }
        emit(UpdateResult.Success(count))
    }

    override suspend fun getLastUpdate(): LastUpdate? {
        val results = realm.query<MetadataRealm>().find()
        if (results.isEmpty()) return null
        return results.last().toDomain()
    }

    private suspend fun convertRunways(): Long {
        var count = 0L
        realm.write {
            getLinesAsSequence(Files.RUNWAYS.filename) { row ->
                val code = row["airport_ident"] ?: throw NullPointerException("Row with empty ICAO code: $row")
                val airport = query<AirportRealm>("icao = '$code'").first().find()
                if (airport != null) {
                    airport.runways += RunwayRealm().apply {
                        lengthFeet = row["length_ft"]?.toIntOrNull() ?: 0
                        widthFeet = row["width_ft"]?.toIntOrNull() ?: 0
                        surface = row["surface"] ?: "GRASS"
                        closed = row["closed"]?.toBooleanStrictOrNull() ?: false
                        lowNumber = row["le_ident"] ?: "36"
                        lowElevationFeet = row["le_elevation_ft"]?.toIntOrNull() ?: 0
                        lowHeading = row["le_heading_degT"]?.toIntOrNull() ?: 360
                        highNumber = row["he_ident"] ?: "36"
                        highElevationFeet = row["he_elevation_ft"]?.toIntOrNull() ?: 0
                        highHeading = row["he_heading_degT"]?.toIntOrNull() ?: 360
                    }
                    count++
                } else {
                    println("Airport with icao $code not in database. Runways skipped.")
                }
            }
        }
        return count
    }

    private suspend fun convertFrequencies(): Long {
        var count = 0L
        realm.write {
            getLinesAsSequence(Files.FREQUENCIES.filename) { row ->
                val code = row["airport_ident"] ?: throw NullPointerException("Row with empty ICAO code: $row")
                val airport = query<AirportRealm>("icao = '$code'").first().find()
                if (airport != null) {
                    airport.frequencies += FrequencyRealm().apply {
                        type = row["type"] ?: "TWR"
                        description = row["description"] ?: ""
                        valueMhz = row["frequency_mhz"]?.toFloatOrNull() ?: 0.0f
                    }
                    count++
                } else {
                    println("Airport with icao $code not in database. Frequency skipped.")
                }
            }
        }
        return count
    }
}


