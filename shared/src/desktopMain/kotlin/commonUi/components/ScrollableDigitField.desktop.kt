package commonUi.components

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import kotlin.math.roundToInt

const val scrollSensitivity = 0.7f

@OptIn(ExperimentalComposeUiApi::class)
actual fun Modifier.digitScrollModifier(onChange: (Int) -> Unit): Modifier =
    onPointerEvent(PointerEventType.Scroll) {
        val scrollDelta = it.changes.first().scrollDelta.y
        if (scrollDelta != 0f) {
            it.changes.first().consume()
            val deltaValue = (scrollDelta * scrollSensitivity).roundToInt()
            if (deltaValue != 0) onChange(deltaValue)
        }
    }