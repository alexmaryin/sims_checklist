package view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import decompose.Group

@Composable
fun GroupUi(component: Group, modifier: Modifier = Modifier) {
    val group by component.group.subscribeAsState()

    Column(modifier = modifier) {
        Text(text = group.caption)
        LazyColumn {
            items(group.items) { item ->
                ItemUi(item, modifier)
            }
        }
    }
}