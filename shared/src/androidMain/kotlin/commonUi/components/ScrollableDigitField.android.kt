package commonUi.components

import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import kotlin.math.abs
import kotlin.math.sign

const val dragMargin = 2

actual fun Modifier.digitScrollModifier(onChange: (Int) -> Unit): Modifier = pointerInput(Unit) {
    var dragAccumulator = 0f
    detectVerticalDragGestures(
        onDragStart = { dragAccumulator = 0f },
        onVerticalDrag = { change, dragAmount ->
            change.consume()
            dragAccumulator += dragAmount
            val dragThreshold = dragMargin.dp.toPx()
            if (abs(dragAccumulator) > dragThreshold) {
                onChange(sign(-dragAccumulator).toInt())
                dragAccumulator = 0f
            }
        }
    )
}
