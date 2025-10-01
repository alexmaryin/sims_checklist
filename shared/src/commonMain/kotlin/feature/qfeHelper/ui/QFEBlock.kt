package feature.qfeHelper.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ui.ScrollableDigitField

@Composable
fun QFEBlock(
    mmHg: Int,
    mBar: Int,
    qnh: Int,
    onSubmit: (Int) -> Unit
) {
    Text("QFE in mmHg ($mBar mBar)")
    ScrollableDigitField(
        value = mmHg,
        range = 400..1000
    ) { onSubmit(it) }

    Text(
        modifier = Modifier.padding(6.dp),
        text = "QNH: $qnh hPa",
        fontSize = 16.sp,
        color = MaterialTheme.colorScheme.onSurface,
        fontStyle = FontStyle.Italic
    )
}