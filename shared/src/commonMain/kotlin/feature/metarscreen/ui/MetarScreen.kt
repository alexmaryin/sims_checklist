package feature.metarscreen.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import androidx.compose.ui.unit.sp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import feature.metarscreen.MetarScanner
import feature.metarscreen.MetarUiEvent
import feature.metarscreen.WindViewState
import feature.metarscreen.model.WindComponent
import feature.metarscreen.model.toUi
import feature.metarscreen.ui.airportSegment.AirportInfo
import feature.metarscreen.ui.windSegment.WindSegment
import ui.AdaptiveLayout
import ui.Dialog
import ui.ScrollableDigitField

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

    LaunchedEffect(state.isLoading) {
        component.onEvent(MetarUiEvent.LoadTopLatest(scope))
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
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back button"
                        )
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
            WindSegment(
                min(width, height),
                WindComponent(
                    heading = state.data.metarAngle ?: state.data.userAngle,
                    speedKt = state.data.metarSpeedKt ?: state.data.userSpeed
                ),
                state.runway
            ) { value ->
                component.onEvent(MetarUiEvent.SubmitRunwayAngle(value))
            }

            Column {
                Row(Modifier.fillMaxWidth().padding(6.dp)) {
                    Column(
                        Modifier.weight(0.5f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("wind °", fontWeight = FontWeight.Bold)
                        ScrollableDigitField(
                            state.data.metarAngle ?: state.data.userAngle,
                            1..360,
                            fontSize = 16.sp
                        ) {
                            component.onEvent(MetarUiEvent.SubmitWindAngle(it))
                        }
                    }
                    Column(
                        Modifier.weight(0.5f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("runway °", fontWeight = FontWeight.Bold)
                        ScrollableDigitField(
                            state.runway.lowHeading,
                            1..180,
                            speed = 0.5f,
                            fontSize = 16.sp
                        ) {
                            component.onEvent(MetarUiEvent.SubmitRunwayAngle(it))
                        }
                    }
                }

                Row(Modifier.fillMaxWidth().padding(6.dp)) {
                    Column(
                        Modifier.weight(0.5f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("wind speed, Kt", fontWeight = FontWeight.Bold)
                        ScrollableDigitField(
                            state.data.metarSpeedKt ?: state.data.userSpeed,
                            0..30,
                            speed = 0.5f,
                            fontSize = 16.sp
                        ) {
                            component.onEvent(MetarUiEvent.SubmitWindSpeed(it))
                        }
                    }

                    IcaoInput(Modifier.weight(0.5f), state.isLoading.not()) { icao ->
                        component.onEvent(MetarUiEvent.SubmitICAO(icao.uppercase(), scope))
                    }
                }

                AnimatedVisibility(
                    visible = state.airport != null,
                    enter = fadeIn() + slideInVertically(),
                    exit = fadeOut() + slideOutVertically(),
                    modifier = Modifier.padding(8.dp).fillMaxWidth()
                ) {
                    state.airport?.let { airport ->
                        AirportInfo(airport) { runway ->
                            component.onEvent(MetarUiEvent.SubmitRunway(runway.toUi()))
                        }
                    }
                }

                AnimatedVisibility(
                    visible = state.runway.wind != null,
                    enter = fadeIn() + slideInVertically(),
                    exit = fadeOut() + slideOutVertically(),
                    modifier = Modifier.padding(8.dp).fillMaxWidth()
                ) {
                    state.runway.wind?.let { (lowWind, highWind) ->
                        Column(
                            modifier = Modifier.padding(8.dp).fillMaxWidth()
                                .align(Alignment.CenterHorizontally)
                        ) {
                            Text(text = "Wind for selected runway:", textAlign = TextAlign.Center)
                            Row {
                                RunwayWindInfo(state.runway.low, lowWind)
                                Spacer(modifier = Modifier.size(8.dp))
                                RunwayWindInfo(state.runway.high, highWind)
                            }
                        }
                    }
                }

                AnimatedVisibility(
                    visible = state.data.rawMetar.isNotBlank() || state.data.rawTaf.isNotBlank(),
                    enter = fadeIn() + slideInVertically(),
                    exit = fadeOut() + slideOutVertically(),
                    modifier = Modifier.padding(8.dp).fillMaxWidth()
                ) {
                    MetarInfo(state.data)
                }

                AnimatedVisibility(
                    visible = state.historyAirports.isNotEmpty(),
                    enter = fadeIn() + slideInVertically(),
                    exit = fadeOut() + slideOutVertically(),
                    modifier = Modifier.padding(8.dp).fillMaxWidth()
                ) {
                    Column {
                        Text(
                            text = "Top latest airports:",
                            modifier = Modifier.padding(5.dp),
                            fontSize = 14.sp,
                            color = MaterialTheme.colors.secondary
                        )
                        state.historyAirports.forEachIndexed { index, airport ->
                            val background = if (index % 2 == 0) MaterialTheme.colors.surface else Color.Transparent
                            Text(
                                text = "${airport.icao} : ${airport.name}",
                                fontSize = 18.sp,
                                color = MaterialTheme.colors.primary,
                                modifier = Modifier.fillMaxWidth().background(background).padding(5.dp).clickable {
                                    component.onEvent(MetarUiEvent.SubmitICAO(airport.icao, scope))
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}