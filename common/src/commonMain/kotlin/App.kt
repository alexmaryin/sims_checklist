import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import decompose.Root
import decompose.RootUi
import decompose.rememberComponentContext
import model.AircraftBaseTestImpl

@Composable
fun App() {
    MaterialTheme() {
        Surface {
            RootUi(Root(rememberComponentContext(), AircraftBaseTestImpl()))
        }
    }
}
