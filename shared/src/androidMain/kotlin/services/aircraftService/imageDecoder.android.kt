package services.aircraftService

import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.asImageBitmap

actual fun decodeImage(bytes: ByteArray): DecodedImage? {
    val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size) ?: return null
    return DecodedImage(bitmap.width, bitmap.height, bitmap.asImageBitmap())
}
