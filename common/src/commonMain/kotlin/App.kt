import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ComponentContext
import decompose.Root
import decompose.RootUi
import viewState.SimViewState

@Composable
fun App(context: ComponentContext, simViewState: SimViewState) {
    Surface {
        RootUi(Root(context, simViewState.aircraftBase))
    }
}
