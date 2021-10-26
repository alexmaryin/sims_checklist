import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import decompose.Root
import decompose.RootUi
import decompose.rememberComponentContext
import model.DatabaseImpl

@Composable
fun App() {
    MaterialTheme {
        RootUi(Root(rememberComponentContext(), DatabaseImpl()))
    }
}

expect fun ScrollBarForList(modifier: Modifier, state: LazyListState)

expect fun offsetForScrollBar(): Dp