package feature.coldTemperature.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import commonUi.components.ScrollableDigitField

@Composable
fun FAFMDABlock(
    fafAltitude: Int,
    mdaAltitude: Int,
    onSubmitFAF: (Int) -> Unit,
    onSubmitMDA: (Int) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(24.dp),
        verticalAlignment = Alignment.Top
    ) {
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("FAF Altitude, ft")
            ScrollableDigitField(
                value = fafAltitude,
                range = 0..20000,
            ) { onSubmitFAF(it) }
        }

        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("MDA/DA Altitude, ft")
            ScrollableDigitField(
                value = mdaAltitude,
                range = 0..20000,
            ) { onSubmitMDA(it) }
        }
    }
}
