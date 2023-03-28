package services.airportService

import java.nio.file.Files
import java.nio.file.Path

@Suppress("NewApi")
actual fun getFilePath(filename: String): String {
    if (Files.notExists(Path.of("../files"))) {
        Files.createDirectory(Path.of("../files"))
    }
    return "../files/$filename"
}