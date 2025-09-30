package services.airportService

import java.nio.file.Files
import java.nio.file.Path

actual fun getFilePath(filename: String): String {
    val tempDir = System.getProperty("java.io.tmpdir")
    val dir = Path.of(tempDir)
    if (Files.notExists(dir)) {
        Files.createDirectory(dir)
    }
    return dir.resolve(filename).toString()
}