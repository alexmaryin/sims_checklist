import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.Lifecycle
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import decompose.Root
import decompose.RootUi
import model.DatabaseImpl

@Composable
fun App(lifecycle: Lifecycle = LifecycleRegistry()) {

    MaterialTheme {
        RootUi(Root(DefaultComponentContext(LifecycleRegistry()), DatabaseImpl()))
    }
}

expect fun getPlatformName(): String