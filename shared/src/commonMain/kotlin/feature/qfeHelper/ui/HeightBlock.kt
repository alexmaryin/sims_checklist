package feature.qfeHelper.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import commonUi.ScrollableDigitField

@Composable
fun HeightBlock(
    metersAboveAirport: Int,
    feetAboveSea: Int,
    onSubmit: (Int) -> Unit) {
    Text("Height above airport (meters)")
    ScrollableDigitField(
        value = metersAboveAirport,
        range = 0..10000
    ) { onSubmit(it) }

    Text(
        modifier = Modifier.padding(6.dp),
        text = "Feet ASL: $feetAboveSea",
        fontSize = 16.sp,
        color = MaterialTheme.colorScheme.onSurface,
        fontStyle = FontStyle.Italic
    )
}