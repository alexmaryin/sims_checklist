package feature.mainScreen.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import common.BuildKonfig
import feature.mainScreen.MainEventExecutor
import feature.mainScreen.MainScreenEvent
import org.jetbrains.compose.resources.painterResource
import sims_checklist.shared.generated.resources.Res
import sims_checklist.shared.generated.resources.air
import sims_checklist.shared.generated.resources.airplanes
import sims_checklist.shared.generated.resources.apps
import sims_checklist.shared.generated.resources.cloud_download
import sims_checklist.shared.generated.resources.compress
import sims_checklist.shared.generated.resources.snowflake

@Composable
fun MainToolsMenu(component: MainEventExecutor) {
    val state = component.state.subscribeAsState()
    var expanded by remember { mutableStateOf(false) }

    // DropdownMenu positions itself against its parent layout node,
    // so the anchor button and the menu share this Box as siblings.
    Box {
        IconButton(onClick = {
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

        MenuFooter(
            airportsCount = state.value.airportsCount,
            version = BuildKonfig.VER
        )
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
