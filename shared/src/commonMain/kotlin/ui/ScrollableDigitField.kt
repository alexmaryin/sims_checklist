package ui

import androidx.compose.animation.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
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

    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {

        ContinuedButton(
            onClick = { onChange(value - 1) },
            enabled = value > range.first,
        ) {
            Text(
                text = "-",
                fontWeight = fontWeight,
                fontSize = fontSize
            )
        }

        AnimatedContent(
            targetState = value,
            transitionSpec = {
                if (isUp) {
                    (slideInVertically { height -> height } + fadeIn())
                        .togetherWith(slideOutVertically { height -> -height / 2 } + fadeOut())
                } else {
                    (slideInVertically { height -> -height } + fadeIn())
                        .togetherWith(slideOutVertically { height -> height / 2 } + fadeOut())
                }.using(
                    SizeTransform(clip = false)
                )
            }
        ) { target ->
            Text(
                text = "$target",
                modifier = Modifier
                    .padding(horizontal = 6.dp)
                    .digitScrollModifier { delta ->
                        isUp = delta > 0
                        onChange((value + delta).coerceIn(range))
                    },
                fontWeight = fontWeight,
                fontSize = fontSize
            )
        }

        ContinuedButton(
            onClick = { onChange(value + 1) },
            enabled = value < range.last,
        ) {
            Text(
                text = "+",
                fontWeight = fontWeight,
                fontSize = fontSize
            )
        }

    }
}