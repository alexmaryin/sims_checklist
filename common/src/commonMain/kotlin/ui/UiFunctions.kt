package ui

import androidx.compose.foundation.Image
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun <T> AsyncImage(
    loader: suspend () -> T,
    painterFor: @Composable (T) -> Painter,
    contentDescription: String,
    modifier: Modifier = Modifier
) {
    val image: T? by produceState<T?>(null) {
        value = loader()
    }
    image?.let {
        Image(painter = painterFor(it), contentDescription = contentDescription, modifier = modifier)
    }
}

@Composable
fun ToggableText(
    text: String,
    isToggled: Boolean,
    modifier: Modifier = Modifier,
    textStyle: TextStyle? = null
) = Text(
    text = text,
    modifier = modifier,
    color = if (isToggled) MaterialTheme.colors.onSecondary else MaterialTheme.colors.onSurface,
    style = LocalTextStyle.current.merge(textStyle ?: TextStyle(fontWeight = FontWeight.Normal, fontSize = 18.sp))
)

@Composable
fun TopBarWithClearAction(caption: String, onBack: () -> Unit, onClear: () -> Unit) = TopAppBar(
    title = { Text(caption) },
    navigationIcon = {
        IconButton(onClick = onBack) {
            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back button")
        }
    },
    actions = {
        IconButton(onClick = onClear) {
            Icon(imageVector = Icons.Default.Clear, contentDescription = "Uncheck all")
        }
    }
)

@Composable
fun ValidatorIcon(term: Boolean) {
    if (term) Icon(Icons.Default.Warning, "Incorrect")
    else Icon(Icons.Default.Done, "Correct")
}