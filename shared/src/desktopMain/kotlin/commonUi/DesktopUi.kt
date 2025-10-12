package commonUi

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import commonUi.utils.SimColors.buttonColors

@Composable
actual fun Dialog(onDismissRequest: () -> Unit, title: String, text: String) = AlertDialog(
    onDismissRequest = onDismissRequest,
    title = { Text(title) },
    text = { Text(text, Modifier.padding(8.dp)) },
    confirmButton = {
        Button(
            onClick = { onDismissRequest() },
            colors = buttonColors()
        ) { Text("Close") }
    }
)

