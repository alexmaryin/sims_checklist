package di

import java.io.File
import java.nio.file.Files
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class DbMigrationTest {

    private fun tempDir(): File = Files.createTempDirectory("sims-db-test").toFile()

    @Test
    fun `fresh install creates no file and returns the new root path`() {
        val root = tempDir()
        val legacy = File(root, "legacy").apply { mkdirs() }
        val target = resolveAirportsDb(File(root, "app"), legacy)
        assertEquals(File(root, "app/airports.db").absolutePath, target.absolutePath)
        assertFalse(target.exists(), "Room creates the database itself")
        assertFalse(File(legacy, "airports.db").exists())
    }

    @Test
    fun `legacy database is moved with content intact and legacy folder removed`() {
        val root = tempDir()
        val legacy = File(root, "legacy").apply { mkdirs() }
        val content = "legacy-db-content".toByteArray()
        File(legacy, "airports.db").writeBytes(content)
        val appRoot = File(root, "app")

        val target = resolveAirportsDb(appRoot, legacy)

        assertTrue(target.exists())
        assertContentEquals(content, target.readBytes())
        assertFalse(File(legacy, "airports.db").exists())
        assertFalse(legacy.exists(), "empty legacy folder must be removed")
    }

    @Test
    fun `database already in new root wins and legacy is left untouched`() {
        val root = tempDir()
        val legacy = File(root, "legacy").apply { mkdirs() }
        File(legacy, "airports.db").writeBytes("old".toByteArray())
        val appRoot = File(root, "app").apply { mkdirs() }
        val existing = File(appRoot, "airports.db").apply { writeBytes("new".toByteArray()) }

        val target = resolveAirportsDb(appRoot, legacy)

        assertEquals(existing.absolutePath, target.absolutePath)
        assertContentEquals("new".toByteArray(), target.readBytes())
        assertTrue(File(legacy, "airports.db").exists(), "legacy must be left untouched")
    }

    @Test
    fun `failed migration keeps legacy and returns the new path`() {
        val root = tempDir()
        val legacy = File(root, "legacy").apply { mkdirs() }
        File(legacy, "airports.db").writeBytes("old".toByteArray())
        // appRoot is a regular file: neither mkdirs nor move/copy can succeed
        val appRoot = File(root, "app").apply { writeBytes("blocker".toByteArray()) }

        val target = resolveAirportsDb(appRoot, legacy)

        assertEquals(File(appRoot, "airports.db").absolutePath, target.absolutePath)
        assertFalse(target.exists())
        assertTrue(File(legacy, "airports.db").exists(), "legacy must survive a failed migration")
    }

    @Test
    fun `non-empty legacy folder is kept after migration`() {
        val root = tempDir()
        val legacy = File(root, "legacy").apply { mkdirs() }
        File(legacy, "airports.db").writeBytes("db".toByteArray())
        File(legacy, "other.txt").writeBytes("keep".toByteArray())

        resolveAirportsDb(File(root, "app"), legacy)

        assertTrue(legacy.exists())
        assertTrue(File(legacy, "other.txt").exists())
        assertFalse(File(legacy, "airports.db").exists())
    }
}
