package commonTest

import feature.checklists.model.Aircraft
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import services.aircraftService.CatalogManifest
import sims_checklist.shared.generated.resources.Res
import kotlin.test.Test
import kotlin.test.assertEquals

class SplitCatalogTest {

    private val manifest by lazy {
        runBlocking {
            Json.decodeFromString<CatalogManifest>(
                Res.readBytes("files/aircraft/catalog.json").decodeToString()
            )
        }
    }

    @Test
    fun `manifest lists the five bundled per-aircraft files`() {
        assertEquals(
            listOf("cessna172.json", "cirrus_sr22.json", "citationx.json", "king_air_350.json", "piper_pa24.json"),
            manifest.files
        )
    }

    @Test
    fun `catalog parses with five aircraft`() = runBlocking {
        val catalog = manifest.files.map { name ->
            Json.decodeFromString<Aircraft>(Res.readBytes("files/aircraft/$name").decodeToString())
        }
        assertEquals(listOf(0, 1, 2, 3, 4), catalog.map { it.id })
        assertEquals(
            listOf(
                "Cessna 172 Skyhawk",
                "Cirrus SR22",
                "Cessna Citation X",
                "Beech King Air 350",
                "Piper PA-24-250 Comanche"
            ),
            catalog.map { it.name }
        )
    }

    @Test
    fun `each bundled file stands alone as one valid record`() = runBlocking {
        manifest.files.forEach { name ->
            val aircraft = Json.decodeFromString<Aircraft>(
                Res.readBytes("files/aircraft/$name").decodeToString()
            )
            assertEquals(name.removeSuffix(".json"), aircraft.photo)
        }
    }
}
