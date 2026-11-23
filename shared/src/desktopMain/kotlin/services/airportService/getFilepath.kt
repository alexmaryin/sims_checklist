package services.airportService

import services.storage.getAppDataDir
import java.io.File

actual fun getFilePath(filename: String): String =
    File(getAppDataDir(), filename).absolutePath
