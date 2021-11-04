package ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import decompose.ChecklistDetails
import model.CHECKLIST_LINE
import model.Checklist

@Composable
expect fun ScrollBarForList(modifier: Modifier, state: LazyListState)
expect fun offsetForScrollBar(): Dp

@Composable
fun ChecklistDetailsScreen(component: ChecklistDetails) {

    val scaffoldState = rememberScaffoldState()
    val state: State<Checklist> = component.state.subscribeAsState()

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = { TopBarWithClearAction(state.value.caption, component.onFinished, component::clear) }
    ) {

        if (state.value.isCompleted) LaunchedEffect(scaffoldState.snackbarHostState) {
            val result = scaffoldState.snackbarHostState.showSnackbar(
                "${state.value.caption} checklist is completed",
                "Close?",
                SnackbarDuration.Long
            )
            if (result == SnackbarResult.ActionPerformed) component.onFinished()
        }

        val listState = rememberLazyListState()

        Box {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(end = offsetForScrollBar()),
                state = listState,
            ) {
                itemsIndexed(state.value.items) { index, item ->

                    if (item.caption == CHECKLIST_LINE) {
                        Divider()
                    } else {
                        ChecklistItem(item) { component.toggle(index) }
                    }
                }
            }
            ScrollBarForList(Modifier.align(Alignment.CenterEnd).fillMaxHeight(), listState)
        }
    }
}


