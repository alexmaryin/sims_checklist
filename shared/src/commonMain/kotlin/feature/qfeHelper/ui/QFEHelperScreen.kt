package feature.qfeHelper.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import feature.qfeHelper.QFEEvent
import feature.qfeHelper.QFEHelper
import feature.qfeHelper.QFEHelperState

@Composable
fun QFEHelperScreen(component: QFEHelper) {

    val state: State<QFEHelperState> = component.state.subscribeAsState()

    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    state.value.error?.let {
        LaunchedEffect(scaffoldState.snackbarHostState) {
            scaffoldState.snackbarHostState.showSnackbar(
                message = it,
                actionLabel = "Close"
            )
        }
    }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = { Text("QFE Helper") },
                navigationIcon = {
                    IconButton(onClick = component.onBack) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back button")
                    }
                }
            )
        }
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            AirportBlock(
                icao = state.value.airportICAO,
                airportName = state.value.airportName,
            ) { new -> component.onEvent(QFEEvent.SubmitICAO(new, scope)) }

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
