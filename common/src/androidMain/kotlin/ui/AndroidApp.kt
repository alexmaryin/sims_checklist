package ui

import android.graphics.BitmapFactory
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.alexmaryin.simschecklist.AppAndroid

@Composable
actual fun ScrollBarForList(modifier: Modifier, state: LazyListState) = Unit

actual fun offsetForScrollBar() = 0.dp

actual suspend fun loadAircraftPhoto(filename: String): Painter = withContext(Dispatchers.IO) {
    BitmapPainter(
        image = BitmapFactory.decodeStream(AppAndroid.instance().loadPhoto(filename)).asImageBitmap()
    )
}
