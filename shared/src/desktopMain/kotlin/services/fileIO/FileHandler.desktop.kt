package services.fileIO

import java.awt.FileDialog
import java.awt.Frame
import java.io.File
import java.io.FilenameFilter

actual class FileHandler actual constructor() {

    actual suspend fun openFile(mimeType: String): Pair<String, ByteArray>? {
        val dialog = FileDialog(null as Frame?, "Select file", FileDialog.LOAD)
        dialog.filenameFilter = when (mimeType) {
            "application/json" -> FilenameFilter { _, name -> name.endsWith(".json", ignoreCase = true) }
            "image/jpeg" -> FilenameFilter { _, name ->
                name.endsWith(".jpg", ignoreCase = true) || name.endsWith(".jpeg", ignoreCase = true)
            }
            else -> null
        }
        dialog.isVisible = true
        val directory = dialog.directory ?: return null
        val fileName = dialog.file ?: return null
        val selectedFile = File(directory, fileName)
        return Pair(selectedFile.absolutePath, selectedFile.readBytes())
    }
}
