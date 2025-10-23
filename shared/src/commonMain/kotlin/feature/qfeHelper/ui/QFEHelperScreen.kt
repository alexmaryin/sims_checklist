package feature.qfeHelper.ui

import alexmaryin.metarkt.helpers.toCorrectedQnh
import alexmaryin.metarkt.helpers.toIsaQnh
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import feature.qfeHelper.QFEEvent
import feature.qfeHelper.QFEHelper
import feature.qfeHelper.QFEHelperState
import commonUi.utils.SimColors
import commonUi.utils.mySnackbarHost
import org.jetbrains.compose.resources.painterResource
import sims_checklist.shared.generated.resources.Res
import sims_checklist.shared.generated.resources.arrow_back

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QFEHelperScreen(component: QFEHelper) {

    val state: State<QFEHelperState> = component.state.subscribeAsState()

    val snackbarHostState = remember { SnackbarHostState() }

    state.value.error?.let {
        LaunchedEffect(snackbarHostState) {
            snackbarHostState.showSnackbar(
                message = it,
                actionLabel = "Close"
            )
        }
    }

    Scaffold(
        snackbarHost = mySnackbarHost(snackbarHostState),
        topBar = {
            TopAppBar(
                title = { Text("QFE Helper") },
                navigationIcon = {
                    IconButton(onClick = component.onBack) {
                        Icon(painter = painterResource(Res.drawable.arrow_back), contentDescription = "Back button")
                    }
                },
                colors = SimColors.topBarColors()
            )
        },
        contentWindowInsets = WindowInsets.safeDrawing
    ) { paddingValues ->
        Column(
            modifier = Modifier.fillMaxWidth().padding(paddingValues).verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            AirportBlock(
                icao = state.value.airportICAO,
                airportName = state.value.airportName,
            ) { new -> component.onEvent(QFEEvent.SubmitICAO(new)) }

            RunwaysBlock(state.value.runways) { runwayUi ->
                component.onEvent(QFEEvent.SelectRunway(runwayUi))
            }

            ElevationBlock(
                meters = state.value.elevationMeters,
                feet = state.value.elevationFeet
            ) { new -> component.onEvent(QFEEvent.SubmitElevationMeters(new)) }

            TemperatureBlock(celsius = state.value.temperature){
                component.onEvent(QFEEvent.SubmitTemperature(it))
            }

            QFEBlock(
                qfe = state.value.qfe,
                qfeIsa = state.value.qfe.toIsaQnh(state.value.elevationMeters),
                qfeCorrected = state.value.qfe.toCorrectedQnh(state.value.elevationMeters, state.value.temperature),
            ) { new -> component.onEvent(QFEEvent.SubmitQFEmmHg(new)) }

            HeightBlock(
                metersAboveAirport = state.value.heightPlusMeters,
                feetAboveSea = state.value.heightAboveSea
            ) { new -> component.onEvent(QFEEvent.SubmitHeightPlusMeters(new)) }
        }
    }
}
