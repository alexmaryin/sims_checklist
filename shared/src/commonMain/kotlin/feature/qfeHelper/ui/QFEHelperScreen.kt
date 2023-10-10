package feature.qfeHelper.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import feature.qfeHelper.QFEEvent
import feature.qfeHelper.QFEHelper
import feature.qfeHelper.QFEHelperState
import ui.ScrollableDigitField

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
        Column {

            AirportBlock(
                icao = state.value.airportICAO,
                airportName = state.value.airportName,
            ) { new -> component.onEvent(QFEEvent.SubmitICAO(new, scope)) }

            Text("Elevation (meters)")
            ScrollableDigitField(
                value = state.value.elevationMeters,
                range = 0..10000,
            ) {
                component.onEvent(QFEEvent.SubmitElevationMeters(it))
            }

            AnimatedVisibility(
                visible = state.value.elevationMeters > 0,
                modifier = Modifier.padding(6.dp),
                enter = slideInVertically(),
                exit = slideOutVertically()
            ) {
                Text(
                    text = "${state.value.elevationFeet} feet",
                    fontSize = 12.sp,
                    color = Color.DarkGray,
                    fontStyle = FontStyle.Italic
                )
            }

            Text("QFE in mmHg")
            ScrollableDigitField(
                value = state.value.qfeMmHg,
                range = 600..1000
            ) {
                component.onEvent(QFEEvent.SubmitQFEmmHg(it))
            }

            Text(
                modifier = Modifier.padding(6.dp),
                text = "QNH: ${state.value.qnh()} hPa",
                fontSize = 12.sp,
                color = Color.DarkGray,
                fontStyle = FontStyle.Italic
            )
        }
    }
}