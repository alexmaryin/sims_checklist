package decompose

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.jetbrains.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.animation.child.slide
import com.arkivanov.decompose.router.*
import com.arkivanov.decompose.value.Value
import model.AircraftBase
import feature.remote.service.MetarService
import ui.metarscreen.MetarScreen
import ui.AircraftListScreen
import ui.ChecklistDetailsScreen
import ui.ChecklistsScreen
import ui.FuelCalculatorScreen

typealias Content = @Composable () -> Unit

fun <T : Any> T.asContent(content: @Composable (T) -> Unit): Content = { content(this) }

class Root(
    componentContext: ComponentContext,
    private val aircraftBase: AircraftBase,
    private val metarService: MetarService
) : ComponentContext by componentContext {

    private val router = router<Configuration, Content>(
        initialConfiguration = Configuration.AircraftList,
        childFactory = ::createChild,
        handleBackButton = true,
    )

    val routerState: Value<RouterState<*, Content>> = router.state

    private fun createChild(configuration: Configuration, context: ComponentContext): Content =
        when (configuration) {

            Configuration.AircraftList -> AircraftList(
                aircraftList = aircraftBase.getAll(),
                onSelected = { id -> router.push(Configuration.Checklists(id)) },
                onCalculatorSelect = { id -> router.push(Configuration.FuelCalculator(id)) },
                onMetarSelect = { router.push((Configuration.MetarScanner)) }
            ).asContent { AircraftListScreen(it) }

            is Configuration.Checklists -> Checklists(
                aircraft = aircraftBase.getById(configuration.aircraftId),
                onBack = { router.pop() },
                onSelected = { checklist ->
                    router.push(Configuration.Checklist(configuration.aircraftId, checklist.id))
                },
                clearBaseChecklists = {
                    aircraftBase.clearBaseChecklists(configuration.aircraftId)
                }
            ).asContent { ChecklistsScreen(it) }

            is Configuration.Checklist -> ChecklistDetails(
                checklist = aircraftBase.getChecklist(configuration.aircraftId, configuration.checklistId),
                onFinished = { router.pop() },
                updateBaseChecklist = { items ->
                    aircraftBase.updateBaseChecklist(configuration.aircraftId, configuration.checklistId, items)
                }
            ).asContent { ChecklistDetailsScreen(it) }

            is Configuration.FuelCalculator -> FuelCalculator(
                aircraft = aircraftBase.getById(configuration.aircraftId),
                onBack = { router.pop() }
            ).asContent { FuelCalculatorScreen(it) }

            is Configuration.MetarScanner -> MetarScanner(
                metarService = metarService,
                onBack = { router.pop() }
            ).asContent { MetarScreen(it) }
        }
}

@OptIn(ExperimentalDecomposeApi::class)
@Composable
fun RootUi(root: Root) {
    Children(root.routerState, animation = slide()) { it.instance() }
}
