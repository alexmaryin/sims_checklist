package services.storage

import java.io.File

actual fun getAppDataDir(): File {
    val os = System.getProperty("os.name")?.lowercase() ?: ""
    val userHome = System.getProperty("user.home")
    val dir = when {
        os.contains("win") -> File(System.getenv("APPDATA"), "SimsChecklist")
        os.contains("mac") -> File(userHome, "Library/Application Support/SimsChecklist")
        else -> File(userHome, ".local/share/SimsChecklist")
    }
    if (!dir.exists()) dir.mkdirs()
    return dir
}
