package services.airportService

import services.commonApi.Result
import services.airportService.model.Airport
import services.airportService.model.Frequency
import services.airportService.model.HistoryAirport
import services.airportService.model.LastUpdate
import services.airportService.model.Runway

interface AirportService {
    suspend fun isEmpty(): Boolean
    suspend fun dropAll()
    suspend fun getLastUpdate(): LastUpdate?
    suspend fun getAirportByICAO(icao: String): Result<Airport>
    suspend fun getRunwaysByICAO(icao: String): Result<List<Runway>>
    suspend fun getFrequenciesByICAO(icao: String): Result<List<Frequency>>
    suspend fun getAirportsHistory(): Result<List<HistoryAirport>>
    suspend fun searchAirports(search: String, limit: Int = SEARCH_LIMIT, page: Int = 0): Result<List<Airport>>

    companion object {
        const val SEARCH_LIMIT = 10
    }
}