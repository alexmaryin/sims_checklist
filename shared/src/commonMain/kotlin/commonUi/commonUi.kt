package commonUi

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import commonUi.utils.SimColors
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import sims_checklist.shared.generated.resources.*

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
    style = LocalTextStyle.current.merge(textStyle ?: TextStyle(fontWeight = FontWeight.Normal, fontSize = 18.sp))
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarWithClearAction(caption: String, onBack: () -> Unit, onClear: () -> Unit) = TopAppBar(
    title = { Text(caption) },
    navigationIcon = {
        IconButton(onClick = onBack) {
            Icon(painter = painterResource(Res.drawable.arrow_back), contentDescription = "Back button")
        }
    },
    actions = {
        IconButton(onClick = onClear) {
            Icon(painter = painterResource(Res.drawable.remove_done), contentDescription = "Uncheck all")
        }
    },
    colors = SimColors.topBarColors()
)

@Composable
fun ValidatorIcon(term: Boolean) {
    if (term) Icon(painter = painterResource(Res.drawable.warning), "Incorrect")
    else Icon(painter = painterResource(Res.drawable.done), "Correct")
}


@Composable
fun RelativeOutlineInput(
    modifier: Modifier = Modifier,
    value: String,
    labelText: String,
    relocationRequester: BringIntoViewRequester,
    scope: CoroutineScope,
    isErrorToggle: Boolean = false,
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
    modifier: Modifier = Modifier,
    value: String,
    labelText: String,
    isErrorToggle: Boolean = false,
    onValueChange: (String) -> Unit
) = OutlinedTextField(
    value = value,
    modifier = modifier.padding(8.dp),
    onValueChange = onValueChange,
    label = { Text(labelText) },
    isError = isErrorToggle,
    trailingIcon = { ValidatorIcon(isErrorToggle) }
)

/**
 * A composable that displays text styled as a hyperlink and opens the given [url] when clicked.
 *
 * @param text The text to display.
 * @param url The URL to open in the browser when the text is clicked.
 * @param modifier The modifier to be applied to the text.
 */
@Composable
fun LinkText(text: String, url: String, modifier: Modifier = Modifier) {
    val uriHandler = LocalUriHandler.current
    Text(
        text = text,
        modifier = modifier.clickable {
            uriHandler.openUri(url)
        },
        color = MaterialTheme.colorScheme.primary,
        textDecoration = TextDecoration.Underline
    )
}

@Composable
expect fun Dialog(onDismissRequest: () -> Unit, title: String, text: String)
