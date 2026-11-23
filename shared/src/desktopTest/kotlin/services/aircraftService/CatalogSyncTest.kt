package services.aircraftService

import java.io.File
import java.nio.file.Files
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class CatalogSyncTest {

    private val manifest = CatalogManifest(listOf("a.json", "b.json"))
    private val bundled = mapOf(
        "a.json" to "bundled-a".toByteArray(),
        "b.json" to "bundled-b".toByteArray(),
        "catalog.json" to "manifest".toByteArray()
    )

    private fun tempDir(): File = Files.createTempDirectory("sims-sync-test").toFile()

    @Test
    fun `first run copies all bundled files, manifest and version marker`() {
        val dir = tempDir()
        syncCatalog(dir, "1.0", manifest) { bundled.getValue(it) }
        assertContentEquals(bundled.getValue("a.json"), File(dir, "a.json").readBytes())
        assertContentEquals(bundled.getValue("b.json"), File(dir, "b.json").readBytes())
        assertContentEquals(bundled.getValue("catalog.json"), File(dir, "catalog.json").readBytes())
        assertEquals("1.0", File(dir, ".deployed-version").readText())
    }

    @Test
    fun `upgrade overwrites bundled copies but never custom files`() {
        val dir = tempDir()
        File(dir, "a.json").writeBytes("old-a".toByteArray())
        File(dir, "custom_5.json").writeBytes("user-data".toByteArray())
        File(dir, "custom_5.jpg").writeBytes("user-icon".toByteArray())
        File(dir, ".deployed-version").writeText("0.9")

        syncCatalog(dir, "1.0", manifest) { bundled.getValue(it) }

        assertContentEquals(bundled.getValue("a.json"), File(dir, "a.json").readBytes())
        assertContentEquals("user-data".toByteArray(), File(dir, "custom_5.json").readBytes())
        assertContentEquals("user-icon".toByteArray(), File(dir, "custom_5.jpg").readBytes())
        assertEquals("1.0", File(dir, ".deployed-version").readText())
    }

    @Test
    fun `same version skips sync entirely`() {
        val dir = tempDir()
        File(dir, "a.json").writeBytes("locally-edited".toByteArray())
        File(dir, ".deployed-version").writeText("1.0")

        syncCatalog(dir, "1.0", manifest) { error("must not read bundled files") }

        assertContentEquals("locally-edited".toByteArray(), File(dir, "a.json").readBytes())
        assertFalse(File(dir, "b.json").exists())
    }

    @Test
    fun `missing marker triggers sync even when files exist`() {
        val dir = tempDir()
        File(dir, "a.json").writeBytes("stale".toByteArray())

        syncCatalog(dir, "1.0", manifest) { bundled.getValue(it) }

        assertContentEquals(bundled.getValue("a.json"), File(dir, "a.json").readBytes())
        assertTrue(File(dir, ".deployed-version").exists())
    }
}
