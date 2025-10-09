package ui

import androidx.compose.animation.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

expect fun Modifier.digitScrollModifier(onChange: (Int) -> Unit): Modifier

@Composable
fun ScrollableDigitField(
    value: Int,
    range: IntRange,
    fontSize: TextUnit = 24.sp,
    fontWeight: FontWeight = FontWeight.Bold,
    onChange: (Int) -> Unit
) {
    var isUp by remember { mutableStateOf(false) }
    var isEditable by remember { mutableStateOf(false) }
    var tempValue by remember { mutableStateOf("$value") }

    Row(verticalAlignment = Alignment.CenterVertically) {
        TextButton(
            onClick = { onChange((value - 1).coerceIn(range)); isUp = false },
            enabled = value > range.first
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
                    onValueChange = { new -> tempValue = new.filter { it.isDigit() } },
                    singleLine = true,
                    keyboardActions = KeyboardActions(onDone = {
                        isEditable = false
                        val finalValue = tempValue.toIntOrNull() ?: value
                        onChange(finalValue.coerceIn(range))
                    })
                )
            } else {
                Box(
                    modifier = Modifier
                        .sizeIn(minWidth = 50.dp, minHeight = 50.dp)
                        .padding(horizontal = 6.dp)
                        .digitScrollModifier { delta ->
                            isUp = delta > 0
                            onChange((value + delta).coerceIn(range))
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "$target",
                        fontWeight = fontWeight,
                        fontSize = fontSize,
                        modifier = Modifier.clickable { isEditable = true }
                    )
                }
            }
        }

        TextButton(
            onClick = { onChange((value + 1).coerceIn(range)); isUp = true },
            enabled = value < range.last
        ) { Text(text = "+", fontWeight = fontWeight, fontSize = fontSize) }
    }
}