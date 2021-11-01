package decompose

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.ExperimentalDecomposeApi
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
        initialConfiguration = Configuration.AircraftList,
        childFactory = ::createChild,
        handleBackButton = true
    )

    val routerState = router.state

    private fun createChild(configuration: Configuration, context: ComponentContext): Content =
        when (configuration) {

            Configuration.AircraftList -> AircraftList(context, aircraftBase.getAll()) { aircraft ->
                router.push(Configuration.Checklists(aircraft.id))
            }.asContent { AircraftListUi(it) }

            is Configuration.Checklists -> Checklists(
                context,
                aircraftBase.getById(configuration.aircraftId),
                onBack = {
                    if (router.state.value.backStack.isNotEmpty()) router.pop()
                    else router.push(Configuration.AircraftList)
                }
            ) { checklist ->
                router.push(Configuration.Checklist(configuration.aircraftId, checklist.id))
            }.asContent { ChecklistsUi(it) }

            is Configuration.Checklist -> ChecklistDetails(
                context,
                aircraftBase.getChecklist(configuration.aircraftId, configuration.checklistId)
            ) {
                if (router.state.value.backStack.isNotEmpty()) router.pop()
                else router.push(Configuration.Checklists(configuration.aircraftId))
            }.asContent { ChecklistUi(it) }

            is Configuration.FuelCalculator -> Unit.asContent {}
        }
}

@OptIn(ExperimentalDecomposeApi::class)
@Composable
fun RootUi(root: Root) {
    Children(root.routerState, animation = slide()) { child -> child.instance() }
}
