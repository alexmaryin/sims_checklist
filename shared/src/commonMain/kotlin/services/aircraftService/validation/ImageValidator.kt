package services.aircraftService.validation

import services.aircraftService.decodeImage
import kotlin.math.abs
import kotlin.math.max

/**
 * Soft icon restrictions for custom aircraft: JPEG, at most 50 KB, at most 200 px
 * per side, width/height difference at most 10% of the larger side.
 */
object ImageValidator {

    const val MAX_SIZE_BYTES = 50 * 1024
    const val MAX_SIDE_PX = 200
    const val MAX_RATIO_DIFF = 0.10

    fun validate(bytes: ByteArray): List<String> {
        val errors = mutableListOf<String>()
        if (bytes.size > MAX_SIZE_BYTES)
            errors += "Image is larger than 50 KB (${bytes.size / 1024} KB)"
        if (bytes.size < 3 ||
            bytes[0] != 0xFF.toByte() || bytes[1] != 0xD8.toByte() || bytes[2] != 0xFF.toByte()
        ) {
            errors += "Image must be in JPEG format"
            return errors
        }
        if (errors.isNotEmpty()) return errors

        val decoded = decodeImage(bytes)
        if (decoded == null) {
            errors += "Image cannot be decoded"
            return errors
        }
        if (decoded.width > MAX_SIDE_PX || decoded.height > MAX_SIDE_PX)
            errors += "Image must be at most 200 px per side (got ${decoded.width}x${decoded.height})"
        val diff = abs(decoded.width - decoded.height)
        if (diff > MAX_RATIO_DIFF * max(decoded.width, decoded.height))
            errors += "Width and height must differ by no more than 10% (got ${decoded.width}x${decoded.height})"
        return errors
    }
}
