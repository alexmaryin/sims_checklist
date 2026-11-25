package feature.mainScreen.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import common.BuildKonfig
import commonUi.utils.SimColors
import commonUi.utils.largeWithShadow
import commonUi.utils.loadAircraftJpgPhoto
import commonUi.utils.mySnackbarHost
import feature.mainScreen.MainEventExecutor
import feature.mainScreen.MainScreenEvent
import org.jetbrains.compose.resources.painterResource
import services.aircraftService.CUSTOM_PREFIX
import sims_checklist.shared.generated.resources.Res
import sims_checklist.shared.generated.resources.airplanes
import sims_checklist.shared.generated.resources.delete
import sims_checklist.shared.generated.resources.gas_station

@Composable
fun AircraftListScreen(component: MainEventExecutor) {

    val state = component.state.subscribeAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    state.value.snack?.let {
        LaunchedEffect(key1 = state.value.snack) {
            val action = snackbarHostState.showSnackbar(
                message = it.message,
                actionLabel = it.label,
                duration = SnackbarDuration.Short
            )
            if (action == SnackbarResult.ActionPerformed) component(it.event)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Select your aircraft") },
                actions = {
                    MainToolsMenu(component)
                },
                colors = SimColors.topBarColors()
            )
        },
        snackbarHost = mySnackbarHost(snackbarHostState),
        bottomBar = {
            AnimatedVisibility(visible = state.value.updateMessage != null) {
                BottomAppBar {
                    Text(
                        text = state.value.updateMessage ?: "",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(16.dp).weight(1f),
                        textAlign = TextAlign.Center
                    )
                    CircularProgressIndicator(progress = { state.value.progress / 100f })
                }
            }

        },
        contentWindowInsets = WindowInsets.safeDrawing
    ) { padding ->


        val listState = rememberLazyListState()
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            state = listState,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(state.value.aircraftList) { item ->
                Card(
                    elevation = CardDefaults.elevatedCardElevation(12.dp),
                    modifier = Modifier.clickable { component(MainScreenEvent.SelectAircraft(item.id)) }
                        .padding(1.dp)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        val photoModifier = Modifier
                            .padding(8.dp)
                            .size(150.dp)
                            .shadow(
                                elevation = 8.dp,
                                shape = RoundedCornerShape(16.dp),
                                clip = true
                            )
                        val painter = loadAircraftJpgPhoto(item.photo)
                        if (painter != null) {
                            Image(
                                painter = painter,
                                contentDescription = "Photo of ${item.name}",
                                modifier = photoModifier
                            )
                        } else {
                            Box(
                                modifier = photoModifier,
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    painter = painterResource(Res.drawable.airplanes),
                                    contentDescription = "Photo of ${item.name} is loading",
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth().align(Alignment.CenterHorizontally),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (item.photo.startsWith(CUSTOM_PREFIX)) {
                                IconButton(
                                    onClick = { component(MainScreenEvent.RemoveAircraftConfirm(item.id)) }
                                ) {
                                    Icon(
                                        painter = painterResource(Res.drawable.delete),
                                        contentDescription = "Remove ${item.name}"
                                    )
                                }
                            } else {
                                // keep the title centered on bundled cards too
                                Spacer(Modifier.size(48.dp))
                            }
                            Text(
                                text = item.name.uppercase(),
                                modifier = Modifier.padding(4.dp).weight(1f),
                                color = MaterialTheme.colorScheme.onSurface,
                                textAlign = TextAlign.Center,
                                style = largeWithShadow()
                            )
                            IconButton(
                                onClick = { component(MainScreenEvent.SelectFuelCalculator(item.id)) }
                            ) {
                                Icon(
                                    painter = painterResource(Res.drawable.gas_station),
                                    contentDescription = "Open fuel calculator"
                                )
                            }
                        }
                    }
                }
            }
            if (BuildKonfig.DEBUG) item {
                Button(
                    onClick = { component(MainScreenEvent.DropBaseConfirm) },
                    modifier = Modifier.padding(6.dp),
                    colors = SimColors.buttonColors()
                ) {
                    Text("Drop airports database")
                }
            }
        }
    }
}
