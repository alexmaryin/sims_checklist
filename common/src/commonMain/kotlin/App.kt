import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import decompose.Root
import decompose.RootUi

@Composable
fun App(root: Root) {
    Surface {
        RootUi(root)
    }
}
