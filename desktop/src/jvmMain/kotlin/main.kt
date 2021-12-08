import androidx.compose.material.MaterialTheme
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import decompose.Root
import di.apiModule
import di.dbModule
import org.koin.core.context.startKoin

fun main() = application {

    startKoin {
        modules(dbModule, apiModule)
    }

    val root = Root(DefaultComponentContext(LifecycleRegistry()))

    Window(
        title = "Sims checklists",
        onCloseRequest = ::exitApplication) {
        MaterialTheme(colors = Themes.light) {
            App(root)
        }
    }
}

