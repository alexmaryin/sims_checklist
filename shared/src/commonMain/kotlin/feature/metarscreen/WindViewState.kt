package feature.metarscreen

import feature.metarscreen.model.*
import services.airportService.model.Airport

data class WindViewState(
    val data: MetarUi = MetarUi(),
    val airport: Airport? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val showInfo: Boolean = false,
    val runway: RunwayUi = RunwayUi().withCalculatedWind(INIT_WIND_SPEED, INIT_WIND_HEADING),
)