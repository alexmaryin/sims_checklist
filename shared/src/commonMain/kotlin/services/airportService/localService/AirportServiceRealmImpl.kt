package services.airportService.localService

import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import services.airportService.AirportService
import services.airportService.model.Airport
import services.airportService.model.Frequency
import services.airportService.model.HistoryAirport
import services.airportService.model.Runway
import services.airportService.model.realm.AirportRealm
import services.airportService.model.realm.HistoryAirportRealm
import services.airportService.model.realm.toDomain
import services.commonApi.ErrorType
import services.commonApi.Result
import services.commonApi.forSuccess

class AirportServiceRealmImpl(
    private val realm: Realm
) : AirportService {

    private suspend fun addAirportToHistory(airport: Airport) {
        realm.write {
            // At first, delete similar airports from history
            delete(query<HistoryAirportRealm>("icao = $0", airport.icao).find())
            // then add as last one
            val new = HistoryAirportRealm().apply {
                timestamp = System.currentTimeMillis()
                icao = airport.icao
                name = airport.name
            }
            copyToRealm(new)
        }
        // check for list overflow
        val history = getAirportsHistory()
        history.forSuccess { airports ->
            if (airports.size > 10) {
                realm.write {
                    delete(query<HistoryAirportRealm>().first())
                }
            }
        }
    }

    override suspend fun getAirportByICAO(icao: String): Result<Airport> {
        val search = realm.query<AirportRealm>("icao = '$icao'").find()
        return if (search.size > 0) {
            Result.Success(search.first().toDomain()
                .also { addAirportToHistory(it) }
            )
        } else {
            Result.Error(ErrorType.BAD_REQUEST, "No airport found with ICAO $icao")
        }
    }

    override suspend fun getRunwaysByICAO(icao: String): Result<List<Runway>> {
        val search = realm.query<AirportRealm>("icao = '$icao'").find()
        return if (search.size > 0) {
            Result.Success(search.first().toDomain().runways)
        } else {
            Result.Error(ErrorType.BAD_REQUEST, "No airport found with ICAO $icao")
        }
    }

    override suspend fun getFrequenciesByICAO(icao: String): Result<List<Frequency>> {
        val search = realm.query<AirportRealm>("icao = '$icao'").find()
        return if (search.size > 0) {
            Result.Success(search.first().toDomain().frequencies)
        } else {
            Result.Error(ErrorType.BAD_REQUEST, "No airport found with ICAO $icao")
        }
    }

    override suspend fun getAirportsHistory(): Result<List<HistoryAirport>> {
        val search = realm.query<HistoryAirportRealm>().find()
        return if (search.size > 0) {
            Result.Success(search.map(HistoryAirportRealm::toDomain))
        } else {
            Result.Error(ErrorType.OTHER_CLIENT_ERROR, "Empty history list")
        }
    }
}