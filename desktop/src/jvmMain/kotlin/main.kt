import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import decompose.Root
import viewState.SimViewState

fun main() = application {
    val root = Root(DefaultComponentContext(LifecycleRegistry()), SimViewState().aircraftBase)

    Window(onCloseRequest = ::exitApplication) {
        MaterialTheme(colors = darkColors()) {
            App(root)
        }
    }
}
