package feature.metarscreen.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import feature.metarscreen.model.MetarUi

@Composable
fun MetarInfo(info: MetarUi) {
    Column {
        Text(
            text = "METAR: ${info.rawMetar}",
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.secondary
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "TAF: ${info.rawTaf}",
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.secondary
        )
    }
}