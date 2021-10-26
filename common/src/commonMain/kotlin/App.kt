import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
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