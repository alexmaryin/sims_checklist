package services.aircraftService

import kotlinx.serialization.Serializable

/** Lists the bundled per-aircraft JSON file names shipped in resources `files/aircraft/`. */
@Serializable
data class CatalogManifest(val files: List<String>)
