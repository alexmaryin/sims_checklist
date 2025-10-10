package feature.mainScreen

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.update
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import com.arkivanov.essenty.lifecycle.doOnCreate
import feature.mainScreen.database.AircraftBase
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class AircraftList(
    private val componentContext: ComponentContext,
    private val onUiCommand: (event: MainScreenEvent) -> Unit,
) : KoinComponent, ComponentContext by componentContext {

    private val aircraftBase: AircraftBase by inject()
    private val scope = componentContext.coroutineScope() + SupervisorJob()
    val state = MutableValue(MainScreenViewState())

    init {
        lifecycle.doOnCreate {
            scope.launch {
                state.update { it.copy(isLoading = true) }
                aircraftBase.getAircraft()?.let { base ->
                    state.update { it.copy(aircraftList = base, isLoading = false) }
                }
            }
        }
    }

    fun onEvent(event: MainScreenEvent) = onUiCommand(event)
}