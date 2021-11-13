package ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
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
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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
        Image(
            painter = painterFor(it),
            contentDescription = contentDescription,
            modifier = modifier
        )
    }
}

@Composable
fun ToggleableText(
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


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RelativeOutlineInput(
    value: String,
    labelText: String,
    relocationRequester: BringIntoViewRequester,
    scope: CoroutineScope,
    isErrorToggle: Boolean = false,
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit
) = OutlinedTextField(
    value = value,
    modifier = modifier
        .padding(8.dp)
        .bringIntoViewRequester(relocationRequester)
        .onFocusEvent {
            if (it.isFocused) scope.launch { delay(300); relocationRequester.bringIntoView() }
        },
    onValueChange = onValueChange,
    label = { Text(labelText) },
    isError = isErrorToggle,
    trailingIcon = { ValidatorIcon(isErrorToggle) }
)

@Composable
fun ValidatedOutlineInput(
    value: String,
    labelText: String,
    isErrorToggle: Boolean = false,
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit
) = OutlinedTextField(
    value = value,
    modifier = modifier.padding(8.dp),
    onValueChange = onValueChange,
    label = { Text(labelText) },
    isError = isErrorToggle,
    trailingIcon = { ValidatorIcon(isErrorToggle) }
)

@Composable
expect fun loadXmlPicture(name: String): ImageVector

@Composable
expect fun modifierForWindFace(): Modifier

@Composable
expect fun Dialog(onDismissRequest: () -> Unit, title: String, text: String)
