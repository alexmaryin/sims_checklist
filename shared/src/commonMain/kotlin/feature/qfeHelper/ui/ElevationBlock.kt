package feature.qfeHelper.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ui.ScrollableDigitField

@Composable
fun ElevationBlock(
    meters: Int,
    feet: Int,
    onSubmit: (Int) -> Unit
) {
    Text("Elevation (meters)")
    ScrollableDigitField(
        value = meters,
        range = 0..10000,
    ) { onSubmit(it) }

    AnimatedVisibility(
        visible = meters > 0,
        modifier = Modifier.padding(6.dp),
        enter = slideInVertically(),
        exit = slideOutVertically()
    ) {
        Text(
            text = "$feet feet",
            fontSize = 16.sp,
            color = Color.DarkGray,
            fontStyle = FontStyle.Italic
        )
    }
}