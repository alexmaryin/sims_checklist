package feature.qfeHelper.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import commonUi.SubmitField

@Composable
fun AirportBlock(
    icao: String?,
    airportName: String?,
    onSubmit: (String) -> Unit
) {
    val fieldICAO = rememberTextFieldState(icao ?: "")
    SubmitField(
        fieldState = fieldICAO,
        label = "Airport ICAO",
        placeholder = "enter 4 symbols, i.e. KJFK"
    ) {
        onSubmit(fieldICAO.text.toString())
        fieldICAO.clearText()
    }

    AnimatedVisibility(
        visible = airportName != null,
        modifier = Modifier.padding(6.dp),
        enter = slideInVertically(),
        exit = slideOutVertically()
    ) {
        Text(
            text = "$icao $airportName",
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurface,
            fontStyle = FontStyle.Italic,
            fontWeight = FontWeight.SemiBold
        )
    }
}