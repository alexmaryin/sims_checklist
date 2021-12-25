package services.airportService

import io.ktor.client.*
import services.airportService.model.Airport
import services.airportService.model.Frequency
import services.airportService.model.Runway
import services.commonApi.requestFor

class AirportServiceImpl(
    private val client: HttpClient
) : AirportService {
    override suspend fun getAirportByICAO(icao: String) =
        client.requestFor<Airport>(HttpRoutes.AIRPORTS + icao)

    override suspend fun getRunwaysByICAO(icao: String) =
        client.requestFor<List<Runway>>(HttpRoutes.RUNWAYS + icao)

    override suspend fun getFrequenciesByICAO(icao: String) =
        client.requestFor<List<Frequency>>(HttpRoutes.FREQUENCIES + icao)
}