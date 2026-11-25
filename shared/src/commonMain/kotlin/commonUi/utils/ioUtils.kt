package commonUi.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.compose.resources.painterResource
import services.aircraftService.decodeImage
import services.storage.aircraftDir
import sims_checklist.shared.generated.resources.Res
import sims_checklist.shared.generated.resources.allDrawableResources
import java.io.File

/**
 * Resolves an aircraft photo: a file in the app data aircraft directory first
 * (custom aircraft icons), then the bundled drawable resource. Returns null while
 * a file icon is still decoding or when neither source exists.
 */
@Composable
fun loadAircraftJpgPhoto(name: String): Painter? {
    val filePainter by produceState<Painter?>(initialValue = null, key1 = name) {
        value = withContext(Dispatchers.IO) {
            val file = File(aircraftDir(), "$name.jpg")
            if (file.exists()) decodeImage(file.readBytes())?.bitmap?.let { BitmapPainter(it) }
            else null
        }
    }
    return filePainter ?: Res.allDrawableResources[name]?.let { painterResource(it) }
}