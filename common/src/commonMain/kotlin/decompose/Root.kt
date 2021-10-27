package decompose

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.extensions.compose.jetbrains.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.animation.child.slide
import com.arkivanov.decompose.router.pop
import com.arkivanov.decompose.router.push
import com.arkivanov.decompose.router.router
import model.AircraftBase

typealias Content = @Composable () -> Unit

fun <T : Any> T.asContent(content: @Composable (T) -> Unit): Content = { content(this) }

class Root(
    componentContext: ComponentContext,
    private val aircraftBase: AircraftBase
) : ComponentContext by componentContext {

    private val router = router<Configuration, Content>(
        initialConfiguration = Configuration.Checklists(0),
        childFactory = ::createChild,
        handleBackButton = true
    )

    val routerState = router.state

    private fun createChild(configuration: Configuration, context: ComponentContext): Content =
        when (configuration) {

            is Configuration.Checklists -> Checklists(aircraftBase.getById(configuration.aircraftId)) { checklist ->
                router.push(Configuration.Checklist(configuration.aircraftId, checklist.id))
            }.asContent { ChecklistsUi(it) }

            is Configuration.Checklist -> ChecklistDetails(
                aircraftBase.getChecklist(configuration.aircraftId, configuration.checklistId),
                router::pop
            ).asContent {
                ChecklistUi(it)
            }

            is Configuration.FuelCalculator -> Unit.asContent {}
        }
}

@Composable
fun RootUi(root: Root) {
    Children(
        root.routerState,
        animation = slide()
    ) { child ->
        child.instance()
    }
}
