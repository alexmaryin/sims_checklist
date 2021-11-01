package ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import model.Checklist

@Composable
fun ChecklistsScreen(
    name: String,
    items: List<Checklist>,
    onBackClick: () -> Unit,
    onChecklistClick: (checklist: Checklist) -> Unit
) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(name) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back button")
                    }
                }
            )
        }
    ) {
        val state = rememberLazyListState()
        LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp), state) {
            items(items) { item ->
                Card(
                    modifier = Modifier.padding(vertical = 1.dp),
                    elevation = 12.dp,
                    backgroundColor = if (item.isCompleted) MaterialTheme.colors.secondary else MaterialTheme.colors.surface
                ) {
                    Text(
                        text = item.caption.uppercase(),
                        modifier = Modifier.clickable { onChecklistClick(item) }
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

