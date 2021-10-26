package ui

import ScrollBarForList
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import model.Item
import offsetForScrollBar

@Composable
fun ItemListScreen(items: List<Item>, onItemClick: (id: Long) -> Unit) {
    val state = rememberLazyListState()
    Box {
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(end = offsetForScrollBar()), state
        ) {
            items(items) { item ->
                Text(
                    text = item.text,
                    modifier = Modifier.clickable { onItemClick(item.id) }
                        .fillMaxWidth()
                        .padding(16.dp)
                )
                Divider()
            }
        }
        ScrollBarForList(Modifier.align(Alignment.CenterEnd).fillMaxHeight(), state)
    }

}