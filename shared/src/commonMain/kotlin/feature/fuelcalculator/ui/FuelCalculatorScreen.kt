package feature.fuelcalculator.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import feature.fuelcalculator.FuelCalcViewState
import feature.fuelcalculator.FuelCalculator
import feature.fuelcalculator.FuelUiEvent
import commonUi.RelativeOutlineInput
import commonUi.ValidatedOutlineInput
import commonUi.ValidatorIcon
import commonUi.utils.SimColors
import commonUi.utils.mySnackbarHost

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FuelCalculatorScreen(component: FuelCalculator) {

    val snackbarHostState = remember { SnackbarHostState() }
    val state: State<FuelCalcViewState> = component.state.subscribeAsState()

    state.value.snackBar?.let {
        LaunchedEffect(snackbarHostState) {
            val result = snackbarHostState.showSnackbar(it.message, it.button, false, SnackbarDuration.Short)
            if (result == SnackbarResult.ActionPerformed) component.onEvent(it.event)
        }
    }

    Scaffold(
        snackbarHost = mySnackbarHost(snackbarHostState),
        topBar = {
            TopAppBar(
                title = { Text("Block fuel calculator") },
                navigationIcon = {
                    IconButton(onClick = { component.onEvent(FuelUiEvent.Back) }) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back button")
                    }
                },
                colors = SimColors.topBarColors()
            )
        },
        contentWindowInsets = WindowInsets.safeDrawing
    ) { paddingValues ->

        var tripDistance by rememberSaveable { mutableStateOf(state.value.tripDistance.toString()) }
        var alterDistance by rememberSaveable { mutableStateOf(state.value.alterDistance.toString()) }
        var headWind by rememberSaveable { mutableStateOf(state.value.headWindComponent.toString()) }
        var avgCruiseSpeed by rememberSaveable { mutableStateOf(state.value.performance.averageCruiseSpeed.toString()) }
        var avgFuelFlow by rememberSaveable { mutableStateOf(state.value.performance.averageFuelFlow.toString()) }
        var taxiFuel by rememberSaveable { mutableStateOf(state.value.performance.taxiFuel.toString()) }
        var contingency by rememberSaveable { mutableStateOf(state.value.performance.contingency.toString()) }
        var reserveTime by rememberSaveable { mutableStateOf(state.value.performance.reservesMinutes.toString()) }
        var fuelCapacity by rememberSaveable { mutableStateOf(state.value.performance.fuelCapacity.toString()) }

        val scrollState = rememberScrollState()

        Column(
            modifier = Modifier.fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            val scope = rememberCoroutineScope()
            val relocationRequester = remember { BringIntoViewRequester() }

            Text(
                text = "Calculate fuel quantity for your trip on ${state.value.name}",
                modifier = Modifier.padding(8.dp),
                color = MaterialTheme.colorScheme.onSurface
            )
            Row {
                ValidatedOutlineInput(
                    modifier = Modifier.weight(1f),
                    value = tripDistance,
                    labelText = "Trip distance, sm",
                    isErrorToggle = component.isFloatIncorrect(tripDistance, false)
                ) { new -> tripDistance = new; component.onEvent(FuelUiEvent.TripDistanceChange(new)) }

                ValidatedOutlineInput(
                    modifier = Modifier.weight(1f),
                    value = alterDistance,
                    labelText = "Alter. distance, sm",
                    isErrorToggle = component.isFloatIncorrect(alterDistance)
                ) { new -> alterDistance = new; component.onEvent(FuelUiEvent.AlterDistanceChange(new)) }

            }

            Row {
                ValidatedOutlineInput(
                    Modifier.weight(1f),
                    headWind, "Headwind component, kt",
                    component.isIntIncorrect(headWind),
                ) { new -> headWind = new; component.onEvent(FuelUiEvent.HeadwindChange(new)) }

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
                ValidatedOutlineInput(
                    Modifier.weight(1f),
                    taxiFuel, "Taxi fuel, g",
                    component.isFloatIncorrect(taxiFuel),
                ) { new -> taxiFuel = new; component.onEvent(FuelUiEvent.TaxiChange(new)) }

                ValidatedOutlineInput(
                    Modifier.weight(1f),
                    "$contingency%", "Contingency fuel, % of trip",
                    component.isFloatIncorrect(contingency),
                ) { new ->
                    contingency = new.substringBefore("%")
                    component.onEvent(FuelUiEvent.ContingencyChange(new.substringBefore("%")))
                }
            }
            HorizontalDivider(modifier = Modifier.padding(8.dp))
            Text(
                text = "You may change params below according to the performance table",
                modifier = Modifier.padding(8.dp).align(Alignment.CenterHorizontally),
                color = MaterialTheme.colorScheme.onSurface
            )
            Row {
                RelativeOutlineInput(
                    Modifier.weight(1f),
                    avgCruiseSpeed, "Average cruise speed, kt", relocationRequester, scope,
                    component.isFloatIncorrect(avgCruiseSpeed, false),
                ) { new -> avgCruiseSpeed = new; component.onEvent(FuelUiEvent.CruiseSpeedChange(new)) }

                RelativeOutlineInput(
                    Modifier.weight(1f),
                    avgFuelFlow, "Average fuel flow, gph", relocationRequester, scope,
                    component.isFloatIncorrect(avgFuelFlow, false),
                ) { new -> avgFuelFlow = new; component.onEvent(FuelUiEvent.FuelFlowChange(new)) }

            }
            Text(
                text = "You should not change params below, but you may on your own risk",
                modifier = Modifier.padding(8.dp).align(Alignment.CenterHorizontally),
                color = MaterialTheme.colorScheme.onSurface
            )
            Row {
                RelativeOutlineInput(
                    Modifier.weight(1f),
                    reserveTime, "Reserve time, min", relocationRequester, scope,
                    component.isIntIncorrect(reserveTime),
                ) { new -> reserveTime = new; component.onEvent(FuelUiEvent.ReserveTimeChange(new)) }

                RelativeOutlineInput(
                    Modifier.weight(1f),
                    fuelCapacity, "Fuel capacity, g", relocationRequester, scope,
                    component.isFloatIncorrect(fuelCapacity, false),
                ) { new -> fuelCapacity = new; component.onEvent(FuelUiEvent.FuelCapacityChange(new)) }

            }
        }
    }
}
