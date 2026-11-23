package feature.customAircraft

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.update
import com.arkivanov.essenty.lifecycle.doOnDestroy
import feature.checklists.model.Aircraft
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import repository.AircraftRepository
import services.aircraftService.CUSTOM_INDEX
import services.aircraftService.CUSTOM_PREFIX
import services.aircraftService.validation.AircraftValidator
import services.aircraftService.validation.ImageValidator
import services.fileIO.FileHandler
import services.storage.aircraftDir
import java.io.File

class AddCustomAircraft(
    componentContext: ComponentContext,
    private val onBack: () -> Unit,
    private val aircraftDirProvider: () -> File = { aircraftDir() },
) : ComponentContext by componentContext, KoinComponent {

    data class State(
        val jsonFileName: String? = null,
        val jsonErrors: List<String> = emptyList(),
        val jsonValid: Boolean = false,
        val imageFileName: String? = null,
        val imageErrors: List<String> = emptyList(),
        val imageValid: Boolean = false,
        val attested: Boolean = false,
        val isSaving: Boolean = false,
        val errorMessage: String? = null,
    ) {
        val saveEnabled: Boolean get() = jsonValid && imageValid && attested && !isSaving
    }

    sealed class Event {
        data object PickJson : Event()
        data object PickImage : Event()
        data object ToggleAttestation : Event()
        data object Save : Event()
        data object Back : Event()
        data object ClearError : Event()
    }

    private val repository: AircraftRepository by inject()
    private val fileHandler = FileHandler()
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    val state = MutableValue(State())

    private var validatedAircraft: Aircraft? = null
    private var imageBytes: ByteArray? = null

    init {
        lifecycle.doOnDestroy { scope.cancel() }
    }

    operator fun invoke(event: Event) {
        when (event) {
            Event.PickJson -> scope.launch {
                fileHandler.openFile("application/json")?.let { (name, bytes) -> onJsonPicked(name, bytes) }
            }
            Event.PickImage -> scope.launch {
                fileHandler.openFile("image/jpeg")?.let { (name, bytes) -> onImagePicked(name, bytes) }
            }
            Event.ToggleAttestation -> state.update { it.copy(attested = !it.attested) }
            Event.Save -> if (state.value.saveEnabled) scope.launch { performSave() }
            Event.Back -> onBack()
            Event.ClearError -> state.update { it.copy(errorMessage = null) }
        }
    }

    internal fun onJsonPicked(name: String, bytes: ByteArray) {
        val result = AircraftValidator.validate(bytes.decodeToString())
        val duplicateError = result.aircraft?.let { aircraft ->
            val normalized = aircraft.name.trim().lowercase()
            repository.getAll().firstOrNull { it.name.trim().lowercase() == normalized }
                ?.let { "An aircraft named '${it.name}' already exists" }
        }
        val errors = result.errors + listOfNotNull(duplicateError)
        val valid = result.isValid && duplicateError == null
        validatedAircraft = if (valid) result.aircraft else null
        state.update { it.copy(jsonFileName = name, jsonErrors = errors, jsonValid = valid) }
    }

    internal fun onImagePicked(name: String, bytes: ByteArray) {
        val errors = ImageValidator.validate(bytes)
        imageBytes = if (errors.isEmpty()) bytes else null
        state.update { it.copy(imageFileName = name, imageErrors = errors, imageValid = errors.isEmpty()) }
    }

    internal suspend fun performSave() {
        val aircraft = validatedAircraft ?: return
        val icon = imageBytes ?: return
        state.update { it.copy(isSaving = true) }
        try {
            val saved = withContext(Dispatchers.IO) { saveAircraft(aircraft, icon) }
            repository.addAircraft(saved)
            withContext(Dispatchers.Main) { onBack() }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            state.update { it.copy(isSaving = false, errorMessage = e.message ?: "Save failed") }
        }
    }

    /** Assigns the app-managed id and photo label, then persists both files. */
    internal fun saveAircraft(aircraft: Aircraft, icon: ByteArray): Aircraft {
        val dir = aircraftDirProvider()
        val newId = (repository.getAll().map { it.id }.maxOrNull() ?: -1) + 1
        val photo = "$CUSTOM_PREFIX$newId"
        val updated = aircraft.copy(id = newId, photo = photo)
        File(dir, "$photo.json").writeText(
            AircraftValidator.strictJson.encodeToString(Aircraft.serializer(), updated)
        )
        File(dir, "$photo.jpg").writeBytes(icon)
        val index = File(dir, CUSTOM_INDEX)
        val ids = if (index.exists()) index.readLines().mapNotNull { it.trim().toIntOrNull() } else emptyList()
        index.writeText((ids + newId).joinToString("\n"))
        return updated
    }
}
