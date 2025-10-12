package commonUi

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun AdaptiveLayout(
    padding: PaddingValues,
    children: @Composable (width: Dp, height: Dp) -> Unit
) {
    val layoutModifier = Modifier.fillMaxWidth().padding(8.dp)
    val scrollState = rememberScrollState()
    BoxWithConstraints(
        modifier = Modifier.fillMaxSize().padding(padding).verticalScroll(scrollState),
    ) {
        val width = maxWidth
        val height = maxHeight
        if (width > 600.dp) {
            Row(modifier = layoutModifier) {
                children(width, height)
            }
        } else {
            Column(modifier = layoutModifier) {
                children(width, height)
            }
        }
    }
}