package services.storage

import java.io.File

/**
 * Single OS-conventional per-user root for all app files.
 * Desktop: %APPDATA%/SimsChecklist (Windows), ~/Library/Application Support/SimsChecklist (macOS),
 * ~/.local/share/SimsChecklist (Linux). Android: the app's internal files directory.
 * The directory is created on demand.
 */
expect fun getAppDataDir(): File

/** Directory holding per-aircraft JSON files and custom icons, created on demand. */
fun aircraftDir(): File = File(getAppDataDir(), "aircraft").apply { if (!exists()) mkdirs() }
