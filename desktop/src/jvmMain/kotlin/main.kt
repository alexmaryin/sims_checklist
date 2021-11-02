import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import viewState.SimViewState

fun main() = application {
    val lifecycle = LifecycleRegistry()
    val simViewState = SimViewState()

    Window(onCloseRequest = ::exitApplication) {
        MaterialTheme(colors = darkColors()) {
            App(DefaultComponentContext(lifecycle), simViewState)
        }
    }
}
