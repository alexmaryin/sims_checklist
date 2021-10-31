package ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import model.Aircraft

@Composable
fun AircraftListScreen(
    items: List<Aircraft>,
    onAircraftClick: (aircraft: Aircraft) -> Unit) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Выберите самолет") },
            )
        }
    ) {
        val state = rememberLazyListState()
        LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp), state) {
            items(items) { item ->
                Card(elevation = 12.dp) {
                    Text(
                        text = item.name.uppercase(),
                        modifier = Modifier.clickable { onAircraftClick(item) }
                            .fillMaxWidth()
                            .padding(16.dp),
                        color =  MaterialTheme.colors.onSurface,
                        textAlign = TextAlign.Center,
                        style = LargeWithShadow()
                    )
                }
            }
        }
    }
}
