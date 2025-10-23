package feature.metarscreen.ui

import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import commonUi.components.SubmitField

@Composable
fun IcaoInput(
    isLoading: Boolean,
    onClick: (String) -> Unit
) {
    val icaoInput = rememberTextFieldState("")

    SubmitField(
        fieldState = icaoInput,
        modifier = Modifier.widthIn(max = 180.dp),
        label = "ICAO",
        uppercase = true,
        isLoading = isLoading
    ) {
        onClick(icaoInput.text.toString())
        icaoInput.clearText()
    }
}