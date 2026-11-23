package feature.mainScreen.database

import feature.checklists.model.Aircraft
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import kotlinx.serialization.json.Json
import services.aircraftService.CATALOG_RESOURCE_DIR
import services.aircraftService.CUSTOM_INDEX
import services.aircraftService.CUSTOM_PREFIX
import services.aircraftService.CatalogManifest
import services.aircraftService.MANIFEST_FILE
import services.aircraftService.syncBundledCatalog
import services.aircraftService.validation.AircraftValidator
import services.storage.aircraftDir
import sims_checklist.shared.generated.resources.Res
import java.io.File

/**
 * Loads the aircraft catalog file-by-file: bundled aircraft from their app-data copies
 * with per-file fallback to bundled resources, then user-added `custom_*` files with no
 * fallback. One bad file never aborts the catalog; skipped aircraft are reported via
 * [loadWarnings].
 */
class AircraftBaseImpl(
    private val aircraftDirProvider: () -> File = { aircraftDir() }
) : AircraftBase {

    override val loadWarnings: List<String>
        field = mutableListOf<String>()

    override suspend fun getAircraft(): List<Aircraft>? {
        loadWarnings.clear()
        return try {
            val dir = aircraftDirProvider()
            syncBundledCatalog(dir)
            val manifest = readManifest(dir)
            val result = mutableListOf<Aircraft>()

            manifest.files.forEach { name ->
                val aircraft = loadValid(File(dir, name)) ?: loadValidResource(name)
                if (aircraft != null) result += aircraft
                else loadWarnings += "Aircraft file $name is missing or invalid in both app files and resources"
            }

            indexedCustomIds(dir).forEach { id ->
                if (!File(dir, "$CUSTOM_PREFIX$id.json").exists())
                    loadWarnings += "Custom aircraft $CUSTOM_PREFIX$id skipped: file is missing"
            }
            dir.listFiles { file ->
                file.isFile && file.name.startsWith(CUSTOM_PREFIX) && file.name.endsWith(".json")
            }?.sortedBy { it.name }?.forEach { file ->
                val aircraft = loadValid(file)
                if (aircraft != null) result += aircraft
                else loadWarnings += "Custom aircraft ${file.name} skipped: file is invalid"
            }

            result.ifEmpty { null }
        } catch (_: Exception) {
            currentCoroutineContext().ensureActive()
            null
        }
    }

    private suspend fun readManifest(dir: File): CatalogManifest {
        val local = File(dir, MANIFEST_FILE)
        if (local.exists()) {
            try {
                return Json.decodeFromString(local.readText())
            } catch (_: Exception) {
                // fall through to the bundled manifest
            }
        }
        return Json.decodeFromString(
            Res.readBytes("$CATALOG_RESOURCE_DIR/$MANIFEST_FILE").decodeToString()
        )
    }

    private fun indexedCustomIds(dir: File): List<Int> {
        val index = File(dir, CUSTOM_INDEX)
        if (!index.exists()) return emptyList()
        return index.readLines().mapNotNull { it.trim().toIntOrNull() }
    }

    private fun loadValid(file: File): Aircraft? {
        if (!file.exists()) return null
        return try {
            AircraftValidator.validate(file.readText()).aircraft
        } catch (_: Exception) {
            null
        }
    }

    private suspend fun loadValidResource(name: String): Aircraft? =
        try {
            AircraftValidator.validate(
                Res.readBytes("$CATALOG_RESOURCE_DIR/$name").decodeToString()
            ).aircraft
        } catch (_: Exception) {
            currentCoroutineContext().ensureActive()
            null
        }
}
