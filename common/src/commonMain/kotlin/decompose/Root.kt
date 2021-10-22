package decompose

import com.arkivanov.decompose.router.RouterState
import com.arkivanov.decompose.value.Value

interface Root {

    val routerState: Value<RouterState<*, Child>>

    fun onNext()
    fun onPrevious()

    sealed class Child {
        class Page(val component: decompose.Page) : Child()
    }
}