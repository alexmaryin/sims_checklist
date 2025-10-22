package commonUi.components

import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun ToggleableText(
    text: String,
    isToggled: Boolean,
    modifier: Modifier = Modifier,
    textStyle: TextStyle? = null
) = Text(
    text = text,
    modifier = modifier,
    color = if (isToggled) MaterialTheme.colorScheme.inverseOnSurface else MaterialTheme.colorScheme.onSurface,
    style = LocalTextStyle.current.merge(
        textStyle ?: TextStyle(
            fontWeight = FontWeight.Normal,
            fontSize = 18.sp
        )
    )
)