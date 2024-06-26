package feature.qfeHelper.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.*

@Composable
fun AirportBlock(
    icao: String?,
    airportName: String?,
    onSubmit: (String) -> Unit
) {
    var fieldICAO by remember { mutableStateOf(icao ?: "") }

    OutlinedTextField(
        modifier = Modifier.padding(6.dp),
        label = { Text("Airport ICAO") },
        placeholder = { Text("enter 4 symbols, i.e. KJFK") },
        value = fieldICAO,
        onValueChange = { new -> fieldICAO = new },
        trailingIcon = {
            IconButton(onClick = {
                onSubmit(fieldICAO.uppercase(Locale.getDefault()))
                fieldICAO = ""
            }) {
                Icon(imageVector = Icons.AutoMirrored.Filled.Send, contentDescription = "submit")
            }
        },
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.Characters,
            imeAction = ImeAction.Send
        ),
        keyboardActions = KeyboardActions { onSubmit(fieldICAO.uppercase(Locale.getDefault())) }
    )

    AnimatedVisibility(
        visible = airportName != null,
        modifier = Modifier.padding(6.dp),
        enter = slideInVertically(),
        exit = slideOutVertically()
    ) {
        Text(
            text = "$icao $airportName",
            fontSize = 16.sp,
            color = Color.DarkGray,
            fontStyle = FontStyle.Italic,
            fontWeight = FontWeight.SemiBold
        )
    }
}