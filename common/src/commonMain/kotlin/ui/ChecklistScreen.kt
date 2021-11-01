package ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import model.Checklist

@Composable
expect fun ScrollBarForList(modifier: Modifier, state: LazyListState)
expect fun offsetForScrollBar(): Dp

@Composable
fun ChecklistScreen(checklist: Checklist, onBackClick: () -> Unit) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(checklist.caption) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back button")
                    }
                }
            )
        }
    ) {

        val state = rememberLazyListState()

        Box {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(end = offsetForScrollBar()),
                state = state,
            ) {
                items(checklist.items) { item ->
                    val isChecked = remember { mutableStateOf(item.checked && item.caption != "LINE") }
                    Row(
                        modifier = Modifier
                            .clickable { item.toggle(); isChecked.value = item.checked && item.caption != "LINE" }
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
            ScrollBarForList(Modifier.align(Alignment.CenterEnd).fillMaxHeight(), state)
        }
    }
}

