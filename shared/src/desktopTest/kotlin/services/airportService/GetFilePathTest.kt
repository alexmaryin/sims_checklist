package services.airportService

import services.storage.getAppDataDir
import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals

class GetFilePathTest {

    @Test
    fun `update files resolve inside the app data root`() {
        val path = getFilePath("x.csv")
        assertEquals(File(getAppDataDir(), "x.csv").absolutePath, path)
    }
}
