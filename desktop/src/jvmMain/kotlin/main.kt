import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.decompose.extensions.compose.lifecycle.LifecycleController
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import decompose.Root
import di.apiModule
import di.dbModule
import di.roomModule
import org.koin.core.context.startKoin

fun main() {
    startKoin {
        modules(dbModule, apiModule, roomModule)
    }
    val lifecycle = LifecycleRegistry()
    val root = runOnUiThread { Root(DefaultComponentContext(lifecycle)) }

    application {
        val windowState = rememberWindowState()
        LifecycleController(lifecycle, windowState)

        Window(
            state = windowState,
            title = "Sims checklists",
            onCloseRequest = ::exitApplication
        ) {
            val isDark = isSystemInDarkTheme()
            MaterialTheme(colorScheme = if (isDark) Themes.dark else Themes.light) {
                App(root)
            }
        }
    }
}

