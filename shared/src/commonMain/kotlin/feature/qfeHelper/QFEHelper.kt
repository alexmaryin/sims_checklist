package feature.qfeHelper

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.update
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import services.airportService.AirportService
import services.commonApi.forError
import services.commonApi.forSuccess
import kotlin.math.roundToInt

class QFEHelper(
    val onBack: () -> Unit
) : KoinComponent {

    private val airportService: AirportService by inject()

    val state = MutableValue(QFEHelperState())

    fun onEvent(event: QFEEvent) = when (event) {
        is QFEEvent.SubmitElevationMeters -> submitElevation(event.elevation)
        is QFEEvent.SubmitHeightPlusMeters -> submitHeight(event.meters)
        is QFEEvent.SubmitICAO -> submitICAO(event.icao, event.scope)
        is QFEEvent.SubmitQFEmmHg -> submitQFE(event.mmHg)
    }

    private fun submitElevation(meters: Int) {
        state.update {
            it.copy(
                elevationMeters = meters,
                elevationFeet = it.elevationFeet(meters),
                airportName = null,
                airportICAO = ""
            )
        }
    }

    private fun submitHeight(meters: Int) {
        state.update { it.copy(heightPlusMeters = meters) }
    }

    private fun submitICAO(icao: String, scope: CoroutineScope) {
        state.update { it.copy(isLoading = true) }
        scope.launch {
            val response = airportService.getAirportByICAO(icao)
            response.forSuccess { airport ->
                state.update {
                    it.copy(
                        airportICAO = icao,
                        airportName = airport.name,
                        elevationMeters = (airport.elevation / METER_FEET).roundToInt(), isLoading = false
                    )
                }
            }
            response.forError { error ->
                state.update { it.copy(isLoading = false, error = error.message) }
            }
        }
    }

    private fun submitQFE(mmHg: Int) {
        state.update { it.copy(
            qfeMmHg = mmHg,
            qfeMilliBar = it.qfeMilliBar(mmHg),
            qnh = it.qnh()
        ) }
    }
}