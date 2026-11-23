package services.aircraftService

import common.BuildKonfig
import kotlinx.serialization.json.Json
import services.storage.aircraftDir
import sims_checklist.shared.generated.resources.Res
import java.io.File

const val CATALOG_RESOURCE_DIR = "files/aircraft"
const val MANIFEST_FILE = "catalog.json"
const val VERSION_MARKER = ".deployed-version"
const val CUSTOM_PREFIX = "custom_"
const val CUSTOM_INDEX = ".custom-index"

/**
 * Testable sync core: copies bundled aircraft files and the manifest into [dir]
 * when [version] differs from the recorded marker (or the marker is missing).
 * Only manifest-listed files, the manifest itself and the marker are written —
 * user-added `custom_*` files are never touched.
 */
fun syncCatalog(
    dir: File,
    version: String,
    manifest: CatalogManifest,
    readBundled: (String) -> ByteArray
) {
    val marker = File(dir, VERSION_MARKER)
    if (marker.exists() && marker.readText() == version) return
    if (!dir.exists()) dir.mkdirs()
    manifest.files.forEach { name -> File(dir, name).writeBytes(readBundled(name)) }
    File(dir, MANIFEST_FILE).writeBytes(readBundled(MANIFEST_FILE))
    marker.writeText(version)
}

/** Deletes a custom aircraft's files and removes its id from the custom index. */
fun removeCustomAircraftFiles(dir: File, id: Int) {
    File(dir, "$CUSTOM_PREFIX$id.json").delete()
    File(dir, "$CUSTOM_PREFIX$id.jpg").delete()
    val index = File(dir, CUSTOM_INDEX)
    if (index.exists()) {
        val ids = index.readLines().mapNotNull { it.trim().toIntOrNull() }.filterNot { it == id }
        index.writeText(ids.joinToString("\n"))
    }
}

/** Copies the bundled catalog from resources into [dir] (default: app data aircraft dir) when needed. */
suspend fun syncBundledCatalog(dir: File = aircraftDir()) {
    val marker = File(dir, VERSION_MARKER)
    if (marker.exists() && marker.readText() == BuildKonfig.VER) return

    val manifest = Json.decodeFromString<CatalogManifest>(
        Res.readBytes("$CATALOG_RESOURCE_DIR/$MANIFEST_FILE").decodeToString()
    )
    val bundled = (manifest.files + MANIFEST_FILE).associateWith { name ->
        Res.readBytes("$CATALOG_RESOURCE_DIR/$name")
    }
    syncCatalog(dir, BuildKonfig.VER, manifest) { name -> bundled.getValue(name) }
}
