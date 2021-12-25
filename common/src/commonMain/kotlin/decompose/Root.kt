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
import feature.checklistDetails.ChecklistDetails
import feature.checklistDetails.ui.ChecklistDetailsScreen
import feature.checklists.Checklists
import feature.checklists.ui.ChecklistsScreen
import feature.fuelcalculator.FuelCalculator
import feature.fuelcalculator.ui.FuelCalculatorScreen
import feature.metarscreen.MetarScanner
import feature.metarscreen.ui.MetarScreen
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import repository.AircraftRepository
import ui.AircraftListScreen

typealias Content = @Composable () -> Unit

fun <T : Any> T.asContent(content: @Composable (T) -> Unit): Content = { content(this) }

class Root(
    componentContext: ComponentContext,
) : KoinComponent, ComponentContext by componentContext {

    private val aircraftRepository: AircraftRepository by inject()

    private val router = router<Configuration, Content>(
        initialConfiguration = Configuration.AircraftList,
        childFactory = ::createChild,
        handleBackButton = true,
    )

    val routerState: Value<RouterState<*, Content>> = router.state

    private fun createChild(configuration: Configuration, context: ComponentContext): Content =
        when (configuration) {

            Configuration.AircraftList -> AircraftList(
                aircraftList = aircraftRepository.getAll(),
                onSelected = { id -> router.push(Configuration.Checklists(id)) },
                onCalculatorSelect = { id -> router.push(Configuration.FuelCalculator(id)) },
                onMetarSelect = { router.push((Configuration.MetarScanner)) }
            ).asContent { AircraftListScreen(it) }

            is Configuration.Checklists -> Checklists(
                aircraftId = configuration.aircraftId,
                repository = aircraftRepository,
                onBack = { router.pop() },
                onSelected = { checklistId ->
                    router.push(Configuration.Checklist(configuration.aircraftId, checklistId))
                }
            ).asContent { ChecklistsScreen(it) }

            is Configuration.Checklist -> ChecklistDetails(
                aircraftId = configuration.aircraftId,
                checklistId = configuration.checklistId,
                repository = aircraftRepository,
                onFinished = { router.pop() }
            ).asContent { ChecklistDetailsScreen(it) }

            is Configuration.FuelCalculator -> FuelCalculator(
                aircraft = aircraftRepository.getById(configuration.aircraftId),
                onBack = { router.pop() }
            ).asContent { FuelCalculatorScreen(it) }

            is Configuration.MetarScanner -> MetarScanner(
                onBack = { router.pop() }
            ).asContent { MetarScreen(it) }
        }
}

@OptIn(ExperimentalDecomposeApi::class)
@Composable
fun RootUi(root: Root) {
    Children(root.routerState, animation = slide()) { it.instance() }
}
