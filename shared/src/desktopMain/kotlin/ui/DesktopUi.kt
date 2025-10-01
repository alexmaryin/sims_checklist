package ui

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
actual fun Dialog(onDismissRequest: () -> Unit, title: String, text: String) = AlertDialog(
    onDismissRequest = onDismissRequest,
    title = { Text(title) },
    text = { Text(text, Modifier.padding(8.dp)) },
    confirmButton = {
        Button(
            onClick = { onDismissRequest() }
        ) { Text("Close") }
    },
    modifier = Modifier.padding(20.dp).size(width = 600.dp, height = 300.dp),
    backgroundColor = MaterialTheme.colorScheme.surface,
    contentColor = MaterialTheme.colorScheme.onSurface
)

