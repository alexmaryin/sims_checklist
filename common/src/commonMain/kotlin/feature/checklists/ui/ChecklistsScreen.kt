package feature.checklists.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import feature.checklists.Checklists
import feature.checklists.model.Aircraft
import ui.utils.LargeWithShadow
import ui.TopBarWithClearAction

@Composable
fun ChecklistsScreen(component: Checklists) {

    val aircraft: State<Aircraft> = component.state.subscribeAsState()

    Scaffold(
        topBar = { TopBarWithClearAction(aircraft.value.name, component.onBack, component::clear) }
    ) {

        val state = rememberLazyListState()
        LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp), state) {

            items(aircraft.value.checklists) { item ->
                Card(
                    modifier = Modifier.padding(vertical = 1.dp),
                    elevation = 12.dp,
                    backgroundColor = if (item.isCompleted) MaterialTheme.colors.secondaryVariant else MaterialTheme.colors.surface
                ) {
                    Text(
                        text = item.caption.uppercase(),
                        modifier = Modifier.clickable { component.onSelected(item) }
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

