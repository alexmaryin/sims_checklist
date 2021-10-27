package ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontSynthesis
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import model.Checklist

@Composable
expect fun ScrollBarForList(modifier: Modifier, state: LazyListState)
expect fun offsetForScrollBar(): Dp

@Composable
fun ChecklistScreen(checklist: Checklist, onBackClick: () -> Unit) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(checklist.caption) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back button")
                    }
                }
            )
        }
    ) {

        val state = rememberLazyListState()

        Box {
            LazyColumn(modifier = Modifier.fillMaxSize().padding(end = offsetForScrollBar()), state = state) {
                items(checklist.items) { item ->
                    val isChecked = remember { mutableStateOf(item.checked) }
                    Column(
                        modifier = Modifier
                            .clickable { item.toggle(); isChecked.value = item.checked }
                            .fillMaxWidth()
                            .background(if (isChecked.value) MaterialTheme.colors.secondary else MaterialTheme.colors.surface)
                            .padding(10.dp)
                    ) {
                        Text(
                            item.caption,
                            color = if (isChecked.value) MaterialTheme.colors.onSecondary else MaterialTheme.colors.onSurface,
                            fontWeight = FontWeight.Normal,
                            fontSize = 18.sp
                        )
                        if (item.details.isNotEmpty()) Text(
                            item.details,
                            color = if (isChecked.value) MaterialTheme.colors.onSecondary else MaterialTheme.colors.onSurface,
                            fontWeight = FontWeight.Light,
                            fontSize = 12.sp
                        )
                    }
                    Divider()
                }
            }
            ScrollBarForList(Modifier.align(Alignment.CenterEnd).fillMaxHeight(), state)
        }
    }
}

