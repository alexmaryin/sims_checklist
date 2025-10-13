package feature.airportsBase.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import commonUi.utils.MyIcons
import commonUi.utils.SimColors
import commonUi.utils.mySnackbarHost
import feature.airportsBase.AirportEventExecutor
import feature.airportsBase.AirportsUiEvent
import kotlinx.coroutines.launch

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
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back button")
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
                            Icon(imageVector = MyIcons.Update, "update database", Modifier.padding(8.dp))
                        }
                    }
                },
                colors = SimColors.topBarColors()
            )
        },
        snackbarHost = mySnackbarHost(snackbarHostState),
        contentWindowInsets = WindowInsets.safeDrawing
    ) { paddingValues ->

        state.value.snackbar?.let {
            scope.launch {
                snackbarHostState.currentSnackbarData?.dismiss()
                val result = snackbarHostState.showSnackbar(it.message, it.button, false, SnackbarDuration.Short)
                if (result == SnackbarResult.ActionPerformed) component(it.event)
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxWidth().padding(paddingValues),
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
                eventsExecutor = component
            )
        }
    }
}