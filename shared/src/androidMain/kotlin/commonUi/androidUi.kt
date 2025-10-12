package commonUi

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import commonUi.utils.SimColors.buttonColors

@Composable
actual fun Dialog(onDismissRequest: () -> Unit, title: String, text: String) = AlertDialog(
    onDismissRequest = onDismissRequest,
    title = { Text(title) },
    text = { Text(text) },
    confirmButton = {
        Button(
            onClick = { onDismissRequest() },
            colors = buttonColors()
        ) { Text("Close") }
    }
)


