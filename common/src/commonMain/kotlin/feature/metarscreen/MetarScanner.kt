package feature.metarscreen

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.reduce
import feature.metarscreen.model.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import feature.remote.service.MetarService

class MetarScanner(
    private val metarService: MetarService,
    val onBack: () -> Unit
) {

    val state = MutableValue(WindViewState())

    private var fetchJob: Job? = null

    fun onEvent(event: MetarUiEvent) = when (event) {
        is MetarUiEvent.SubmitAngle -> submitAngle(event.new)
        is MetarUiEvent.SubmitICAO -> submitICAO(event.station, event.scope)
        is MetarUiEvent.ShowInfoDialog -> showInfoDialog(true)
        is MetarUiEvent.DismissInfoDialog -> showInfoDialog(false)
    }

    private fun submitAngle(new: Int) {
        fetchJob?.cancel().also { fetchJob = null }
        state.value = WindViewState(data = MetarUi(userAngle = new))
    }

    private fun submitICAO(station: String, scope: CoroutineScope) {
        fetchJob = scope.launch {
            // Say to Ui that loading has started
            state.reduce { it.copy(isLoading = true) }
            // Parse response for Ui
            when (val response = metarService.getMetar(station)) {
                is MetarResponse.Success -> {
                    state.reduce {
                        val metar = response.data.parseMetar()
                        it.copy(
                            data = MetarUi(
                                metarAngle = metar?.windDirection ?: it.data.metarAngle,
                                metarSpeedKt = metar?.windSpeedKt ?: it.data.metarSpeedKt,
                                airport = response.data.name,
                                rawMetar = response.data.metar,
                                rawTaf = response.data.taf,
                            ),
                            isLoading = false,
                            error = metar?.let { null } ?: ErrorUi(ErrorType.METAR_PARSE_ERROR, "Metar has no correct wind information")
                        )
                    }
                }
                is MetarResponse.Error -> state.reduce {
                    it.copy(
                        isLoading = false,
                        error = response.body
                    )
                }
            }
        }
    }

    private fun showInfoDialog(show: Boolean = true) {
        state.reduce {
            it.copy(showInfo = show)
        }
    }
}