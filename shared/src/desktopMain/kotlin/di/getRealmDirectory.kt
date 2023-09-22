package di

import java.nio.file.Files
import java.nio.file.Path

@Suppress("NewApi")
actual fun getRealmDirectory(): String {
    val dir = "${System.getProperty("user.home")}/.simsChecklists/realm"
    if (Files.notExists(Path.of(dir))) Files.createDirectories(Path.of(dir))
    return dir
}