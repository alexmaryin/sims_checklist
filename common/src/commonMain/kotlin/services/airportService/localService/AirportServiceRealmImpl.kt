package services.airportService.localService

import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import services.airportService.AirportService
import services.airportService.model.Airport
import services.airportService.model.Frequency
import services.airportService.model.Runway
import services.airportService.model.realm.AirportRealm
import services.airportService.model.realm.toDomain
import services.commonApi.ErrorType
import services.commonApi.Result

class AirportServiceRealmImpl(
    private val realm: Realm
) : AirportService {
    override suspend fun getAirportByICAO(icao: String): Result<Airport> {
        val search = realm.query<AirportRealm>("icao = '$icao'").find()
        return if (search.size > 0) {
            Result.Success(search.first().toDomain())
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
}