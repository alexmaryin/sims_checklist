package feature.customAircraft

import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import feature.mainScreen.database.AircraftBaseImpl
import kotlinx.coroutines.runBlocking
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import repository.AircraftRepository
import repository.AircraftRepositoryImpl
import services.aircraftService.CUSTOM_INDEX
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import java.io.File
import java.nio.file.Files
import javax.imageio.ImageIO
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class AddCustomAircraftTest {

    private val userJson = """
        {
          "id": 99,
          "name": "Import Test Jet",
          "performance": { "fuelCapacity": 200, "averageCruiseSpeed": 300, "averageFuelFlow": 80 },
          "checklists": [
            { "id": 0, "caption": "ENGINE START", "items": [ { "caption": "Battery", "action": "ON" } ] }
          ],
          "photo": "user_label"
        }
    """.trimIndent()

    private lateinit var dir: File
    private lateinit var repository: AircraftRepository
    private lateinit var component: AddCustomAircraft
    private var backCalled = false

    private fun validJpeg(): ByteArray {
        val image = BufferedImage(150, 150, BufferedImage.TYPE_INT_RGB)
        val out = ByteArrayOutputStream()
        ImageIO.write(image, "jpg", out)
        return out.toByteArray()
    }

    private fun pngBytes(): ByteArray {
        val image = BufferedImage(150, 150, BufferedImage.TYPE_INT_RGB)
        val out = ByteArrayOutputStream()
        ImageIO.write(image, "png", out)
        return out.toByteArray()
    }

    @BeforeTest
    fun setup() {
        dir = Files.createTempDirectory("add-aircraft-test").toFile()
        repository = AircraftRepositoryImpl(AircraftBaseImpl { dir })
        startKoin {
            modules(module {
                single<AircraftRepository> { repository }
            })
        }
        backCalled = false
        component = AddCustomAircraft(
            componentContext = DefaultComponentContext(LifecycleRegistry()),
            onBack = { backCalled = true },
            aircraftDirProvider = { dir }
        )
    }

    @AfterTest
    fun teardown() {
        stopKoin()
    }

    @Test
    fun `initial state has empty controls and disabled save`() {
        val state = component.state.value
        assertEquals(null, state.jsonFileName)
        assertEquals(null, state.imageFileName)
        assertFalse(state.attested)
        assertFalse(state.saveEnabled)
    }

    @Test
    fun `invalid json surfaces errors and keeps save disabled`() {
        component.onJsonPicked("bad.json", "{ broken".toByteArray())
        val state = component.state.value
        assertEquals("bad.json", state.jsonFileName)
        assertFalse(state.jsonValid)
        assertTrue(state.jsonErrors.isNotEmpty())
        assertFalse(state.saveEnabled)
    }

    @Test
    fun `invalid image surfaces errors and keeps save disabled`() {
        component.onJsonPicked("ok.json", userJson.toByteArray())
        component.onImagePicked("icon.png", pngBytes())
        val state = component.state.value
        assertTrue(state.jsonValid)
        assertFalse(state.imageValid)
        assertTrue(state.imageErrors.any { it.contains("JPEG") })
        assertFalse(state.saveEnabled)
    }

    @Test
    fun `save requires attestation even with both files valid`() {
        component.onJsonPicked("ok.json", userJson.toByteArray())
        component.onImagePicked("icon.jpg", validJpeg())
        assertFalse(component.state.value.saveEnabled)

        component(AddCustomAircraft.Event.ToggleAttestation)
        assertTrue(component.state.value.saveEnabled)

        component(AddCustomAircraft.Event.ToggleAttestation)
        assertFalse(component.state.value.saveEnabled)
    }

    @Test
    fun `save assigns next id, rewrites photo, persists both files and refreshes repository`() = runBlocking {
        component.onJsonPicked("ok.json", userJson.toByteArray())
        component.onImagePicked("icon.jpg", validJpeg())
        component(AddCustomAircraft.Event.ToggleAttestation)

        component.performSave()

        // bundled ids are 0..4, so the user's id 99 must become 5
        val saved = File(dir, "custom_5.json")
        assertTrue(saved.exists(), "custom_5.json must be persisted")
        assertTrue(File(dir, "custom_5.jpg").exists(), "custom_5.jpg must be persisted")
        assertTrue(File(dir, CUSTOM_INDEX).readLines().contains("5"))

        val storedJson = saved.readText()
        assertTrue(storedJson.contains(""""id":5""") || storedJson.contains(""""id": 5"""))
        assertTrue(storedJson.contains("custom_5"))
        assertFalse(storedJson.contains("user_label"))

        assertEquals(6, repository.getAll().size)
        assertEquals("Import Test Jet", repository.getById(5).name)
        assertEquals("custom_5", repository.getById(5).photo)
        assertTrue(backCalled, "must navigate back after save")
    }

    @Test
    fun `duplicate of a bundled aircraft name is rejected`() {
        val json = userJson.replace(""""name": "Import Test Jet"""", """"name": "  cessna 172 SKYHAWK  """")
        component.onJsonPicked("dup.json", json.toByteArray())
        val state = component.state.value
        assertFalse(state.jsonValid)
        assertTrue(state.jsonErrors.any { it.contains("already exists") && it.contains("Cessna 172 Skyhawk") })
        assertFalse(state.saveEnabled)
    }

    @Test
    fun `second import of the same aircraft is rejected as duplicate`() = runBlocking {
        component.onJsonPicked("ok.json", userJson.toByteArray())
        component.onImagePicked("icon.jpg", validJpeg())
        component(AddCustomAircraft.Event.ToggleAttestation)
        component.performSave()

        val second = AddCustomAircraft(
            componentContext = DefaultComponentContext(LifecycleRegistry()),
            onBack = {},
            aircraftDirProvider = { dir }
        )
        second.onJsonPicked("ok.json", userJson.toByteArray())
        assertFalse(second.state.value.jsonValid)
        assertTrue(second.state.value.jsonErrors.any { it.contains("already exists") })
        assertFalse(second.state.value.saveEnabled)
    }

    @Test
    fun `distinct name passes the duplicate check`() {
        component.onJsonPicked("ok.json", userJson.toByteArray())
        assertTrue(component.state.value.jsonValid)
    }

    @Test
    fun `second import gets the next id after the first`() = runBlocking {
        component.onJsonPicked("ok.json", userJson.toByteArray())
        component.onImagePicked("icon.jpg", validJpeg())
        component(AddCustomAircraft.Event.ToggleAttestation)
        component.performSave()

        val second = AddCustomAircraft(
            componentContext = DefaultComponentContext(LifecycleRegistry()),
            onBack = {},
            aircraftDirProvider = { dir }
        )
        second.onJsonPicked("ok2.json", userJson.replace("Import Test Jet", "Import Test Jet II").toByteArray())
        second.onImagePicked("icon2.jpg", validJpeg())
        second(AddCustomAircraft.Event.ToggleAttestation)
        second.performSave()

        assertTrue(File(dir, "custom_6.json").exists())
        assertTrue(File(dir, "custom_6.jpg").exists())
    }
}
