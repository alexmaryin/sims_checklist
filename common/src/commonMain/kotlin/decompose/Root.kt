package decompose

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.jetbrains.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.animation.child.slide
import com.arkivanov.decompose.router.RouterState
import com.arkivanov.decompose.router.pop
import com.arkivanov.decompose.router.push
import com.arkivanov.decompose.router.router
import com.arkivanov.decompose.value.Value
import model.AircraftBase

typealias Content = @Composable () -> Unit

fun <T : Any> T.asContent(content: @Composable (T) -> Unit): Content = { content(this) }

class Root(
    componentContext: ComponentContext,
    private val aircraftBase: AircraftBase
) : ComponentContext by componentContext {

    private val router = router<Configuration, Content>(
        initialConfiguration = Configuration.AircraftList,
        childFactory = ::createChild,
        handleBackButton = true
    )

    val routerState: Value<RouterState<*, Content>> = router.state

    private fun createChild(configuration: Configuration, context: ComponentContext): Content =
        when (configuration) {

            Configuration.AircraftList -> AircraftList(aircraftBase.getAll()) { aircraft ->
                router.push(Configuration.Checklists(aircraft.id))
            }.asContent { AircraftListUi(it) }

            is Configuration.Checklists -> Checklists(
                aircraftBase.getById(configuration.aircraftId),
                onBack = router::pop
            ) { checklist ->
                router.push(Configuration.Checklist(configuration.aircraftId, checklist.id))
            }.asContent { ChecklistsUi(it) }

            is Configuration.Checklist -> ChecklistDetails(
                aircraftBase.getChecklist(configuration.aircraftId, configuration.checklistId),
                router::pop
            ).asContent { ChecklistUi(it) }

            is Configuration.FuelCalculator -> Unit.asContent {}
        }
}

@OptIn(ExperimentalDecomposeApi::class)
@Composable
fun RootUi(root: Root) {
    Children(root.routerState, animation = slide()) { it.instance() }
}
