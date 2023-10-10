package decompose

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.plus
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.scale
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.stackAnimation
import com.arkivanov.decompose.router.stack.*
import com.arkivanov.decompose.value.Value
import decompose.RootComponent.Child.*
import feature.airportsBase.AirportsBase
import feature.airportsBase.ui.AirportsBaseScreen
import feature.checklistDetails.ChecklistDetails
import feature.checklistDetails.ui.ChecklistDetailsScreen
import feature.checklists.Checklists
import feature.checklists.ui.ChecklistsScreen
import feature.fuelcalculator.FuelCalculator
import feature.fuelcalculator.ui.FuelCalculatorScreen
import feature.metarscreen.MetarScanner
import feature.metarscreen.ui.MetarScreen
import feature.qfeHelper.QFEHelper
import feature.qfeHelper.ui.QFEHelperScreen
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import repository.AircraftRepository
import ui.AircraftListScreen

class Root(
    componentContext: ComponentContext,
) : RootComponent, KoinComponent, ComponentContext by componentContext {

    private val aircraftRepository: AircraftRepository by inject()

    private val navigation = StackNavigation<Configuration>()
    override val stack: Value<ChildStack<*, RootComponent.Child>> = childStack(
        source = navigation,
        initialConfiguration = Configuration.AircraftList,
        handleBackButton = true,
        childFactory = ::createChild
    )

    override fun onBackClicked(toIndex: Int) = navigation.popTo(toIndex)

    private fun aircraftList() = AircraftList(
        aircraftList = aircraftRepository.getAll(),
        onSelected = { id -> navigation.push(Configuration.Checklists(id)) },
        onCalculatorSelect = { id -> navigation.push(Configuration.FuelCalculator(id)) },
        onMetarSelect = { navigation.push(Configuration.MetarScanner) },
        onAirportsBaseSelect = { navigation.push(Configuration.AirportsBase) },
        onQFEHelperSelect = { navigation.push(Configuration.QFEHelper) }
    )

    private fun checklists(aircraftId: Int) = Checklists(
        aircraftId = aircraftId,
        repository = aircraftRepository,
        onBack = { navigation.pop() },
        onSelected = { checklistId ->
            navigation.push(Configuration.Checklist(aircraftId, checklistId))
        }
    )

    private fun checklist(aircraftId: Int, checklistId: Int) = ChecklistDetails(
        aircraftId = aircraftId,
        checklistId = checklistId,
        repository = aircraftRepository,
        onFinished = { navigation.pop() }
    )

    private fun fuelCalculator(aircraftId: Int) = FuelCalculator(
        aircraft = aircraftRepository.getById(aircraftId),
        onBack = { navigation.pop() }
    )

    private fun metarScanner() = MetarScanner(
        onBack = { navigation.pop() }
    )

    private fun airportsBase() = AirportsBase(
        onBack = { navigation.pop() }
    )

    private fun qfeHelper() = QFEHelper(
        onBack = { navigation.pop() }
    )

    private fun createChild(configuration: Configuration, context: ComponentContext): RootComponent.Child =
        when (configuration) {

            Configuration.AircraftList -> AircraftListChild(aircraftList())

            is Configuration.Checklists -> ChecklistsChild(checklists(configuration.aircraftId))

            is Configuration.Checklist -> ChecklistDetailsChild(checklist(configuration.aircraftId, configuration.checklistId))

            is Configuration.FuelCalculator -> FuelCalculatorChild(fuelCalculator(configuration.aircraftId))

            is Configuration.MetarScanner -> MetarScannerChild(metarScanner())

            is Configuration.AirportsBase -> AirportsBaseChild(airportsBase())

            is Configuration.QFEHelper -> QFEHelperChild(qfeHelper())
        }
}

@Composable
fun RootUi(root: RootComponent) {
    Children(
        stack = root.stack,
        animation = stackAnimation(fade() + scale()),
    ) {
        when (val child = it.instance) {
            is AircraftListChild -> AircraftListScreen(child.component)
            is AirportsBaseChild -> AirportsBaseScreen(child.component)
            is ChecklistDetailsChild -> ChecklistDetailsScreen(child.component)
            is ChecklistsChild -> ChecklistsScreen(child.component)
            is FuelCalculatorChild -> FuelCalculatorScreen(child.component)
            is MetarScannerChild -> MetarScreen(child.component)
            is QFEHelperChild -> QFEHelperScreen(child.component)
        }
    }
}
