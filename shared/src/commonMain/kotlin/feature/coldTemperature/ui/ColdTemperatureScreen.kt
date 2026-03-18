package feature.coldTemperature.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import commonUi.components.SubmitField
import commonUi.utils.SimColors
import commonUi.utils.mySnackbarHost
import feature.coldTemperature.ColdTemperatureCorrector
import feature.coldTemperature.ColdTemperatureEvent
import feature.coldTemperature.ColdTemperatureState
import feature.coldTemperature.ColdTemperatureWaypoint
import org.jetbrains.compose.resources.painterResource
import sims_checklist.shared.generated.resources.Res
import sims_checklist.shared.generated.resources.arrow_back

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ColdTemperatureScreen(component: ColdTemperatureCorrector) {
    val state: State<ColdTemperatureState> = component.state.subscribeAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    var airportInput by rememberSaveable { mutableStateOf("") }
    var temperatureInput by rememberSaveable { mutableStateOf(state.value.temperatureCelsius.toString()) }
    var elevationInput by rememberSaveable { mutableStateOf(state.value.airportElevationFeet.toString()) }
    var showWaypointSheet by rememberSaveable { mutableStateOf(false) }
    var waypointNameInput by rememberSaveable { mutableStateOf("") }
    var waypointAltitudeInput by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(state.value.airportICAO) {
        val current = state.value.airportICAO.orEmpty()
        if (airportInput != current) airportInput = current
    }
    LaunchedEffect(state.value.temperatureCelsius) {
        val current = state.value.temperatureCelsius.toString()
        if (temperatureInput != current) temperatureInput = current
    }
    LaunchedEffect(state.value.airportElevationFeet) {
        val current = state.value.airportElevationFeet.toString()
        if (elevationInput != current) elevationInput = current
    }

    state.value.error?.let { message ->
        LaunchedEffect(message) {
            snackbarHostState.showSnackbar(
                message = message,
                actionLabel = "Close",
                duration = SnackbarDuration.Short
            )
            component.onEvent(ColdTemperatureEvent.ClearError)
        }
    }

    if (showWaypointSheet) {
        ModalBottomSheet(
            onDismissRequest = { showWaypointSheet = false }
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text = "Add waypoint",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(Modifier.height(12.dp))
                OutlinedTextField(
                    value = waypointNameInput,
                    onValueChange = { waypointNameInput = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Waypoint name") },
                    singleLine = true
                )
                Spacer(Modifier.height(12.dp))
                OutlinedTextField(
                    value = waypointAltitudeInput,
                    onValueChange = { waypointAltitudeInput = it.filterAllowedNumberChars() },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Altitude MSL, ft") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "Default value follows the previous point or airport elevation + 3000 ft.",
                    style = MaterialTheme.typography.bodySmall
                )
                Spacer(Modifier.height(16.dp))
                Button(
                    onClick = {
                        val altitude = waypointAltitudeInput.toIntOrNull()
                        if (altitude != null) {
                            component.onEvent(
                                ColdTemperatureEvent.AddWaypoint(
                                    name = waypointNameInput,
                                    altitudeFeet = altitude
                                )
                            )
                            showWaypointSheet = false
                            waypointNameInput = ""
                            waypointAltitudeInput = ""
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = waypointNameInput.isNotBlank() && waypointAltitudeInput.toIntOrNull() != null,
                    colors = SimColors.buttonColors()
                ) {
                    Text("Add waypoint")
                }
                Spacer(Modifier.height(24.dp))
            }
        }
    }

    Scaffold(
        snackbarHost = mySnackbarHost(snackbarHostState),
        topBar = {
            TopAppBar(
                title = { Text("Cold Temperature corrector") },
                navigationIcon = {
                    IconButton(onClick = component.onBack) {
                        Icon(painter = painterResource(Res.drawable.arrow_back), contentDescription = "Back button")
                    }
                },
                colors = SimColors.topBarColors()
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier.fillMaxSize().padding(paddingValues).padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val airportFieldState = remember(state.value.airportICAO) {
                androidx.compose.foundation.text.input.TextFieldState(airportInput)
            }

            LaunchedEffect(airportInput) {
                if (airportFieldState.text.toString() != airportInput) {
                    airportFieldState.edit {
                        replace(0, airportFieldState.text.length, airportInput)
                    }
                }
            }

            SubmitField(
                fieldState = airportFieldState,
                modifier = Modifier.fillMaxWidth(),
                label = "Airport ICAO",
                placeholder = "Enter 4 symbols, i.e. UUEE",
                isLoading = state.value.isLoading
            ) {
                airportInput = airportFieldState.text.toString().uppercase()
                component.onEvent(ColdTemperatureEvent.SubmitICAO(airportInput))
            }

            if (!state.value.airportName.isNullOrBlank()) {
                Text(
                    text = "${state.value.airportICAO} ${state.value.airportName}",
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = temperatureInput,
                    onValueChange = {
                        temperatureInput = it.filterAllowedNumberChars()
                        temperatureInput.toIntOrNull()?.let { value ->
                            component.onEvent(ColdTemperatureEvent.SubmitTemperature(value))
                        }
                    },
                    modifier = Modifier.weight(1f),
                    label = { Text("Temperature, C") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                OutlinedTextField(
                    value = elevationInput,
                    onValueChange = {
                        elevationInput = it.filterAllowedNumberChars()
                        elevationInput.toIntOrNull()?.let { value ->
                            component.onEvent(ColdTemperatureEvent.SubmitAirportElevation(value))
                        }
                    },
                    modifier = Modifier.weight(1f),
                    label = { Text("Airport elev., ft") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }

            Spacer(Modifier.height(12.dp))

            Button(
                onClick = {
                    waypointNameInput = ""
                    waypointAltitudeInput = state.value.nextWaypointAltitudeFeet.toString()
                    showWaypointSheet = true
                },
                modifier = Modifier.fillMaxWidth(),
                colors = SimColors.buttonColors()
            ) {
                Text("+ waypoint")
            }

            Spacer(Modifier.height(12.dp))

            if (state.value.waypoints.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxWidth().weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No waypoints yet. Add the first point to calculate cold temperature correction.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth().weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(state.value.waypoints) { waypoint ->
                        WaypointCard(waypoint)
                    }
                }
            }
        }
    }
}

@Composable
private fun WaypointCard(waypoint: ColdTemperatureWaypoint) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.fillMaxWidth().padding(12.dp)) {
            Text(
                text = "${waypoint.number}. ${waypoint.name}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Altitude MSL")
                Text("${waypoint.altitudeFeet} ft", fontWeight = FontWeight.Medium)
            }
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Corrected altitude")
                Text("${waypoint.correctedAltitudeFeet} ft", fontWeight = FontWeight.Bold)
            }
        }
    }
}

private fun String.filterAllowedNumberChars(): String =
    filterIndexed { index, char -> char.isDigit() || (char == '-' && index == 0) }
