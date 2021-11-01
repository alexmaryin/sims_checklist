import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ComponentContext
import decompose.Root
import decompose.RootUi
import model.AircraftBaseImpl

@Composable
fun App(context: ComponentContext) {
    Surface {
        RootUi(Root(context, AircraftBaseImpl()))
    }
}
