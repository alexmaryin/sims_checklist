package commonUi.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.KeyboardActionHandler
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import sims_checklist.shared.generated.resources.Res
import sims_checklist.shared.generated.resources.done
import sims_checklist.shared.generated.resources.warning

@Composable
fun ValidatedFiled(
    fieldState: TextFieldState,
    modifier: Modifier = Modifier,
    label: String = "",
    placeholder: String = "",
    isError: Boolean = false,
    focusManager: FocusManager = LocalFocusManager.current,
    onSubmit: () -> Unit
) = OutlinedTextField(
    state = fieldState,
    modifier = modifier.padding(8.dp),
    label = { Text(label) },
    placeholder = { Text(placeholder) },
    trailingIcon = { ValidatorIcon(isError) },
    lineLimits = TextFieldLineLimits.SingleLine,
    keyboardOptions = KeyboardOptions(
        keyboardType = KeyboardType.Decimal,
        imeAction = ImeAction.Send
    ),
    onKeyboardAction = KeyboardActionHandler {
        if (fieldState.text.isNotEmpty()) {
            onSubmit()
            focusManager.clearFocus()
        }
    }
)

@Composable
fun ValidatorIcon(term: Boolean) {
    if (term) Icon(painter = painterResource(Res.drawable.warning), "Incorrect")
    else Icon(painter = painterResource(Res.drawable.done), "Correct")
}