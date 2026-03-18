package feature.metarscreen

import alexmaryin.metarkt.models.Wind
import feature.metarscreen.model.*
import kotlinx.serialization.Serializable
import services.airportService.model.Airport
import services.airportService.model.HistoryAirport

@Serializable
data class MetarScreenViewState(
    val data: MetarUi = MetarUi(),
    val metar: MetarData? = null,
    val airport: Airport? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val showInfo: Boolean = false,
    val runway: RunwayUi = RunwayUi().withCalculatedWind(Wind(INIT_WIND_HEADING, speed = INIT_WIND_SPEED)),
    val historyAirports: List<HistoryAirport> = emptyList()
)