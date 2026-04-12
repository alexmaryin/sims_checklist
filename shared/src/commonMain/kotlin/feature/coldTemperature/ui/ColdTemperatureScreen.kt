package feature.coldTemperature.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import commonUi.components.AirportBlock
import commonUi.components.TemperatureBlock
import commonUi.utils.SimColors
import commonUi.utils.mySnackbarHost
import feature.coldTemperature.ColdTemperatureCorrector
import feature.coldTemperature.ColdTemperatureEvent
import feature.coldTemperature.ColdTemperatureState
import org.jetbrains.compose.resources.painterResource
import services.coldTemperature.ApproachSegment
import sims_checklist.shared.generated.resources.Res
import sims_checklist.shared.generated.resources.arrow_back

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ColdTemperatureScreen(component: ColdTemperatureCorrector) {
    val state: State<ColdTemperatureState> = component.state.subscribeAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    var isAddWaypointSheetVisible by remember { mutableStateOf(false) }

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

    AddWaypointSheet(
        isVisible = isAddWaypointSheetVisible,
        defaultAltitude = state.value.waypoints.lastOrNull()?.altitudeFeet ?: state.value.airportElevationFeet,
        onSubmit = { name, altitude, segment ->
            component.onEvent(ColdTemperatureEvent.AddWaypoint(name, altitude, segment))
        },
        onDismissRequest = { isAddWaypointSheetVisible = false }
    )

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
            AirportBlock(
                icao = state.value.airportICAO,
                airportName = state.value.airportName
            ) { icao -> component.onEvent(ColdTemperatureEvent.SubmitICAO(icao)) }

            Spacer(Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(24.dp),
                verticalAlignment = Alignment.Top
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    TemperatureBlock(state.value.temperatureCelsius) { temp ->
                        component.onEvent(ColdTemperatureEvent.SubmitTemperature(temp))
                    }
                }
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    ElevationBlock(state.value.airportElevationFeet) { elevation ->
                        component.onEvent(ColdTemperatureEvent.SubmitAirportElevation(elevation))
                    }
                }
            }

            Spacer(Modifier.height(12.dp))

            FAFMDABlock(
                fafAltitude = state.value.fafAltitudeFeet,
                mdaAltitude = state.value.mdaAltitudeFeet,
                onSubmitFAF = { faf -> component.onEvent(ColdTemperatureEvent.SubmitFAFAltitude(faf)) },
                onSubmitMDA = { mda -> component.onEvent(ColdTemperatureEvent.SubmitMDAAltitude(mda)) }
            )

            Spacer(Modifier.height(12.dp))

            Button(
                onClick = { isAddWaypointSheetVisible = true },
                modifier = Modifier.fillMaxWidth(),
                enabled = !state.value.airportICAO.isNullOrBlank(),
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
                        text = if (state.value.airportICAO.isNullOrBlank()) {
                            "Enter ICAO to load airport waypoints."
                        } else {
                            "No waypoints for ${state.value.airportICAO}.\nClick + waypoint to add."
                        },
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth().weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    itemsIndexed(state.value.waypoints) { idx, waypoint ->
                        WaypointCard(
                            idx = idx,
                            waypoint = waypoint,
                            onDelete = {
                                component.onEvent(ColdTemperatureEvent.DeleteWaypoint(waypoint.name))
                            }
                        )
                    }
                }
            }
        }
    }
}