package feature.airportsBase.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import services.airportService.model.Airport

@Composable
fun AirportsList(
    searchString: String,
    searchResult: List<Airport>,
    expandedAirport: Airport? = null,
    onChange: (String) -> Unit,
    onAirportClick: (String) -> Unit
) {
    val lazyState = rememberLazyListState()
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = searchString,
            onValueChange = onChange,
            keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() }),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done,
                capitalization = KeyboardCapitalization.Characters,
                keyboardType = KeyboardType.Ascii
            ),
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            label = { Text("Search airport by ICAO or name") },
            singleLine = true
        )
        LazyColumn(state = lazyState, modifier = Modifier.fillMaxWidth().padding(16.dp)) {
            items(searchResult) { item ->
                if (expandedAirport?.icao == item.icao) {
                    ExpandedAirport(expandedAirport) { onAirportClick(item.icao) }
                } else {
                    CollapsedAirport(item) { onAirportClick(item.icao) }
                }
            }
        }
    }
}