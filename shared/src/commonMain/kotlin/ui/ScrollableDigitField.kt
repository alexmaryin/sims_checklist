package ui

import androidx.compose.animation.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.roundToInt

@Composable
fun ScrollableDigitField(
    value: Int,
    range: IntRange,
    speed: Float = 1f,
    fontSize: TextUnit = 24.sp,
    fontWeight: FontWeight = FontWeight.Bold,
    onChange: (Int) -> Unit
) {

    var isUp by remember { mutableStateOf(false) }
    var isEditable by remember { mutableStateOf(false) }
    var tempValue by remember { mutableStateOf("$value") }
    val density = LocalDensity.current.density * 2

    Row(verticalAlignment = Alignment.CenterVertically) {
        TextButton(
            onClick = { onChange((value - 1).coerceIn(range)); isUp = false },
            enabled = value > range.first,
            modifier = Modifier.testTag("decrease button")
        ) { Text(text = "-", fontWeight = fontWeight, fontSize = fontSize) }

        AnimatedContent(
            targetState = value,
            transitionSpec = {
                if (isUp) {
                    (slideInVertically { height -> height } + fadeIn())
                        .togetherWith(slideOutVertically { height -> -height } + fadeOut())
                } else {
                    (slideInVertically { height -> -height } + fadeIn())
                        .togetherWith(slideOutVertically { height -> height } + fadeOut())
                }.using(
                    SizeTransform(clip = false)
                )
            }
        ) { target ->
            if (isEditable) {
                TextField(
                    value = tempValue,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    onValueChange = { new -> tempValue = "${new.toIntOrNull() ?: ""}" },
                    singleLine = true,
                    keyboardActions = KeyboardActions(onDone = {
                        isEditable = false
                        onChange(tempValue.toInt().coerceIn(range))
                    })
                )
            } else {
                Text(
                    text = "$target",
                    fontWeight = fontWeight,
                    fontSize = fontSize,
                    modifier = Modifier
                        .padding(horizontal = 6.dp)
                        .scrollable(orientation = Orientation.Vertical, reverseDirection = true,
                            state = rememberScrollableState { delta ->
                                isUp = delta > 0
                                val newValue = ((delta / density * speed).roundToInt() + value).coerceIn(range)
                                onChange(newValue)
                                delta
                            })
                        .clickable { isEditable = true }
                )
            }
        }

        TextButton(
            onClick = { onChange((value + 1).coerceIn(range)); isUp = true },
            enabled = value < range.last,
            modifier = Modifier.testTag("increase button")
        ) { Text(text = "+", fontWeight = fontWeight, fontSize = fontSize) }
    }
}