package feature.airportsBase.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import services.airportService.model.Frequency
import commonUi.utils.largeWithShadow

@Composable
fun FrequencyInfo(frequency: Frequency, modifier: Modifier) {
    FlowRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.Start,
        itemVerticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = frequency.type.csv, fontSize = 20.sp)
        Spacer(Modifier.width(16.dp))

        Text(text = frequency.valueMhz.toString(), style = largeWithShadow())

        frequency.description?.let {
            Spacer(Modifier.width(16.dp))
            Text(it)
        }
    }
}