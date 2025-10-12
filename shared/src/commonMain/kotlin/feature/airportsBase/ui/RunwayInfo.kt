import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import services.airportService.model.Runway
import commonUi.utils.largeWithShadow

@Composable
fun RunwayInfo(runway: Runway, modifier: Modifier = Modifier) {
    FlowRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.Start,
        itemVerticalAlignment = Alignment.CenterVertically
    ) {
        if (runway.closed) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Runway Closed",
                tint = Color.Red
            )
            Spacer(Modifier.width(8.dp))
        }

        Text(text = "${runway.lowNumber}/${runway.highNumber}", style = largeWithShadow())
        Spacer(Modifier.width(16.dp))

        Text(text = runway.surface.name, style = MaterialTheme.typography.bodyMedium)
        Spacer(Modifier.width(8.dp))

        runway.lengthFeet?.let { length ->
            val dimensions = runway.widthFeet?.let { width -> "$length x ${width}ft" } ?: "${length}ft"
            Text(text = dimensions, style = MaterialTheme.typography.bodyMedium)
        }
        Spacer(Modifier.width(8.dp))

        Text(text = "HDG: ${runway.lowHeading}°/${runway.highHeading}°", style = MaterialTheme.typography.bodyMedium)
    }
}
