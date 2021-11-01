import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry

fun main() = application {
    val lifecycle = LifecycleRegistry()

    Window(onCloseRequest = ::exitApplication) {
        MaterialTheme(colors = darkColors()) {
            App(DefaultComponentContext(lifecycle))
        }

    }
}
