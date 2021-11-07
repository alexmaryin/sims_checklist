package ui.metarscreen

import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import decompose.MetarScanner

@Composable
fun WindSegment(boxScope: BoxWithConstraintsScope, component: MetarScanner) {
    CircleFace(boxScope = boxScope, color = Color.DarkGray)
    WindPointer(boxScope = boxScope, component, color = Color.DarkGray)
}