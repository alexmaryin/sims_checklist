package ui

import android.annotation.SuppressLint
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.core.graphics.drawable.toBitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.alexmaryin.simschecklist.AppAndroid

@Composable
actual fun loadXmlPicture(name: String): ImageVector =
    with(AppAndroid.instance()) {
        ImageVector.vectorResource(
            resources.getIdentifier(name, "drawable", packageName)
        )
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
    }
)

@SuppressLint("UseCompatLoadingForDrawables")
actual suspend fun loadAircraftJpgPhoto(name: String): Painter =
    with(AppAndroid.instance()) {
        withContext(Dispatchers.IO) {
            val drawableId = resources.getIdentifier(name, "drawable", packageName)
            val bitmap = getDrawable(drawableId)?.toBitmap()?.asImageBitmap()
            bitmap?.let { BitmapPainter(it) } ?: throw RuntimeException("Can not load image from resources: $name")
        }
    }


