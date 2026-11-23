package services.storage

import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class AppDataDirTest {

    @Test
    fun `app data root follows OS conventions and exists`() {
        val dir = getAppDataDir()
        val os = System.getProperty("os.name")?.lowercase() ?: ""
        val userHome = System.getProperty("user.home")
        val expected = when {
            os.contains("win") -> File(System.getenv("APPDATA"), "SimsChecklist")
            os.contains("mac") -> File(userHome, "Library/Application Support/SimsChecklist")
            else -> File(userHome, ".local/share/SimsChecklist")
        }
        assertEquals(expected.absolutePath, dir.absolutePath)
        assertTrue(dir.exists() && dir.isDirectory, "app data root must be created on demand")
    }

    @Test
    fun `aircraft directory lives under the app data root and is created on demand`() {
        val dir = aircraftDir()
        assertEquals(File(getAppDataDir(), "aircraft").absolutePath, dir.absolutePath)
        assertTrue(dir.exists() && dir.isDirectory)
    }
}
