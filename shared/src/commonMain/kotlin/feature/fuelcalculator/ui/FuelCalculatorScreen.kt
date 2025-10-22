package feature.fuelcalculator.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import commonUi.components.ValidatedFiled
import commonUi.components.ValidatorIcon
import commonUi.utils.ObserveEvents
import commonUi.utils.SimColors
import commonUi.utils.mySnackbarHost
import feature.fuelcalculator.FuelCalcViewState
import feature.fuelcalculator.FuelCalculator
import feature.fuelcalculator.FuelUiEvent
import org.jetbrains.compose.resources.painterResource
import sims_checklist.shared.generated.resources.Res
import sims_checklist.shared.generated.resources.arrow_back

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FuelCalculatorScreen(component: FuelCalculator) {

    val snackbarHostState = remember { SnackbarHostState() }
    val state: State<FuelCalcViewState> = component.state.subscribeAsState()

    ObserveEvents(component.events) { event ->
        snackbarHostState.showSnackbar(
            message = event.message,
            actionLabel = event.button,
            duration = SnackbarDuration.Short
        )
    }

    Scaffold(
        snackbarHost = mySnackbarHost(snackbarHostState),
        topBar = {
            TopAppBar(
                title = { Text("Block fuel calculator") },
                navigationIcon = {
                    IconButton(onClick = { component.onEvent(FuelUiEvent.Back) }) {
                        Icon(painter = painterResource(Res.drawable.arrow_back), contentDescription = "Back button")
                    }
                },
                colors = SimColors.topBarColors()
            )
        },
        contentWindowInsets = WindowInsets.safeDrawing
    ) { paddingValues ->

        val tripDistance = rememberTextFieldState(state.value.tripDistance.toString())
        val alterDistance = rememberTextFieldState(state.value.alterDistance.toString())
        val headWind = rememberTextFieldState(state.value.headWindComponent.toString())
        val avgCruiseSpeed = rememberTextFieldState(state.value.performance.averageCruiseSpeed.toString())
        val avgFuelFlow = rememberTextFieldState(state.value.performance.averageFuelFlow.toString())
        val taxiFuel = rememberTextFieldState(state.value.performance.taxiFuel.toString())
        val contingency = rememberTextFieldState(state.value.performance.contingency.toString())
        val reserveTime = rememberTextFieldState(state.value.performance.reservesMinutes.toString())
        val fuelCapacity = rememberTextFieldState(state.value.performance.fuelCapacity.toString())

        val scrollState = rememberScrollState()

        Column(
            modifier = Modifier.fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "Calculate fuel quantity for your trip on ${state.value.name}",
                modifier = Modifier.padding(8.dp),
                color = MaterialTheme.colorScheme.onSurface
            )
            Row {
                ValidatedFiled(
                    fieldState = tripDistance,
                    modifier = Modifier.weight(1f),
                    label = "Trip distance, sm",
                    isError = component.isFloatIncorrect(tripDistance.text.toString(), false)
                ) { component.onEvent(FuelUiEvent.TripDistanceChange(tripDistance.text.toString())) }

                ValidatedFiled(
                    fieldState = alterDistance,
                    modifier = Modifier.weight(1f),
                    label = "Alter. distance, sm",
                    isError = component.isFloatIncorrect(alterDistance.text.toString())
                ) { component.onEvent(FuelUiEvent.AlterDistanceChange(alterDistance.text.toString())) }

            }
            Row {
                ValidatedFiled(
                    fieldState = headWind,
                    modifier = Modifier.weight(1f),
                    label = "Headwind component, kt",
                    isError = component.isIntIncorrect(headWind.text.toString()),
                ) { component.onEvent(FuelUiEvent.HeadwindChange(headWind.text.toString())) }

                OutlinedTextField(
                    value = state.value.blockFuel().toString(),
                    onValueChange = {},
                    textStyle = LocalTextStyle.current.copy(
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.ExtraBold,
                        color = if (state.value.fuelExceed) MaterialTheme.colorScheme.error else Color.Unspecified
                    ),
                    modifier = Modifier.padding(8.dp).weight(1f),
                    readOnly = true,
                    label = { Text(if (state.value.fuelExceed) "Fuel exceed!" else "Calculated block fuel, g") },
                    isError = state.value.fuelExceed,
                    trailingIcon = { ValidatorIcon(state.value.fuelExceed) }
                )
            }
            Row {
                ValidatedFiled(
                    fieldState = taxiFuel,
                    modifier = Modifier.weight(1f),
                    label = "Taxi fuel, g",
                    isError = component.isFloatIncorrect(taxiFuel.text.toString()),
                ) { component.onEvent(FuelUiEvent.TaxiChange(taxiFuel.text.toString())) }

                ValidatedFiled(
                    fieldState = contingency,
                    modifier = Modifier.weight(1f),
                    label = "Contingency fuel, % of trip",
                    isError = component.isFloatIncorrect(contingency.text.toString()),
                ) { component.onEvent(FuelUiEvent.ContingencyChange(contingency.text.toString())) }
            }
            HorizontalDivider(modifier = Modifier.padding(8.dp))
            Text(
                text = "You may change params below according to the performance table",
                modifier = Modifier.padding(8.dp).align(Alignment.CenterHorizontally),
                color = MaterialTheme.colorScheme.onSurface
            )
            Row {
                ValidatedFiled(
                    fieldState = avgCruiseSpeed,
                    modifier = Modifier.weight(1f),
                    label = "Average cruise speed, kt",
                    isError = component.isFloatIncorrect(avgCruiseSpeed.text.toString(), false),
                ) { component.onEvent(FuelUiEvent.CruiseSpeedChange(avgCruiseSpeed.text.toString())) }

                ValidatedFiled(
                    fieldState = avgFuelFlow,
                    modifier = Modifier.weight(1f),
                    label = "Average fuel flow, gph",
                    isError = component.isFloatIncorrect(avgFuelFlow.text.toString(), false),
                ) { component.onEvent(FuelUiEvent.FuelFlowChange(avgFuelFlow.text.toString())) }

            }
            Text(
                text = "You should not change params below, but you may on your own risk",
                modifier = Modifier.padding(8.dp).align(Alignment.CenterHorizontally),
                color = MaterialTheme.colorScheme.onSurface
            )
            Row {
                ValidatedFiled(
                    fieldState = reserveTime,
                    modifier = Modifier.weight(1f),
                    label = "Reserve time, min",
                    isError = component.isIntIncorrect(reserveTime.text.toString()),
                ) { component.onEvent(FuelUiEvent.ReserveTimeChange(reserveTime.text.toString())) }

                ValidatedFiled(
                    fieldState = fuelCapacity,
                    modifier = Modifier.weight(1f),
                    label = "Fuel capacity, g",
                    isError = component.isFloatIncorrect(fuelCapacity.text.toString(), false),
                ) { component.onEvent(FuelUiEvent.FuelCapacityChange(fuelCapacity.text.toString())) }
            }
        }
    }
}
