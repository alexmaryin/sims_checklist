package commonUi.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun TemperatureBlock(
    celsius: Int,
    onSubmit: (Int) -> Unit
) {
    Text("Temp. ℃")
    ScrollableDigitField(
        value = celsius,
        range = -60..60,
    ) { onSubmit(it) }
}