package ui

import androidx.compose.foundation.Image
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
import org.jetbrains.compose.resources.painterResource
import sims_checklist.shared.generated.resources.Res
import sims_checklist.shared.generated.resources.allDrawableResources
import ui.utils.LargeWithShadow
import ui.utils.MyIcons

@Composable
fun loadAircraftJpgPhoto(name: String): Painter = painterResource(Res.allDrawableResources[name]!!)

@Composable
fun AircraftListScreen(component: AircraftList) {

    Scaffold(
        modifier = Modifier.safeDrawingPadding(),
        topBar = {
            TopAppBar(
                title = { Text("Select your aircraft") },
                actions = {
                    IconButton(onClick = { component.onMetarSelect() }) {
                        Icon(imageVector = MyIcons.Air, contentDescription = "Weather and airport")
                    }
                    IconButton(onClick = { component.onAirportsBaseSelect() }) {
                        Icon(imageVector = MyIcons.Update, contentDescription = "Airports database")
                    }
                    IconButton(onClick = { component.onQFEHelperSelect() }) {
                        Icon(imageVector = MyIcons.Compress, contentDescription = "QFE helper")
                    }
                }
            )
        }
    ) { padding ->
        val state = rememberLazyListState()
        LazyColumn(modifier = Modifier.fillMaxSize().padding(padding), state) {
            items(component.aircraftList) { item ->
                Card(elevation = 12.dp, modifier = Modifier.clickable { component.onSelected(item.id) }) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Image(
                            painter = loadAircraftJpgPhoto(item.photo),
                            contentDescription = "Photo of ${item.name}",
                            modifier = Modifier
                                .padding(8.dp)
                                .size(150.dp)
                                .shadow(
                                    elevation = 8.dp,
                                    shape = RoundedCornerShape(16.dp),
                                    clip = true
                                )
                        )
                        Row (
                            modifier = Modifier.fillMaxWidth().align(Alignment.CenterHorizontally)
                        ) {
                            Text(
                                text = item.name.uppercase(),
                                modifier = Modifier.padding(8.dp).weight(1f),
                                color = MaterialTheme.colors.onSurface,
                                textAlign = TextAlign.Center,
                                style = LargeWithShadow()
                            )
                            IconButton(
                                onClick = { component.onCalculatorSelect(item.id) }
                            ) {
                                Icon(imageVector = MyIcons.GasStation, contentDescription = "Open fuel calculator")
                            }
                        }
                    }
                }
            }
        }
    }
}


