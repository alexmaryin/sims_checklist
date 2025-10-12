package feature.checklists.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import feature.checklists.Checklists
import feature.checklists.ChecklistsUiEvent
import feature.checklists.ChecklistsViewState
import commonUi.TopBarWithClearAction
import commonUi.utils.largeWithShadow
import commonUi.utils.mySnackbarHost

@Composable
fun ChecklistsScreen(component: Checklists) {

    val state: State<ChecklistsViewState> = component.state.subscribeAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        component.onEvent(ChecklistsUiEvent.Refresh)
    }

    state.value.snackBar?.let {
        LaunchedEffect(snackbarHostState) {
            val result = snackbarHostState.showSnackbar(it.message, it.button, false, SnackbarDuration.Short)
            if (result == SnackbarResult.ActionPerformed) component.onEvent(it.event)
        }
    }

    Scaffold(
        snackbarHost = mySnackbarHost(snackbarHostState),
        topBar = {
            TopBarWithClearAction(
                caption = state.value.caption,
                onBack = { component.onEvent(ChecklistsUiEvent.Back) },
                onClear = { component.onEvent(ChecklistsUiEvent.ConfirmClear) }
            )
        },
        contentWindowInsets = WindowInsets.safeDrawing
    ) { paddingValues ->

        val lazyState = rememberLazyListState()
        LazyColumn(modifier = Modifier.fillMaxSize().padding(paddingValues), lazyState) {

            items(
                items = state.value.list,
                key = { checklist -> checklist.id }
            ) { checklist ->
                Card(
                    modifier = Modifier.padding(vertical = 1.dp),
                    elevation = CardDefaults.elevatedCardElevation(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (checklist.isCompleted) MaterialTheme.colorScheme.inversePrimary
                        else Color.Unspecified
                    )
                ) {
                    Text(
                        text = checklist.caption.uppercase(),
                        modifier = Modifier.clickable { component.onEvent(ChecklistsUiEvent.SelectChecklist(checklist.id)) }
                            .fillMaxWidth()
                            .padding(16.dp),
                        color = if (checklist.isCompleted) MaterialTheme.colorScheme.inverseOnSurface else MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center,
                        style = largeWithShadow()
                    )
                }
            }
        }
    }
}

