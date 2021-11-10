package feature.metarscreen.ui

import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import feature.metarscreen.MetarScanner

@Composable
fun WindSegment(boxScope: BoxWithConstraintsScope, component: MetarScanner) {
    CircleFace(boxScope = boxScope, color = MaterialTheme.colors.onSurface)
    WindPointer(boxScope = boxScope, component, color = MaterialTheme.colors.secondary)
}