package decompose

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.RouterState
import com.arkivanov.decompose.router.pop
import com.arkivanov.decompose.router.push
import com.arkivanov.decompose.router.router
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import decompose.Root.Child

class RootComponent(
    componentContext: ComponentContext
) : Root, ComponentContext by componentContext {

    private val router = router<Config, Child>(
        initialConfiguration = Config.Page(0),
        childFactory = ::child
    )

    override val routerState: Value<RouterState<*, Child>> = router.state

    private fun child(config: Config, componentContext: ComponentContext): Child =
        when(config) {
            is Config.Page -> Child.Page(PageComponent())
        }

    override fun onNext() {
        router.push(Config.Page(router.state.value.backStack.size + 1))
    }

    override fun onPrevious() {
        router.pop()
    }

    private sealed class Config : Parcelable {
        @Parcelize
        data class Page(val index: Int) : Config()
    }
}