package ui

import androidx.compose.foundation.LocalScrollbarStyle
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
actual fun ScrollBarForList(modifier: Modifier, state: LazyListState) = VerticalScrollbar(
    modifier = modifier,
    adapter = rememberScrollbarAdapter(scrollState = state),
    style = LocalScrollbarStyle.current.copy(thickness = offsetForScrollBar())
)

actual fun offsetForScrollBar() = 16.dp

