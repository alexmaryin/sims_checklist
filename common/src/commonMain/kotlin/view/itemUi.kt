package view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Checkbox
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import decompose.Item

@Composable
fun ItemUi(component: Item, modifier: Modifier = Modifier) {
    val item by component.item.subscribeAsState()

    Row(
        modifier = modifier.background(
            color = if(item.checked) Color.Green else Color.Unspecified
        ).fillMaxWidth(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.Top,
    ) {
        Column(
            modifier = Modifier.weight(2f)
        ) {
            Text(text = item.caption)
            if(item.details.isNotEmpty()) Text(text = item.details)
        }
        Checkbox(
            checked = item.checked,
            modifier = Modifier.weight(1f, false),
            onCheckedChange = { component.onClick() }
        )
    }
}