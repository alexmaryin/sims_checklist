package decompose

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.extensions.compose.jetbrains.Children
import com.arkivanov.decompose.router.pop
import com.arkivanov.decompose.router.push
import com.arkivanov.decompose.router.router
import model.Database

typealias Content = @Composable () -> Unit

fun <T : Any> T.asContent(content: @Composable (T) -> Unit) : Content = { content(this) }

class Root(
    componentContext: ComponentContext,
    private val database: Database
) : ComponentContext by componentContext {

    private val router = router<Configuration, Content>(
        initialConfiguration = ItemsList,
        childFactory = ::createChild
    )

    val routerState = router.state

    private fun createChild(configuration: Configuration, context: ComponentContext): Content =
        when(configuration) {

            is ItemsList -> ItemList(database) { id ->
                router.push(Details(id))
            }.asContent { ItemListUi(it) }

            is Details -> ItemDetails(configuration.id, database, router::pop).asContent {
                ItemDetailsUi(it)
            }
        }
}

@Composable
fun RootUi(root: Root) {
    Children(root.routerState) { child ->
        child.instance()
    }
}