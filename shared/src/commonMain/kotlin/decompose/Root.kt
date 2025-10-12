package decompose

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.plus
import com.arkivanov.decompose.extensions.compose.stack.animation.scale
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
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
import feature.mainScreen.AircraftList
import feature.mainScreen.MainScreenEvent
import feature.mainScreen.ui.AircraftListScreen
import feature.metarscreen.MetarScanner
import feature.metarscreen.ui.MetarScreen
import feature.qfeHelper.QFEHelper
import feature.qfeHelper.ui.QFEHelperScreen

class Root(
    componentContext: ComponentContext,
) : RootComponent, ComponentContext by componentContext {

    private val navigation = StackNavigation<Configuration>()
    override val stack: Value<ChildStack<*, RootComponent.Child>> = childStack(
        source = navigation,
        serializer = Configuration.serializer(),
        initialConfiguration = Configuration.AircraftList,
        handleBackButton = true,
        childFactory = ::createChild
    )

    private fun aircraftList(context: ComponentContext) = AircraftList(
        componentContext = context) { event ->
        when (event) {
            is MainScreenEvent.SelectAircraft -> navigation.pushNew(Configuration.Checklists(event.aircraftId))
            MainScreenEvent.SelectAirportsBase -> navigation.pushNew(Configuration.AirportsBase)
            is MainScreenEvent.SelectFuelCalculator -> navigation.pushNew(Configuration.FuelCalculator(event.aircraftId))
            MainScreenEvent.SelectMetar -> navigation.pushNew(Configuration.MetarScanner())
            MainScreenEvent.SelectQFEHelper -> navigation.pushNew(Configuration.QFEHelper())
            else -> Unit
        }
    }

    private fun checklists(aircraftId: Int) = Checklists(
        aircraftId = aircraftId,
        onBack = { navigation.pop() },
        onSelected = { checklistId ->
            navigation.pushNew(Configuration.Checklist(aircraftId, checklistId))
        }
    )

    private fun checklist(aircraftId: Int, checklistId: Int) = ChecklistDetails(
        aircraftId = aircraftId,
        checklistId = checklistId,
        onFinished = { navigation.pop() }
    )

    private fun fuelCalculator(aircraftId: Int) = FuelCalculator(
        aircraftId = aircraftId,
        onBack = { navigation.pop() }
    )

    private fun metarScanner(context: ComponentContext, icao: String? = null) = MetarScanner(
        componentContext = context,
        icao = icao,
        onOpenQfeHelper = { icao, qfe, celsius ->
            navigation.pushNew(Configuration.QFEHelper(icao, qfe, celsius)) },
        onBack = { navigation.pop() }
    )

    private fun airportsBase(context: ComponentContext) = AirportsBase(
        componentContext = context,
        onSelectAirport = { icao -> navigation.pushNew(Configuration.MetarScanner(icao)) },
        onSelectQfeHelper = { icao -> navigation.pushNew(Configuration.QFEHelper(icao = icao)) },
        onBack = { navigation.pop() }
    )

    private fun qfeHelper(
        context: ComponentContext,
        icao: String? = null,
        qfe: Int? = null,
        celsius: Int? = null
    ) = QFEHelper(
        componentContext = context,
        icao = icao,
        qfe = qfe,
        temperature = celsius,
        onBack = { navigation.pop() }
    )

    private fun createChild(configuration: Configuration, context: ComponentContext): RootComponent.Child =
        when (configuration) {

            Configuration.AircraftList -> AircraftListChild(aircraftList(context))

            is Configuration.Checklists -> ChecklistsChild(checklists(configuration.aircraftId))

            is Configuration.Checklist -> ChecklistDetailsChild(checklist(
                configuration.aircraftId,
                configuration.checklistId
            ))

            is Configuration.FuelCalculator -> FuelCalculatorChild(fuelCalculator(configuration.aircraftId))

            is Configuration.MetarScanner -> MetarScannerChild(metarScanner(context, configuration.icao))

            is Configuration.AirportsBase -> AirportsBaseChild(airportsBase(context))

            is Configuration.QFEHelper -> QFEHelperChild(qfeHelper(
                context,
                configuration.icao,
                configuration.qfe,
                configuration.celsius
            ))
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
