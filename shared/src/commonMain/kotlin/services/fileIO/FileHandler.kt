package services.fileIO

/**
 * Opens a platform file picker and returns the selected file's name/path and bytes,
 * or null when the user cancels. Bytes are returned so content can be validated
 * before anything is written to app storage.
 */
expect class FileHandler() {
    suspend fun openFile(mimeType: String): Pair<String, ByteArray>?
}
