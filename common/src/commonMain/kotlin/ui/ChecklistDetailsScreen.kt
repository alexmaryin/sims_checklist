package ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import decompose.ChecklistDetails

@Composable
expect fun ScrollBarForList(modifier: Modifier, state: LazyListState)
expect fun offsetForScrollBar(): Dp

@Composable
fun ChecklistDetailsScreen(component: ChecklistDetails) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(component.state.value.caption) },
                navigationIcon = {
                    IconButton(onClick = component.onFinished) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back button")
                    }
                },
                actions = {
                    IconButton(onClick = { component.clear() }) {
                        Icon(imageVector = Icons.Default.Clear, contentDescription = "Uncheck all")
                    }
                }
            )
        }
    ) {

        val state: State<ChecklistDetails.ComponentData> = component.state.subscribeAsState()
        val listState = rememberLazyListState()

        Box {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(end = offsetForScrollBar()),
                state = listState,
            ) {
                itemsIndexed(state.value.items) { index, item ->
                    Row(
                        modifier = Modifier
                            .clickable { component.toggle(index) }
                            .fillMaxWidth()
                            .background(if (item.checked) MaterialTheme.colors.secondary else MaterialTheme.colors.surface)
                            .padding(10.dp)
                    ) {
                        if (item.caption == "LINE") {
                            Divider()
                        } else {
                            Column(modifier = Modifier.weight(1f).fillMaxWidth()) {
                                ToggableText(item.caption, item.checked)
                                if (item.details.isNotEmpty()) ToggableText(
                                    text = item.details,
                                    isToggled = item.checked,
                                    modifier = Modifier.padding(start = 6.dp),
                                    textStyle = TextStyle(fontWeight = FontWeight.Light, fontSize = 12.sp)
                                )
                            }

                            if(item.action.isNotEmpty())
                                ToggableText(item.action, item.checked)

                            if (item.checked) Icon(
                                imageVector = Icons.Default.Done,
                                contentDescription = "item checked"
                            )
                        }
                    }
                }
            }
            ScrollBarForList(Modifier.align(Alignment.CenterEnd).fillMaxHeight(), listState)
        }
    }
}

