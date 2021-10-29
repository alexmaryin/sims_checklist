import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import decompose.Root
import decompose.RootUi
import decompose.rememberComponentContext
import model.AircraftBaseImpl

@Composable
fun App() {
    MaterialTheme {
        Surface {
            RootUi(Root(rememberComponentContext(), AircraftBaseImpl()))
        }
    }
}
