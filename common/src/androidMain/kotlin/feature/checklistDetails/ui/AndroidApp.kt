package feature.checklistDetails.ui

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
actual fun ScrollBarForList(modifier: Modifier, state: LazyListState) = Unit

actual fun offsetForScrollBar() = 0.dp
