package feature.airportsBase.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import commonUi.SubmitField
import feature.airportsBase.AirportEventExecutor
import feature.airportsBase.AirportsUiEvent
import kotlinx.coroutines.flow.collectLatest
import services.airportService.model.Airport


fun LazyListScope.airportsList(
    searchString: String,
    searchResult: List<Airport>,
    expandedAirport: Airport? = null,
    isVisible: Boolean = false,
    eventsExecutor: AirportEventExecutor,
) {
    item {
        val search = rememberTextFieldState(searchString)

        LaunchedEffect(search) {
            snapshotFlow { search.text.toString() }.collectLatest {
                eventsExecutor(AirportsUiEvent.SendSearch(it))
            }
        }

        SubmitField(
            fieldState = search,
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            enabled = isVisible,
            label = "Search airport by ICAO or name",
            uppercase = false
        ) {}
    }

    items(searchResult) { item ->
        if (expandedAirport?.icao == item.icao) {
            ExpandedAirport(expandedAirport, eventsExecutor)
        } else {
            CollapsedAirport(item, eventsExecutor)
        }
    }
}