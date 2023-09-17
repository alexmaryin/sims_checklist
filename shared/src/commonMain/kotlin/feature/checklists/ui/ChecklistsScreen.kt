package feature.checklists.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import feature.checklists.Checklists
import feature.checklists.ChecklistsUiEvent
import feature.checklists.ChecklistsViewState
import ui.TopBarWithClearAction
import ui.utils.LargeWithShadow

@Composable
fun ChecklistsScreen(component: Checklists) {

    val state: State<ChecklistsViewState> = component.state.subscribeAsState()

    val scaffoldState = rememberScaffoldState()

    state.value.snackBar?.let {
        LaunchedEffect(scaffoldState.snackbarHostState) {
            val result = scaffoldState.snackbarHostState.showSnackbar(it.message, it.button, SnackbarDuration.Short)
            if (result == SnackbarResult.ActionPerformed) component.onEvent(it.event)
        }
    }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopBarWithClearAction(
                caption = state.value.caption,
                onBack = { component.onEvent(ChecklistsUiEvent.Back) },
                onClear = { component.onEvent(ChecklistsUiEvent.ConfirmClear) }
            )
        }
    ) {

        val lazyState = rememberLazyListState()
        LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp), lazyState) {

            items(state.value.list) { item ->
                Card(
                    modifier = Modifier.padding(vertical = 1.dp),
                    elevation = 12.dp,
                    backgroundColor = if (item.isCompleted) MaterialTheme.colors.secondaryVariant else MaterialTheme.colors.surface
                ) {
                    Text(
                        text = item.caption.uppercase(),
                        modifier = Modifier.clickable { component.onEvent(ChecklistsUiEvent.SelectChecklist(item.id)) }
                            .fillMaxWidth()
                            .padding(16.dp),
                        color = if (item.isCompleted) MaterialTheme.colors.onSecondary else MaterialTheme.colors.onSurface,
                        textAlign = TextAlign.Center,
                        style = LargeWithShadow()
                    )
                }
            }
        }
    }
}

