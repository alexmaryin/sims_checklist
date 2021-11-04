package ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import model.Item

@Composable
fun ChecklistItem(item: Item, onClick: () -> Unit) = Row(
    modifier = Modifier
        .clickable { onClick() }
        .fillMaxWidth()
        .background(if (item.checked) MaterialTheme.colors.secondary else MaterialTheme.colors.surface)
        .padding(10.dp)
) {

    Column(modifier = Modifier.weight(1f).fillMaxWidth()) {
        ToggableText(item.caption, item.checked)
        if (item.details.isNotEmpty()) ToggableText(
            text = item.details,
            isToggled = item.checked,
            modifier = Modifier.padding(start = 6.dp),
            textStyle = TextStyle(fontWeight = FontWeight.Light, fontSize = 12.sp)
        )
    }

    if (item.action.isNotEmpty())
        ToggableText(item.action, item.checked)

    if (item.checked) Icon(
        imageVector = Icons.Default.Done,
        contentDescription = "item checked"
    )
}