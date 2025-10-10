package feature.mainScreen

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.update
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import com.arkivanov.essenty.lifecycle.doOnCreate
import com.arkivanov.essenty.lifecycle.doOnStart
import feature.mainScreen.database.AircraftBase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import services.airportService.AirportService
import services.airportService.LocalBaseConverter
import services.airportService.updateService.AirportUpdateService

class AircraftList(
    private val componentContext: ComponentContext,
    private val onUiCommand: (event: MainScreenEvent) -> Unit,
) : MainEventExecutor, KoinComponent, ComponentContext by componentContext {

    private val aircraftBase: AircraftBase by inject()
    private val airportService: AirportService by inject()
    private val updateService: AirportUpdateService by inject()
    private val csvConverter: LocalBaseConverter by inject()

    private val scope = componentContext.coroutineScope() + SupervisorJob()
    override val state = MutableValue(MainScreenViewState())

    init {
        lifecycle.doOnCreate {
            scope.launch {
                state.update { it.copy(isLoading = true) }
                aircraftBase.getAircraft()?.let { base ->
                    state.update { it.copy(aircraftList = base, isLoading = false) }
                }
            }
        }

        lifecycle.doOnStart(true) {
            scope.launch {
                val snack = if (airportService.isEmpty())
                    MainScreenSnack.StartUpdate("Airport database is empty!")
                else airportService.getLastUpdate()?.let {
                    MainScreenSnack.Close("${it.airports} airports in database.")
                }
                state.update { it.copy(snack = snack) }
            }
        }

    }

    override fun invoke(event: MainScreenEvent) {
        when (event) {
            MainScreenEvent.ClearSnack -> state.update { it.copy(snack = null) }
            MainScreenEvent.StartUpdate -> scope.onStartUpdate()
            MainScreenEvent.DropBase -> scope.onDropBase()
            else -> onUiCommand(event)
        }
    }

    private fun CoroutineScope.onStartUpdate() = launch {
        updateService.updateFlow(this).collect {
            when (it) {
                is AirportUpdateService.UpdateResult.Progress -> {
                    state.update { old -> old.copy(updateMessage = "Downloading ${it.file}") }
                }

                is AirportUpdateService.UpdateResult.Error -> {
                    state.update { old -> old.copy(updateMessage = it.message) }
                }

                is AirportUpdateService.UpdateResult.Success -> Unit
            }
        }
        csvConverter.convertFiles(this).collect {
            when (it) {
                is LocalBaseConverter.UpdateResult.Progress -> {
                    state.update { old -> old.copy(updateMessage = "Converting ${it.label}") }
                }

                is LocalBaseConverter.UpdateResult.Success -> {
                    state.update { old ->
                        old.copy(
                            updateMessage = null,
                            snack = MainScreenSnack.Close("Updated successfully. ${it.count} airports in database.")
                        )
                    }
                }
            }
        }
        updateService.clearAfterUpdate()
    }

    private fun CoroutineScope.onDropBase() = launch {
        airportService.dropAll()
        state.update { it.copy(snack = MainScreenSnack.StartUpdate("Database dropped.")) }
    }
}