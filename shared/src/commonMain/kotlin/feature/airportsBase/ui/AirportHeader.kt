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
import commonUi.utils.largeWithShadow
import org.jetbrains.compose.resources.painterResource
import sims_checklist.shared.generated.resources.Res
import sims_checklist.shared.generated.resources.air
import sims_checklist.shared.generated.resources.compress

@Composable
fun AirportHeader(
    modifier: Modifier = Modifier,
    icao: String,
    name: String,
    onMetarClick: () -> Unit,
    onQfeClick: () -> Unit
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = icao, modifier = Modifier.requiredWidth(90.dp), style = largeWithShadow())
        Text(text = name, modifier = Modifier.weight(1f))
        IconButton(onClick = onMetarClick) {
            Icon(painter = painterResource(Res.drawable.air), contentDescription = "Open Metar for airport")
        }
        IconButton(onClick = onQfeClick) {
            Icon(painter = painterResource(Res.drawable.compress), contentDescription = "Open QFE helper for airport")
        }
    }
}