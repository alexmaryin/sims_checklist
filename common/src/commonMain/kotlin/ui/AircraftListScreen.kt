package ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import decompose.AircraftList

expect suspend fun loadAircraftPhoto(filename: String): Painter

@Composable
fun AircraftListScreen(component: AircraftList) {

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Select your aircraft") })
        }
    ) {
        val state = rememberLazyListState()
        LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp), state) {
            items(component.aircraftList) { item ->
                Card(elevation = 12.dp, modifier = Modifier.clickable { component.onSelected(item.id) }) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {

                        AsyncImage(
                            loader = { loadAircraftPhoto(item.photo) },
                            painterFor = { it },
                            contentDescription = "Photo of ${item.name}",
                            modifier = Modifier
                                .padding(8.dp)
                                .size(150.dp)
                                .shadow(elevation = 8.dp, shape = RoundedCornerShape(16.dp), clip = true)
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = item.name.uppercase(),
                                modifier = Modifier.padding(8.dp),
                                color = MaterialTheme.colors.onSurface,
                                textAlign = TextAlign.Center,
                                style = LargeWithShadow()
                            )
                            IconButton(onClick = { component.onCalculatorSelect(item.id) }) {
                                Icon(imageVector = MyIcons.GasStation, contentDescription = "Open fuel calculator")
                            }
                            IconButton(onClick = { component.onMetarSelect() }) {
                                Icon(imageVector = MyIcons.Air, contentDescription = "Open fuel calculator")
                            }
                        }

                    }
                }
            }
        }
    }
}


