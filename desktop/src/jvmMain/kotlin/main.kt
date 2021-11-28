import androidx.compose.material.MaterialTheme
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import decompose.Root
import decompose.SimViewState
import feature.remote.metarService.MetarService

fun main() = application {

    val service = MetarService.create()
    val root = Root(DefaultComponentContext(LifecycleRegistry()), SimViewState().aircraftRepository, service)

    Window(
        title = "Sims checklists",
        onCloseRequest = ::exitApplication) {
        MaterialTheme(colors = Themes.light) {
            App(root)
        }
    }
}
