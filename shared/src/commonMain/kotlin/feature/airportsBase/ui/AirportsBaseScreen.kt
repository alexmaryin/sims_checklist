package feature.airportsBase.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import commonUi.utils.SimColors
import commonUi.utils.mySnackbarHost
import feature.airportsBase.AirportEventExecutor
import feature.airportsBase.AirportsUiEvent
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import sims_checklist.shared.generated.resources.Res
import sims_checklist.shared.generated.resources.arrow_back
import sims_checklist.shared.generated.resources.scroll_up
import sims_checklist.shared.generated.resources.update_sync

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AirportsBaseScreen(component: AirportEventExecutor) {

    val scope = rememberCoroutineScope()
    val state = component.state.subscribeAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val screenScroll = rememberLazyListState()

    Scaffold(
        topBar = {
            val titlePart = if (state.value.airportsCount > 0) "${state.value.airportsCount} airports"
            else "have not updated yet"
            TopAppBar(
                title = { Text("Airports base ($titlePart)") },
                navigationIcon = {
                    IconButton(onClick = { component(AirportsUiEvent.Back) }) {
                        Icon(painter = painterResource(Res.drawable.arrow_back), contentDescription = "Back button")
                    }
                },
                actions = {
                    IconButton(
                        onClick = { component(AirportsUiEvent.StartUpdate) },
                        modifier = Modifier.padding(8.dp),
                        enabled = !state.value.updating,
                    ) {
                        if (state.value.updating) {
                            CircularProgressIndicator()
                        } else {
                            Icon(
                                painter = painterResource(Res.drawable.update_sync),
                                "update database",
                                Modifier.padding(8.dp)
                            )
                        }
                    }
                },
                colors = SimColors.topBarColors()
            )
        },
        floatingActionButton = {
            AnimatedVisibility(
                visible = !screenScroll.isScrollInProgress && screenScroll.canScrollBackward,
                enter = scaleIn(),
                exit = scaleOut()
            ) {
                FloatingActionButton(
                    onClick = { scope.launch { screenScroll.animateScrollToItem(0) } },
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.scroll_up),
                        contentDescription = "Scroll to top"
                    )
                }
            }
        },
        snackbarHost = mySnackbarHost(snackbarHostState),
        contentWindowInsets = WindowInsets.statusBars
    ) { paddingValues ->

        state.value.snackbar?.let {
            scope.launch {
                snackbarHostState.currentSnackbarData?.dismiss()
                val result = snackbarHostState.showSnackbar(it.message, it.button, false, SnackbarDuration.Short)
                if (result == SnackbarResult.ActionPerformed) component(it.event)
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxWidth().padding(paddingValues)
                .consumeWindowInsets(WindowInsets.safeDrawing),
            state = screenScroll,
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                state.value.lastUpdate?.let { date ->
                    Text("Last update: $date", Modifier.padding(8.dp))
                }
            }
            item {
                if (state.value.updating) {
                    val item = state.value.processingLabel.ifEmpty { state.value.processingFile }
                    Text(item, Modifier.padding(8.dp))
                    LinearProgressIndicator(
                        progress = { state.value.progress / 100f },
                        Modifier.fillMaxWidth().padding(8.dp)
                    )
                }
            }
            airportsList(
                searchString = state.value.searchString,
                searchResult = state.value.searchResult,
                expandedAirport = state.value.expandedAirport,
                isVisible = state.value.airportsCount > 0,
                onAction = { component(it) }
            )
        }
    }

    // Effect for loading next pages
    LaunchedEffect(screenScroll) {
        snapshotFlow {
            screenScroll.layoutInfo.visibleItemsInfo.lastOrNull()?.index to
                    screenScroll.layoutInfo.visibleItemsInfo.first().index
        }
            .distinctUntilChanged()
            .collect { (lastVisible, firstVisible) ->
                val stateLoading = state.value.loadingPage || state.value.searchResult.isEmpty()
                val needLoadNext = lastVisible != null && lastVisible >= state.value.searchResult.size + 2
                if (!stateLoading && needLoadNext) component(AirportsUiEvent.SearchNext)
                if (firstVisible == 0) component(AirportsUiEvent.TrimList)
            }
    }
}