package ui

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.vectorResource
import sims_checklist.shared.generated.resources.Res
import sims_checklist.shared.generated.resources.allDrawableResources
import ui.utils.MyIcons

@Composable
fun ToggleableText(
    text: String,
    isToggled: Boolean,
    modifier: Modifier = Modifier,
    textStyle: TextStyle? = null
) = Text(
    text = text,
    modifier = modifier,
    color = if (isToggled) MaterialTheme.colorScheme.onSecondary else MaterialTheme.colorScheme.onSurface,
    style = LocalTextStyle.current.merge(textStyle ?: TextStyle(fontWeight = FontWeight.Normal, fontSize = 18.sp))
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarWithClearAction(caption: String, onBack: () -> Unit, onClear: () -> Unit) = TopAppBar(
    title = { Text(caption) },
    navigationIcon = {
        IconButton(onClick = onBack) {
            Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back button")
        }
    },
    actions = {
        IconButton(onClick = onClear) {
            Icon(imageVector = MyIcons.CheckBoxOutlineBlank, contentDescription = "Uncheck all")
        }
    }
)

@Composable
fun ValidatorIcon(term: Boolean) {
    if (term) Icon(Icons.Default.Warning, "Incorrect")
    else Icon(Icons.Default.Done, "Correct")
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

@Composable
fun loadXmlPicture(name: String): ImageVector = vectorResource(Res.allDrawableResources[name]!!)

@Composable
expect fun Dialog(onDismissRequest: () -> Unit, title: String, text: String)
