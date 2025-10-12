package feature.qfeHelper.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.unit.dp
import feature.qfeHelper.ui.models.QFERunwayUi
import commonUi.ChipSelector
import commonUi.RunwayChip

@Composable
fun RunwaysBlock(
    runways: List<QFERunwayUi>,
    onSelect: (QFERunwayUi) -> Unit
) {
    if (runways.isEmpty()) return

    var selectorRect by remember { mutableStateOf(Rect.Zero) }

    LaunchedEffect(true) { selectorRect = Rect.Zero }

    Box(modifier = Modifier.fillMaxWidth()) {
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            runways.forEach { runwayUi ->
                RunwayChip(
                    text = runwayUi.number,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                ) { rect ->
                    selectorRect = rect
                    onSelect(runwayUi)
                }
            }
        }
        ChipSelector(selectorRect)
    }
}
