import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import decompose.Root
import di.apiModule
import di.dbModule
import di.realmModule
import org.koin.core.context.startKoin

fun main() = application {

    startKoin {
        modules(dbModule, apiModule, realmModule)
    }

    val root = Root(DefaultComponentContext(LifecycleRegistry()))

    Window(
        title = "Sims checklists",
        onCloseRequest = ::exitApplication) {
        val isDark = isSystemInDarkTheme()
        MaterialTheme(colors = if (isDark) Themes.dark else Themes.light) {
            App(root)
        }
    }
}

