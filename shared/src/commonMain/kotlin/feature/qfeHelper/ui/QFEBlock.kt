package feature.qfeHelper.ui

import alexmaryin.metarkt.models.PressureQFE
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import commonUi.components.ScrollableDigitField

@Composable
fun QFEBlock(
    qfe: PressureQFE,
    qfeIsa: Int,
    qfeCorrected: Int,
    onSubmit: (Int) -> Unit
) {
    Text("QFE in mmHg (${qfe.milliBar} mBar)")
    ScrollableDigitField(
        value = qfe.mmHg,
        range = 400..1000
    ) { onSubmit(it) }

    Text(
        modifier = Modifier.padding(6.dp),
        text = "QNH (ISA) $qfeIsa hPa (t° corrected) $qfeCorrected hPa",
        fontSize = 16.sp,
        color = MaterialTheme.colorScheme.onSurface,
    )
}