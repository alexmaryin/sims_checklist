package feature.metarscreen.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import commonUi.components.SubmitField

@Composable
fun IcaoInput(
    modifier: Modifier,
    isLoading: Boolean,
    onClick: (String) -> Unit
) {
    val icaoInput = rememberTextFieldState("")

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        SubmitField(
            fieldState = icaoInput,
            label = "ICAO",
            uppercase = true,
            isLoading = isLoading
        ) {
            onClick(icaoInput.text.toString())
            icaoInput.clearText()
        }
    }
}