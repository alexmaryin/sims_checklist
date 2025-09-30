package feature.airportsBase.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import feature.airportsBase.AirportEventExecutor
import feature.airportsBase.AirportsUiEvent
import kotlinx.coroutines.launch
import ui.utils.MyIcons

@Composable
fun AirportsBaseScreen(component: AirportEventExecutor) {

    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    val state = component.state.subscribeAsState()

    Scaffold(
        modifier = Modifier.safeDrawingPadding(),
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = { Text("Airports base") },
                navigationIcon = {
                    IconButton(onClick = { component(AirportsUiEvent.Back) }) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back button")
                    }
                }
            )
        }
    ) { paddingValues ->

        LaunchedEffect(true) {
            component(AirportsUiEvent.GetLastUpdate(scope))
        }

        state.value.snackbar?.let {
            scope.launch {
                scaffoldState.snackbarHostState.currentSnackbarData?.dismiss()
                val result = scaffoldState.snackbarHostState.showSnackbar(it.message, it.button, SnackbarDuration.Short)
                if (result == SnackbarResult.ActionPerformed) component(it.event)
            }
        }

        Column(
            modifier = Modifier.fillMaxSize().padding(paddingValues),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            state.value.lastUpdate?.let { date ->
                Text("Last update: $date", Modifier.padding(8.dp))
                Text("${state.value.airportsCount} airports in database", Modifier.padding(8.dp))
            } ?: run {
                Text("Have not updated yet", Modifier.padding(8.dp))
            }
            IconButton(
                onClick = { component(AirportsUiEvent.StartUpdate(scope)) },
                modifier = Modifier.padding(8.dp),
                enabled = !state.value.updating,
            ) {
                if (state.value.updating) {
                    CircularProgressIndicator()
                } else {
                    Icon(imageVector = MyIcons.Update, "update database", Modifier.padding(8.dp))
                }
            }
            if (state.value.updating) {
                val item = state.value.processingLabel.ifEmpty { state.value.processingFile }
                Text(item, Modifier.padding(8.dp))
                LinearProgressIndicator(
                    progress = state.value.progress / 100f,
                    Modifier.fillMaxWidth().padding(8.dp)
                )
            }
        }

    }
}