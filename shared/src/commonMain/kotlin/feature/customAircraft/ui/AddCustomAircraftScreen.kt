package feature.customAircraft.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import commonUi.utils.SimColors
import commonUi.utils.mySnackbarHost
import feature.customAircraft.AddCustomAircraft
import org.jetbrains.compose.resources.painterResource
import sims_checklist.shared.generated.resources.Res
import sims_checklist.shared.generated.resources.arrow_back

@Composable
fun AddCustomAircraftScreen(component: AddCustomAircraft) {

    val state = component.state.subscribeAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    state.value.errorMessage?.let {
        LaunchedEffect(it) {
            snackbarHostState.showSnackbar(message = it, actionLabel = "Close")
            component(AddCustomAircraft.Event.ClearError)
        }
    }

    Scaffold(
        snackbarHost = mySnackbarHost(snackbarHostState),
        topBar = {
            TopAppBar(
                title = { Text("Add custom aircraft") },
                navigationIcon = {
                    IconButton(onClick = { component(AddCustomAircraft.Event.Back) }) {
                        Icon(painter = painterResource(Res.drawable.arrow_back), contentDescription = "Back button")
                    }
                },
                colors = SimColors.topBarColors()
            )
        },
        contentWindowInsets = WindowInsets.safeDrawing
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Select an aircraft JSON file and a JPEG icon. Both files are verified before " +
                    "anything is saved. The app replaces any id in your json to align it with existed aircraft.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            FilePickCard(
                title = "Aircraft data (JSON)",
                hint = "Look at the docs to explore a valid format.",
                fileName = state.value.jsonFileName,
                valid = state.value.jsonValid,
                errors = state.value.jsonErrors,
                onClick = { component(AddCustomAircraft.Event.PickJson) }
            )

            FilePickCard(
                title = "Aircraft icon (JPEG)",
                hint = "JPEG, max 50 KB, max 200 px per side, width and height should be equal (10% buffer).",
                fileName = state.value.imageFileName,
                valid = state.value.imageValid,
                errors = state.value.imageErrors,
                onClick = { component(AddCustomAircraft.Event.PickImage) }
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = state.value.attested,
                    onCheckedChange = { component(AddCustomAircraft.Event.ToggleAttestation) }
                )
                Text(
                    text = "I own this image or obtained it from a free source (e.g. Wikipedia)",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }

            Button(
                onClick = { component(AddCustomAircraft.Event.Save) },
                enabled = state.value.saveEnabled,
                colors = SimColors.buttonColors()
            ) {
                Text(if (state.value.isSaving) "Saving..." else "Save aircraft")
            }
        }
    }
}

@Composable
private fun FilePickCard(
    title: String,
    hint: String,
    fileName: String?,
    valid: Boolean,
    errors: List<String>,
    onClick: () -> Unit
) {
    Card(
        elevation = CardDefaults.elevatedCardElevation(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(text = title, style = MaterialTheme.typography.titleMedium)
            Text(
                text = hint,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Button(onClick = onClick, colors = SimColors.buttonColors()) {
                Text(if (fileName == null) "Select file" else "Change file")
            }
            fileName?.let { name ->
                Text(text = name, style = MaterialTheme.typography.bodySmall)
                if (valid) {
                    Text(
                        text = "Valid",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                } else {
                    errors.forEach { error ->
                        Text(
                            text = error,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        }
    }
}
