package services.aircraftService.validation

import services.aircraftService.decodeImage
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import javax.imageio.ImageIO
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class ImageValidatorTest {

    private fun jpeg(width: Int, height: Int): ByteArray {
        val image = BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
        val out = ByteArrayOutputStream()
        ImageIO.write(image, "jpg", out)
        return out.toByteArray()
    }

    private fun png(width: Int, height: Int): ByteArray {
        val image = BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
        val out = ByteArrayOutputStream()
        ImageIO.write(image, "png", out)
        return out.toByteArray()
    }

    @Test
    fun `PNG is rejected as non-JPEG`() {
        val errors = ImageValidator.validate(png(150, 150))
        assertTrue(errors.any { it.contains("JPEG") })
    }

    @Test
    fun `file larger than 50 KB is rejected`() {
        val bytes = ByteArray(60 * 1024)
        bytes[0] = 0xFF.toByte(); bytes[1] = 0xD8.toByte(); bytes[2] = 0xFF.toByte()
        val errors = ImageValidator.validate(bytes)
        assertTrue(errors.any { it.contains("50 KB") })
    }

    @Test
    fun `side longer than 200 px is rejected`() {
        val errors = ImageValidator.validate(jpeg(250, 250))
        assertTrue(errors.any { it.contains("200 px") })
    }

    @Test
    fun `proportions differing more than 10 percent are rejected`() {
        val errors = ImageValidator.validate(jpeg(150, 180))
        assertTrue(errors.any { it.contains("10%") })
    }

    @Test
    fun `150x150 JPEG within size limit is accepted`() {
        val bytes = jpeg(150, 150)
        assertTrue(bytes.size <= ImageValidator.MAX_SIZE_BYTES)
        assertTrue(ImageValidator.validate(bytes).isEmpty())
    }

    @Test
    fun `slightly non-square 152x150 JPEG is accepted`() {
        assertTrue(ImageValidator.validate(jpeg(152, 150)).isEmpty())
    }

    @Test
    fun `garbage bytes with JPEG magic are rejected as undecodable`() {
        val bytes = byteArrayOf(0xFF.toByte(), 0xD8.toByte(), 0xFF.toByte()) + ByteArray(100)
        assertTrue(ImageValidator.validate(bytes).any { it.contains("decoded") })
    }

    @Test
    fun `decoder reports dimensions of a valid JPEG`() {
        val decoded = assertNotNull(decodeImage(jpeg(152, 150)))
        assertEquals(152, decoded.width)
        assertEquals(150, decoded.height)
    }

    @Test
    fun `decoder returns null for non-image bytes`() {
        assertNull(decodeImage("not an image".toByteArray()))
    }
}
