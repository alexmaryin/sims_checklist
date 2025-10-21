package commonUi

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import sims_checklist.shared.generated.resources.Res
import sims_checklist.shared.generated.resources.arrow_forward

@Composable
fun SubmitField(
    fieldState: TextFieldState,
    modifier: Modifier = Modifier,
    label: String = "",
    placeholder: String = "",
    uppercase: Boolean = true,
    isLoading: Boolean = false,
    focusManager: FocusManager = LocalFocusManager.current,
    onSubmit: () -> Unit
) = OutlinedTextField(
    state = fieldState,
    modifier = modifier.padding(6.dp),
    label = { Text(label) },
    placeholder = { Text(placeholder) },
    // TODO: replace CircularProgressIndicator with LoadingIndicator after m3 version 1.5.0
    trailingIcon = {
        if (isLoading) CircularProgressIndicator() else {
            IconButton(
                onClick = { onSubmit(); focusManager.clearFocus() },
                enabled = fieldState.text.isNotEmpty()
            ) {
                Icon(
                    painter = painterResource(Res.drawable.arrow_forward),
                    contentDescription = "submit"
                )
            }
        }
    },
    lineLimits = TextFieldLineLimits.SingleLine,
    inputTransformation = if (uppercase) InputTransformation.allCaps(Locale.current) else null,
    keyboardOptions = KeyboardOptions(
        capitalization = KeyboardCapitalization.Characters,
        imeAction = ImeAction.Send
    ),
    onKeyboardAction = KeyboardActionHandler {
        if (fieldState.text.isNotEmpty()) {
            onSubmit()
            focusManager.clearFocus()
        }
    }
)