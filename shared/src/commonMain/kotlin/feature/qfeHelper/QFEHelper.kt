package feature.qfeHelper

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.update
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import services.airportService.AirportService
import services.commonApi.forError
import services.commonApi.forSuccess
import kotlin.math.roundToInt

class QFEHelper(
    private val componentContext: ComponentContext,
    val onBack: () -> Unit
) : KoinComponent, ComponentContext by componentContext {

    private val airportService: AirportService by inject()

    val state = MutableValue(QFEHelperState())

    private val scope = componentContext.coroutineScope() + SupervisorJob()

    fun onEvent(event: QFEEvent) = when (event) {
        is QFEEvent.SubmitElevationMeters -> submitElevation(event.elevation)
        is QFEEvent.SubmitHeightPlusMeters -> submitHeight(event.meters)
        is QFEEvent.SubmitICAO -> submitICAO(event.icao)
        is QFEEvent.SubmitQFEmmHg -> submitQFE(event.mmHg)
    }

    private fun submitElevation(meters: Int) {
        state.update {
            it.copy(
                elevationMeters = meters,
                airportName = null,
                airportICAO = null,
                error = null
            )
        }
    }

    private fun submitHeight(meters: Int) {
        state.update { it.copy(heightPlusMeters = meters) }
    }

    private fun submitQFE(mmHg: Int) {
        state.update { it.copy(qfeMmHg = mmHg) }
    }

    private fun submitICAO(icao: String) {
        state.update { it.copy(isLoading = true) }
        scope.launch {
            val response = airportService.getAirportByICAO(icao)
            response.forSuccess { airport ->
                state.update {
                    it.copy(
                        airportICAO = icao,
                        airportName = airport.name,
                        elevationMeters = (airport.elevation / METER_FEET).roundToInt(),
                        isLoading = false,
                        error = null
                    )
                }
            }
            response.forError { error ->
                state.update { it.copy(isLoading = false, error = error.message) }
            }
        }
    }
}