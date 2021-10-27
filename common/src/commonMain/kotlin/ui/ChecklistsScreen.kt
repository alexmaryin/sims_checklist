package ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Card
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import model.Checklist

@Composable
fun LargeWithShadow(multi: Int  = 1) = LocalTextStyle.current.copy(
    fontSize = 20.sp,
    fontWeight = FontWeight.Normal,
    shadow = Shadow(color = Color.DarkGray, offset = Offset(2f * multi, 2f * multi), blurRadius = 2f * multi)
)

@Composable
fun ChecklistsScreen(items: List<Checklist>, onChecklistClick: (checklist: Checklist) -> Unit) {
    val state = rememberLazyListState()
    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp), state) {
        items(items) { item ->
            Card(
                elevation = 12.dp,
                backgroundColor = if (item.isCompleted) MaterialTheme.colors.secondary else MaterialTheme.colors.surface
            ) {
                Text(
                    text = item.caption.uppercase(),
                    modifier = Modifier.clickable { onChecklistClick(item) }
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

