package decompose

import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.LocalSaveableStateRegistry
import androidx.compose.runtime.saveable.SaveableStateRegistry
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.backpressed.BackPressedDispatcher
import com.arkivanov.essenty.backpressed.BackPressedHandler
import com.arkivanov.essenty.lifecycle.Lifecycle
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.destroy
import com.arkivanov.essenty.lifecycle.resume
import com.arkivanov.essenty.parcelable.ParcelableContainer
import com.arkivanov.essenty.statekeeper.StateKeeper
import com.arkivanov.essenty.statekeeper.StateKeeperDispatcher

@Composable
private fun rememberLifecycle(): Lifecycle {
    val lifecycle = remember { LifecycleRegistry() }

    DisposableEffect(Unit) {
        lifecycle.resume()
        onDispose { lifecycle.destroy() }
    }

    return lifecycle
}

@Composable
private fun rememberStateKeeper(): StateKeeper {
    val saveableStateRegistry: SaveableStateRegistry? = LocalSaveableStateRegistry.current

    val dispatcher = remember {
        StateKeeperDispatcher(saveableStateRegistry?.consumeRestored("STATE") as ParcelableContainer?)
    }

    saveableStateRegistry?.let {
        DisposableEffect(Unit) {
            val entry = it.registerProvider("STATE", dispatcher::save)
            onDispose { entry.unregister() }
        }
    }

    return dispatcher
}

val localBackPressedDispatcher: ProvidableCompositionLocal<BackPressedHandler?> = staticCompositionLocalOf { null }

//@Composable
//fun rememberComponentContext(): ComponentContext {
//    val lifecycle = rememberLifecycle()
//    val stateKeeper = rememberStateKeeper()
//    val backPressedDispatcher = localBackPressedDispatcher.current ?: BackPressedDispatcher()
//
//    return remember {
//        DefaultComponentContext(lifecycle, stateKeeper, backPressedHandler = backPressedDispatcher)
//    }
//}
