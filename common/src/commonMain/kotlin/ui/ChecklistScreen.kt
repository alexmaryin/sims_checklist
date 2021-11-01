package ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
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
import model.ChecklistViewState
import kotlin.time.TimeMark
import kotlin.time.TimeSource

@Composable
expect fun ScrollBarForList(modifier: Modifier, state: LazyListState)
expect fun offsetForScrollBar(): Dp

@Composable
fun ChecklistScreen(checklist: ChecklistViewState, onBackClick: () -> Unit) {

    println("ChecklistScreen fun invoked")

    val state by checklist.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(state.caption) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back button")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        checklist.clear()
                    }) {
                        Icon(imageVector = Icons.Default.Clear, contentDescription = "Uncheck all")
                    }
                }
            )
        }
    ) {

        val listState = rememberLazyListState()

        Box {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(end = offsetForScrollBar()),
                state = listState,
            ) {
                itemsIndexed(state.items) { index, item ->

                    val isChecked = remember { mutableStateOf(state.items[index].checked) }

                    Row(
                        modifier = Modifier
                            .clickable { checklist.toggleItem(index) }
                            .fillMaxWidth()
                            .background(if (isChecked.value) MaterialTheme.colors.secondary else MaterialTheme.colors.surface)
                            .padding(10.dp)
                    ) {
                        if (item.caption == "LINE") {
                            Divider()
                        } else {
                            Column(modifier = Modifier.weight(1f).fillMaxWidth()) {
                                ToggableText(item.caption, isChecked.value)
                                if (item.details.isNotEmpty()) ToggableText(
                                    text = item.details,
                                    isToggled = isChecked.value,
                                    modifier = Modifier.padding(start = 6.dp),
                                    textStyle = TextStyle(fontWeight = FontWeight.Light, fontSize = 12.sp)
                                )
                            }

                            if(item.action.isNotEmpty())
                                ToggableText(item.action, isChecked.value)

                            if (isChecked.value) Icon(
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

