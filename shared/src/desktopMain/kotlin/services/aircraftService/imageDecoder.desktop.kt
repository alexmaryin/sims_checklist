package services.aircraftService

import androidx.compose.ui.graphics.toComposeImageBitmap
import org.jetbrains.skia.Image
import org.jetbrains.skiko.Library

actual fun decodeImage(bytes: ByteArray): DecodedImage? =
    try {
        // Idempotent; Compose loads Skiko itself in the app, but headless callers
        // (unit tests, background validation) need the native library explicitly.
        Library.load()
        val image = Image.makeFromEncoded(bytes)
        DecodedImage(image.width, image.height, image.toComposeImageBitmap())
    } catch (_: Exception) {
        null
    }
