import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.Lifecycle
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.destroy
import com.arkivanov.essenty.lifecycle.resume
import decompose.Root
import decompose.RootUi
import decompose.rememberComponentContext
import model.DatabaseImpl



@Composable
fun App() {

    MaterialTheme {
        RootUi(Root(rememberComponentContext(), DatabaseImpl()))
    }
}

expect fun getPlatformName(): String