package feature.airportsBase.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import services.airportService.model.Airport
import ui.utils.largeWithShadow

@Composable
fun CollapsedAirport(airport: Airport, onClick: () -> Unit) {
    Card(
        Modifier.fillMaxWidth().padding(horizontal = 4.dp, vertical = 2.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(8.dp).clickable(onClick = onClick),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = airport.icao, modifier = Modifier.requiredWidth(90.dp), style = largeWithShadow())
            Text(text = airport.name)
        }
    }
}