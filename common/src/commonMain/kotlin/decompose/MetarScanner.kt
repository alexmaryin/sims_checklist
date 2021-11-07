package decompose

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.reduce
import feature.metarscreen.WindViewState
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

    fun submitAngle(new: Int) {
        fetchJob?.cancel().also { fetchJob = null }
        state.value = WindViewState(data = MetarUi(userAngle = new))
    }

    fun submitICAO(station: String, scope: CoroutineScope) {
        fetchJob =  scope.launch {
            // Say to Ui that loading has started
            state.reduce { it.copy(isLoading = true) }
            // Parse response for Ui
            when (val response = metarService.getMetar(station)) {
                is MetarResponse.Success -> response.data.parseMetar()?.let { metar ->
                    state.reduce {
                        it.copy(
                            data = MetarUi(
                                metarAngle = metar.windDirection,
                                metarSpeedKt = metar.windSpeedKt,
                                airport = response.data.name,
                                rawMetar = response.data.metar,
                                rawTaf = response.data.taf,
                            ),
                            isLoading = false,
                            isError = false
                        )
                    }
                } ?: run {
                    state.reduce {
                        it.copy(
                            isLoading = false,
                            isError = true,
                            error = ErrorUi(ErrorType.METAR_PARSE_ERROR)
                        )
                    }
                }
                is MetarResponse.Error -> state.reduce {
                    it.copy(
                        isLoading = false,
                        isError = true,
                        error = ErrorUi (ErrorType.SERVER_ERROR, response.body.message ?: "")
                    )
                }
            }
        }
    }

    fun showInfoDialog(show: Boolean = true) {
        state.reduce {
            it.copy(showInfo = show)
        }
    }
}