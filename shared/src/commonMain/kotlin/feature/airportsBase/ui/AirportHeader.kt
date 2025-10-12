package feature.airportsBase.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ui.utils.MyIcons
import ui.utils.largeWithShadow

@Composable
fun AirportHeader(
    modifier: Modifier = Modifier,
    icao: String,
    name: String,
    onMetarClick: () -> Unit
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = icao, modifier = Modifier.requiredWidth(90.dp), style = largeWithShadow())
        Text(text = name, modifier = Modifier.weight(1f))
        IconButton(onClick = onMetarClick) {
            Icon(imageVector = MyIcons.Air, contentDescription = "Open Metar for airport")
        }
        IconButton(onClick = {}) {
            Icon(imageVector = MyIcons.Compress, contentDescription = "Open QFE helper for airport")
        }
    }
}