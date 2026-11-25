package feature.mainScreen.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.svenjacobs.reveal.Reveal
import com.svenjacobs.reveal.RevealOverlayArrangement
import com.svenjacobs.reveal.RevealShape
import com.svenjacobs.reveal.rememberRevealState
import com.svenjacobs.reveal.shapes.balloon.Arrow
import com.svenjacobs.reveal.shapes.balloon.Balloon
import common.BuildKonfig
import commonUi.reveals.Keys
import feature.mainScreen.MainEventExecutor
import feature.mainScreen.MainScreenEvent
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import sims_checklist.shared.generated.resources.*

@Composable
fun MainToolsMenu(component: MainEventExecutor) {
    val state = component.state.subscribeAsState()
    var expanded by remember { mutableStateOf(false) }
    val revealState = rememberRevealState()
    val coroutineScope = rememberCoroutineScope()

    // DropdownMenu positions itself against its parent layout node,
    // so the anchor button and the menu share this Box as siblings.

    Reveal(
        revealState = revealState,
        onRevealableClick = {
            coroutineScope.launch { revealState.hide() }
        },
        onOverlayClick = {
            coroutineScope.launch { revealState.hide() }
        },
        overlayContent = { key ->
            when (key) {
                Keys.MainMenu -> {
                    Balloon(
                        arrow = Arrow.end(),
                        backgroundColor = Color.White,
                        modifier = Modifier.align(horizontalArrangement = RevealOverlayArrangement.Start)
                            .padding(8.dp),
                    ) {
                        Text(modifier = Modifier.padding(8.dp), text = "All helpful functions are live here")
                    }
                }
            }
        }
    ) {
        Box {
            IconButton(
                modifier = Modifier.revealable(key = Keys.MainMenu),
                onClick = {
                    component(MainScreenEvent.OpenToolsMenu)
                    expanded = !expanded
                }) {
                Icon(
                    painter = painterResource(Res.drawable.apps),
                    contentDescription = "Tools"
                )
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                DropdownMenuItem(
                    text = { Text("Weather and airport") },
                    onClick = {
                        expanded = false
                        component(MainScreenEvent.SelectMetar)
                    },
                    leadingIcon = {
                        Icon(painter = painterResource(Res.drawable.air), contentDescription = null)
                    }
                )
                DropdownMenuItem(
                    text = { Text("Airports database") },
                    onClick = {
                        expanded = false
                        component(MainScreenEvent.SelectAirportsBase)
                    },
                    leadingIcon = {
                        Icon(painter = painterResource(Res.drawable.cloud_download), contentDescription = null)
                    }
                )
                DropdownMenuItem(
                    text = { Text("QFE helper") },
                    onClick = {
                        expanded = false
                        component(MainScreenEvent.SelectQFEHelper)
                    },
                    leadingIcon = {
                        Icon(painter = painterResource(Res.drawable.compress), contentDescription = null)
                    }
                )
                DropdownMenuItem(
                    text = { Text("Cold temperature corrector") },
                    onClick = {
                        expanded = false
                        component(MainScreenEvent.SelectColdTemperature)
                    },
                    leadingIcon = {
                        Icon(painter = painterResource(Res.drawable.snowflake), contentDescription = null)
                    }
                )
                DropdownMenuItem(
                    text = { Text("Add custom aircraft") },
                    onClick = {
                        expanded = false
                        component(MainScreenEvent.SelectAddCustomAircraft)
                    },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(Res.drawable.ic_airplane),
                            contentDescription = null,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                )

                MenuFooter(
                    airportsCount = state.value.airportsCount,
                    version = BuildKonfig.VER
                )
            }
        }
        LaunchedEffect(Unit) {
            revealState.reveal(Keys.MainMenu)
        }
    }
}

@Composable
private fun MenuFooter(airportsCount: Long?, version: String) {
    Column(
        modifier = Modifier.padding(top = 16.dp, bottom = 8.dp, start = 12.dp, end = 12.dp),
        horizontalAlignment = Alignment.Start
    ) {
        val airportsText = if (airportsCount == null || airportsCount == 0L)
            "No airports in database"
        else
            "$airportsCount airports in database"

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(Res.drawable.airplanes),
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = airportsText,
                modifier = Modifier.padding(start = 8.dp),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Text(
            text = "App ver. $version",
            modifier = Modifier.padding(top = 8.dp),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
