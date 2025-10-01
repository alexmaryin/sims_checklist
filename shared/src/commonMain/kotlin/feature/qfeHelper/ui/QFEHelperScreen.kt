package feature.qfeHelper.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import ui.utils.SimColors
import ui.utils.mySnackbarHost

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
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back button")
                    }
                },
                colors = SimColors.topBarColors()
            )
        },
        contentWindowInsets = WindowInsets.safeDrawing
    ) { paddingValues ->
        Column(
            modifier = Modifier.fillMaxWidth().padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            AirportBlock(
                icao = state.value.airportICAO,
                airportName = state.value.airportName,
            ) { new -> component.onEvent(QFEEvent.SubmitICAO(new)) }

            ElevationBlock(
                meters = state.value.elevationMeters,
                feet = state.value.elevationFeet
            ) { new -> component.onEvent(QFEEvent.SubmitElevationMeters(new)) }

            QFEBlock(
                mmHg = state.value.qfeMmHg,
                mBar = state.value.qfeMilliBar,
                qnh = state.value.qnh
            ) { new -> component.onEvent(QFEEvent.SubmitQFEmmHg(new)) }

            HeightBlock(
                metersAboveAirport = state.value.heightPlusMeters,
                feetAboveSea = state.value.heightAboveSea
            ) { new -> component.onEvent(QFEEvent.SubmitHeightPlusMeters(new)) }
        }
    }
}
