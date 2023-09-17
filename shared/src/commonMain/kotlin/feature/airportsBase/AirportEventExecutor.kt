package feature.airportsBase

import com.arkivanov.decompose.value.Value

interface AirportEventExecutor {
    operator fun invoke(event: AirportsUiEvent)

    val state: Value<AirportsBaseViewState>
}