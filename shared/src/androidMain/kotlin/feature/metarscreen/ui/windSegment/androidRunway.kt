package feature.metarscreen.ui.windSegment

import androidx.compose.foundation.gestures.Orientation

actual fun runwayScrollOrientation() = Orientation.Horizontal

actual fun deltaScroll(delta: Float) = -delta / 10