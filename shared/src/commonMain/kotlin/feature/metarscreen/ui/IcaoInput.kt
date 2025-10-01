package feature.metarscreen.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun IcaoInput(
    modifier: Modifier,
    enabled: Boolean,
    onClick: (String) -> Unit
) {
    val relocationRequester = remember { BringIntoViewRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val scope = rememberCoroutineScope()
    var icaoInput by remember { mutableStateOf("") }

    fun submitICAO() {
        keyboardController?.hide()
        onClick(icaoInput)
        icaoInput = ""
    }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = icaoInput,
            onValueChange = { new -> icaoInput = new },
            label = { Text("enter ICAO") },
            singleLine = true,
            enabled = enabled,
            keyboardActions = KeyboardActions(onDone = { submitICAO() }),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done,
                capitalization = KeyboardCapitalization.Characters,
                keyboardType = KeyboardType.Ascii
            ),
            modifier = Modifier.padding(8.dp).weight(1f)
                .bringIntoViewRequester(relocationRequester)
                .onFocusEvent {
                    if (it.isFocused) {
                        scope.launch { delay(300); relocationRequester.bringIntoView() }
                    }
                }
        )

        Button(
            onClick = { submitICAO() },
            enabled = enabled
        ) {
            if (enabled.not()) {
                CircularProgressIndicator()
            } else {
                Text("Submit")
            }
        }
    }
}