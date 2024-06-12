package feature.checklistDetails.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
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

    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    val state: State<ChecklistViewState> = component.state.subscribeAsState()

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopBarWithClearAction(
                caption = state.value.checklist.caption,
                onBack = { component.onEvent(ChecklistUiEvent.Back) },
                onClear = { component.onEvent(ChecklistUiEvent.Clear) })
        }) {

        state.value.snackBar?.let {
            scope.launch {
                scaffoldState.snackbarHostState.currentSnackbarData?.dismiss()
                val result = scaffoldState.snackbarHostState.showSnackbar(it.message, it.button, SnackbarDuration.Short)
                if (result == SnackbarResult.ActionPerformed) component.onEvent(it.event)
            }
        }

        val listState = rememberLazyListState()

        Box {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(end = offsetForScrollBar()),
                state = listState,
            ) {
                itemsIndexed(state.value.checklist.items) { index, item ->

                    if (item.caption == CHECKLIST_LINE) {
                        Divider()
                    } else {
                        ChecklistItem(item) { component.onEvent(ChecklistUiEvent.Toggle(index)) }
                    }
                }
            }
            ScrollBarForList(Modifier.align(Alignment.CenterEnd).fillMaxHeight(), listState)
        }
    }
}


