package ui

import androidx.compose.foundation.layout.size
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.res.loadXmlImageVector
import androidx.compose.ui.res.useResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.xml.sax.InputSource

@Composable
actual fun loadXmlPicture(name: String): ImageVector =
    useResource("drawable/$name.xml") { stream ->
        loadXmlImageVector(InputSource(stream), LocalDensity.current)
    }

@Composable
actual fun Dialog(onDismissRequest: () -> Unit, title: String, text: String) = AlertDialog(
    onDismissRequest = onDismissRequest,
    title = { Text(title) },
    text = { Text(text) },
    confirmButton = {
        Button(
            onClick = { onDismissRequest() }
        ) { Text("Close") }
    },
    modifier = Modifier.size(width = 600.dp, height = 300.dp)
)


actual suspend fun loadAircraftJpgPhoto(name: String): Painter =
    withContext(Dispatchers.IO) {
        useResource("drawable/$name.jpg") { stream ->
            BitmapPainter(loadImageBitmap(stream))
        }
    }

