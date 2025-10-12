package feature.qfeHelper.ui

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import ui.ScrollableDigitField

@Composable
fun TemperatureBlock(
    celsius: Int,
    onSubmit: (Int) -> Unit
) {
    Text("Temperature (Celsius)")
    ScrollableDigitField(
        value = celsius,
        range = -60..60,
    ) { onSubmit(it) }
}