package feature.checklistDetails.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import feature.checklistDetails.model.Item
import ui.ToggleableText

@Composable
fun ChecklistItem(item: Item, onClick: () -> Unit) = Row(
    modifier = Modifier
        .clickable { onClick() }
        .fillMaxWidth()
        .background(if (item.checked) MaterialTheme.colors.secondaryVariant else MaterialTheme.colors.surface)
        .padding(10.dp)
) {

    Column(modifier = Modifier.weight(1f).fillMaxWidth()) {
        ToggleableText(item.caption, item.checked)
        if (item.details.isNotEmpty()) ToggleableText(
            text = item.details,
            isToggled = item.checked,
            modifier = Modifier.padding(start = 6.dp),
            textStyle = TextStyle(fontWeight = FontWeight.Light, fontSize = 12.sp)
        )
    }

    Spacer(modifier = Modifier.width(24.dp))

    if (item.action.isNotEmpty())
        ToggleableText(item.action, item.checked)

    if (item.checked) Icon(
        imageVector = Icons.Default.Done,
        modifier = Modifier.size(24.dp),
        contentDescription = "item checked"
    )
}