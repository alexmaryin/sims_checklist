package services.airportService

import services.commonApi.Result
import services.airportService.model.Airport
import services.airportService.model.Frequency
import services.airportService.model.HistoryAirport
import services.airportService.model.Runway

interface AirportService {
    suspend fun getAirportByICAO(icao: String): Result<Airport>
    suspend fun getRunwaysByICAO(icao: String): Result<List<Runway>>
    suspend fun getFrequenciesByICAO(icao: String): Result<List<Frequency>>
    suspend fun getAirportsHistory(): Result<List<HistoryAirport>>
}