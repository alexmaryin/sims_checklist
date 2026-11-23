package services.aircraftService

import androidx.compose.ui.graphics.ImageBitmap

data class DecodedImage(val width: Int, val height: Int, val bitmap: ImageBitmap)

/** Decodes encoded image bytes (JPEG/PNG/...), or returns null when undecodable. */
expect fun decodeImage(bytes: ByteArray): DecodedImage?
