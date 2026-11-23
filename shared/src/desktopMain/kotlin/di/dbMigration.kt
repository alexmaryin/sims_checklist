package di

import java.io.File
import java.nio.file.Files
import java.nio.file.StandardCopyOption

/**
 * Resolves the airports database location inside the app data root, migrating the
 * legacy `~/.simschecklist/airports.db` once when needed.
 *
 * - database already in the new root: use it, leave any legacy file untouched;
 * - only legacy exists: move it (atomic move, or copy + verify size + delete fallback);
 *   the legacy folder is removed when empty afterwards;
 * - migration fails: keep the legacy file and return the new path, so a fresh
 *   (re-downloadable) database is created instead of losing data.
 */
fun resolveAirportsDb(appRoot: File, legacyDir: File): File {
    val target = File(appRoot, "airports.db")
    if (target.exists()) return target

    val legacy = File(legacyDir, "airports.db")
    if (!legacy.exists()) return target

    try {
        if (!appRoot.exists()) appRoot.mkdirs()
        try {
            Files.move(legacy.toPath(), target.toPath(), StandardCopyOption.ATOMIC_MOVE)
        } catch (_: Exception) {
            Files.copy(legacy.toPath(), target.toPath())
            if (target.length() != legacy.length()) {
                target.delete()
                return target
            }
            legacy.delete()
        }
        if (legacyDir.isDirectory && legacyDir.list()?.isEmpty() == true) legacyDir.delete()
    } catch (_: Exception) {
        // Keep legacy intact; a fresh database will be created at the new root.
    }
    return target
}
