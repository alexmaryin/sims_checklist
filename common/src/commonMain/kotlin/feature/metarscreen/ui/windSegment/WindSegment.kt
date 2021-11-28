package feature.metarscreen.ui.windSegment

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min

@Composable
fun WindSegment(minSide: Dp, angle: Int) {
    BoxWithConstraints(
        modifier = Modifier.size(min(400.dp, minSide - 50.dp)),
    ) {
        CircleFace(boxScope = this, color = MaterialTheme.colors.onSurface)
        WindPointer(boxScope = this, angle, color = MaterialTheme.colors.secondary)
    }
}