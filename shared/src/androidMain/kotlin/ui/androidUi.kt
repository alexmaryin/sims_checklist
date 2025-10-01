package ui

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
actual fun Dialog(onDismissRequest: () -> Unit, title: String, text: String) = AlertDialog(
    onDismissRequest = onDismissRequest,
    title = { Text(title) },
    text = { Text(text) },
    confirmButton = {
        Button(
            onClick = { onDismissRequest() }
        ) { Text("Close") }
    }
)


