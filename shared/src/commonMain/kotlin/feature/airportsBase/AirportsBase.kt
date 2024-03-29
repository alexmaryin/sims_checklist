package feature.airportsBase

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.update
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import services.airportService.LocalBaseConverter
import services.airportService.updateService.AirportUpdateService
import java.nio.file.Paths
import java.text.SimpleDateFormat
import java.util.*

class AirportsBase(
    private val onBack: () -> Unit
) : KoinComponent, AirportEventExecutor {

    private val updateService: AirportUpdateService by inject()
    private val realmConverter: LocalBaseConverter by inject()

    override val state = MutableValue(AirportsBaseViewState())

    override fun invoke(event: AirportsUiEvent) {
        when (event) {
            AirportsUiEvent.Back -> onBack()
            AirportsUiEvent.SnackBarClose -> state.update { it.copy(snackbar = null) }
            is AirportsUiEvent.StartUpdate -> event.scope.onStartUpdate()
            is AirportsUiEvent.GetLastUpdate -> event.scope.onLastUpdate()
        }
    }

    private fun CoroutineScope.onStartUpdate() = launch {
        println("Start update")
        println("Working dir is ${Paths.get("").toAbsolutePath()}")
        state.update { it.copy(updating = true) }
        updateService.updateFlow(this).collect { result ->
            when (result) {
                is AirportUpdateService.UpdateResult.Progress -> {
                    state.update { it.copy(processingFile = result.file, progress = result.percent) }
                }

                is AirportUpdateService.UpdateResult.Success -> {
                    val timeString = Calendar.getInstance(Locale.getDefault()).let {
                        it.timeInMillis = result.lastUpdate
                        SimpleDateFormat("dd MMMM yyyy, HH:mm:ss", Locale.getDefault()).format(it.time)
                    }
                    state.update { it.copy(lastUpdate = timeString) }
                    onStartConvert()
                }

                is AirportUpdateService.UpdateResult.Error -> {
                    state.update { it.copy(snackbar = AirportsSnackBarState.ErrorHint(error = result.message)) }
                }
            }
        }
    }

    private fun CoroutineScope.onStartConvert() = launch {
        realmConverter.convertFiles(this).collect { result ->
            when (result) {
                is LocalBaseConverter.UpdateResult.Progress -> {
                    state.update {
                        it.copy(
                            processingLabel = result.label,
                            airportsCount = result.count
                        )
                    }
                }

                is LocalBaseConverter.UpdateResult.Success -> state.update {
                    launch(Dispatchers.IO) { updateService.clearAfterUpdate() }
                    it.copy(
                        updating = false,
                        airportsCount = result.count
                    )
                }
            }
        }
    }

    private fun CoroutineScope.onLastUpdate() = launch {
        realmConverter.getLastUpdate()?.let { result ->
            val timeString = Calendar.getInstance(Locale.getDefault()).let {
                it.timeInMillis = result.time
                SimpleDateFormat("dd MMMM yyyy, HH:mm:ss", Locale.getDefault()).format(it.time)
            }
            state.update {
                it.copy(
                    lastUpdate = timeString,
                    airportsCount = result.airports
                )
            }
        }
    }
}