package view

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import decompose.Checklist

@Composable
fun ChecklistUi(component: Checklist, modifier: Modifier = Modifier) {
    val checklist by component.checklist.subscribeAsState()

    LazyColumn(modifier = modifier) {
        items(checklist.groups) { group ->
            GroupUi(group, modifier)
        }
    }
}