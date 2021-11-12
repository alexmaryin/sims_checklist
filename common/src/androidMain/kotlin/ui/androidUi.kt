package ui

import android.graphics.BitmapFactory
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.core.graphics.drawable.toBitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.alexmaryin.simschecklist.AppAndroid
import ru.alexmaryin.simschecklist.common.R

@Composable
actual fun loadXmlPicture(filename: String): ImageVector = ImageVector.vectorResource(
        AppAndroid.instance().resources.getIdentifier(filename, "drawable", null)
    )

@Composable
actual fun modifierForWindFace(): Modifier = Modifier.fillMaxWidth()

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

actual suspend fun loadAircraftPhoto(filename: String): Painter =  with (AppAndroid.instance()) {
    BitmapPainter(resources.getDrawable(R.drawable.cessna172).toBitmap().asImageBitmap())
}


