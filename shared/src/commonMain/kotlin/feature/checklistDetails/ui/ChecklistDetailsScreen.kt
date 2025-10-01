package feature.checklistDetails.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import feature.checklistDetails.ChecklistDetails
import feature.checklistDetails.ChecklistUiEvent
import feature.checklistDetails.ChecklistViewState
import feature.checklistDetails.model.CHECKLIST_LINE
import kotlinx.coroutines.launch
import ui.TopBarWithClearAction

@Composable
expect fun ScrollBarForList(modifier: Modifier, state: LazyListState)
expect fun offsetForScrollBar(): Dp

@Composable
fun ChecklistDetailsScreen(component: ChecklistDetails) {

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val state: State<ChecklistViewState> = component.state.subscribeAsState()

    Scaffold(
        modifier = Modifier.safeDrawingPadding(),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopBarWithClearAction(
                caption = state.value.caption,
                onBack = { component.onEvent(ChecklistUiEvent.Back) },
                onClear = { component.onEvent(ChecklistUiEvent.Clear) })
        }) { paddingValues ->

        state.value.snackBar?.let {
            scope.launch {
                snackbarHostState.currentSnackbarData?.dismiss()
                val result = snackbarHostState.showSnackbar(it.message, it.button, false, SnackbarDuration.Short)
                if (result == SnackbarResult.ActionPerformed) component.onEvent(it.event)
            }
        }

        val listState = rememberLazyListState()

        Box(Modifier.padding(paddingValues)) {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(end = offsetForScrollBar()),
                state = listState,
            ) {
                itemsIndexed(items = state.value.items) { index, item ->

                    if (item.caption == CHECKLIST_LINE) {
                        HorizontalDivider()
                    } else {
                        ChecklistItem(item) { component.onEvent(ChecklistUiEvent.Toggle(index)) }
                    }
                }
            }
            ScrollBarForList(Modifier.align(Alignment.CenterEnd).fillMaxHeight(), listState)
        }
    }
}


