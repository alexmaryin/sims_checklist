import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.Lifecycle
import decompose.RootComponent
import view.RootUi

@Composable
fun App(lifecycle: Lifecycle) {
    MaterialTheme {
        val root = RootComponent(DefaultComponentContext(lifecycle))
        RootUi(root)
    }
}

expect fun getPlatformName(): String