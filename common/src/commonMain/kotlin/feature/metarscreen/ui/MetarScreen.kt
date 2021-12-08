package feature.metarscreen.ui

import androidx.compose.animation.*
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import feature.metarscreen.MetarScanner
import feature.metarscreen.MetarUiEvent
import feature.metarscreen.WindViewState
import feature.metarscreen.ui.windSegment.WindSegment
import ui.AdaptiveLayout
import ui.Dialog

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MetarScreen(component: MetarScanner) {

    val state: WindViewState by component.state.subscribeAsState()

    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    state.error?.let {
        LaunchedEffect(key1 = scaffoldState.snackbarHostState) {
            scaffoldState.snackbarHostState.showSnackbar(
                message = it,
                actionLabel = "Close"
            )
        }
    }

    if (state.showInfo) {
        Dialog(
            onDismissRequest = { component.onEvent(MetarUiEvent.DismissInfoDialog) },
            title = "Metar scan",
            text = """
        This feature shows wind direction selected manually or fetched from METAR service.
        This may help you to choice preferred runway at airport for landing or take-off 
        (as you know, you should landing or taking-off with headwind).
        To change direction manually move the slider to needed wind direction.
        To fetch METAR info enter an ICAO of station or airport and press Submit or Done button on keyboard.
        Someday feature will show you other METAR info in visual form.
            """.trimIndent()
        )
    }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = { Text("Metar scan") },
                navigationIcon = {
                    IconButton(onClick = component.onBack) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back button")
                    }
                },
                actions = {
                    IconButton(onClick = { component.onEvent(MetarUiEvent.ShowInfoDialog) }) {
                        Icon(imageVector = Icons.Default.Info, contentDescription = "Information")
                    }
                }
            )
        }
    ) {
        AdaptiveLayout { width, height ->
            WindSegment(min(width, height), state.data.metarAngle ?: state.data.userAngle)

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                WindSlider(state.data.metarAngle ?: state.data.userAngle) { value ->
                    component.onEvent(MetarUiEvent.SubmitAngle(value))
                }

                IcaoInput(state.isLoading.not()) { icao ->
                    component.onEvent(MetarUiEvent.SubmitICAO(icao.uppercase(), scope))
                }

                AnimatedVisibility(
                    visible = state.airport != null,
                    enter = fadeIn() + slideInVertically(),
                    exit = fadeOut() + slideOutVertically(),
                    modifier = Modifier.padding(8.dp)
                ) {
                    state.airport?.let { AirportInfo(it) }
                }

                AnimatedVisibility(
                    visible = state.data.rawMetar.isNotBlank() || state.data.rawTaf.isNotBlank(),
                    enter = fadeIn() + slideInVertically(),
                    exit = fadeOut() + slideOutVertically(),
                    modifier = Modifier.padding(8.dp)
                ) {
                    MetarInfo(state.data)
                }
            }
        }
    }
}