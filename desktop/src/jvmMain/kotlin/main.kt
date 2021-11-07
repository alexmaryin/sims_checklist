import androidx.compose.material.MaterialTheme
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import decompose.Root
import feature.remote.service.MetarService
import viewState.SimViewState

fun main() = application {

    val service = MetarService.create()
    val root = Root(DefaultComponentContext(LifecycleRegistry()), SimViewState().aircraftBase, service)

    Window(
        title = "Sims checklists",
        onCloseRequest = ::exitApplication) {
        MaterialTheme() {
            App(root)
        }
    }
}
