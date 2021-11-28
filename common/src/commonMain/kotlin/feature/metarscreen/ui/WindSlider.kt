package feature.metarscreen.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Slider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun WindSlider(
    initialValue: Int,
    onChangeFinished: (Int) -> Unit
) {
    var value by remember { mutableStateOf(initialValue) }

    Row(
        modifier = Modifier.fillMaxWidth().padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Slider(
            value = value.toFloat(),
            onValueChange = { value = it.toInt() },
            modifier = Modifier.weight(1f),
            valueRange = 1f..360f,
            steps = 360,
            onValueChangeFinished = { onChangeFinished(value) }
        )
        Text("$value°", fontSize = 24.sp)
    }
}