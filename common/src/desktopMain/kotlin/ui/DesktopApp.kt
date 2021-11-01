package ui

import androidx.compose.foundation.LocalScrollbarStyle
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.res.useResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
actual fun ScrollBarForList(modifier: Modifier, state: LazyListState) = VerticalScrollbar(
    modifier = modifier,
    adapter = rememberScrollbarAdapter(scrollState = state),
    style = LocalScrollbarStyle.current.copy(thickness = offsetForScrollBar())
)

actual fun offsetForScrollBar() = 16.dp

actual suspend fun loadAircraftPhoto(filename: String): Painter = withContext(Dispatchers.IO) {
    useResource(filename) { stream ->
        BitmapPainter(loadImageBitmap(stream))
    }
}



