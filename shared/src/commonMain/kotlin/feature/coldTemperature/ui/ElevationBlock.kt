package feature.coldTemperature.ui

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import commonUi.components.ScrollableDigitField

@Composable
fun ElevationBlock(
    elevation: Int,
    onSubmit: (Int) -> Unit
) {
    Text("Airport elev., ft")
    ScrollableDigitField(
        value = elevation,
        range = 0..10000,
    ) { onSubmit(it) }
}