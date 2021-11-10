package feature.fuelcalculator.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import feature.fuelcalculator.FuelCalculator
import ui.RelativeOutlineInput
import ui.ValidatedOutlineInput
import ui.ValidatorIcon

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FuelCalculatorScreen(component: FuelCalculator) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Block fuel calculator") },
                navigationIcon = {
                    IconButton(onClick = component.onBack) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back button")
                    }
                }
            )
        }
    ) { padding ->
        val state: FuelCalculator.Model by component.state.subscribeAsState()

        var tripDistance by remember { mutableStateOf(state.tripDistance.toString()) }
        var alterDistance by remember { mutableStateOf(state.alterDistance.toString()) }
        var headWind by remember { mutableStateOf(state.headWindComponent.toString()) }
        var avgCruiseSpeed by remember { mutableStateOf(state.performance.averageCruiseSpeed.toString()) }
        var avgFuelFlow by remember { mutableStateOf(state.performance.averageFuelFlow.toString()) }
        var taxiFuel by remember { mutableStateOf(state.performance.taxiFuel.toString()) }
        var contingency by remember { mutableStateOf(state.performance.contingency.toString()) }
        var reserveTime by remember { mutableStateOf(state.performance.reservesMinutes.toString()) }
        var fuelCapacity by remember { mutableStateOf(state.performance.fuelCapacity.toString()) }

        val scrollState = rememberScrollState()

        Column(
            modifier = Modifier.fillMaxSize().verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            val scope = rememberCoroutineScope()
            val relocationRequester = remember { BringIntoViewRequester() }

            Text(
                text = "Calculate fuel quantity for your trip on ${component.aircraft.name}",
                modifier = Modifier.padding(8.dp),
                color = MaterialTheme.colors.onSurface
            )
            Row(modifier = Modifier.padding(padding)) {
                ValidatedOutlineInput(
                    tripDistance, "Trip distance, sm",
                    component.isFloatIncorrect(tripDistance, false), Modifier.weight(1f)
                ) { new -> tripDistance = new; component.onTripDistanceChange(new) }

                ValidatedOutlineInput(
                    alterDistance, "Alter distance, sm",
                    component.isFloatIncorrect(alterDistance), Modifier.weight(1f)
                ) { new -> alterDistance = new; component.onAlterDistanceChange(new) }

            }

            Row {
                ValidatedOutlineInput(
                    headWind, "Headwind component, kt",
                    component.isIntIncorrect(headWind), Modifier.weight(1f)
                ) { new -> headWind = new; component.onHeadwindChange(new) }

                OutlinedTextField(
                    value = state.blockFuel().toString(),
                    onValueChange = {},
                    textStyle = LocalTextStyle.current.copy(
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.ExtraBold,
                        color = if (state.fuelExceed) MaterialTheme.colors.error else Color.Unspecified
                    ),
                    modifier = Modifier.padding(8.dp).weight(1f),
                    readOnly = true,
                    label = { Text(if (state.fuelExceed) "Fuel exceed!" else "Calculated block fuel, g") },
                    isError = state.fuelExceed,
                    trailingIcon = { ValidatorIcon(state.fuelExceed) }
                )
            }
            Row {
                ValidatedOutlineInput(
                    taxiFuel, "Taxi fuel, g",
                    component.isFloatIncorrect(taxiFuel), Modifier.weight(1f)
                ) { new -> taxiFuel = new; component.onTaxiChange(new) }

                ValidatedOutlineInput(
                    "$contingency%", "Contingency fuel, % of trip",
                    component.isFloatIncorrect(contingency), Modifier.weight(1f)
                ) { new -> contingency = new.substringBefore("%"); component.onContingencyChange(new.substringBefore("%")) }


            }
            Divider(modifier = Modifier.padding(8.dp))
            Text(
                text = "You may change params below due to the performance table",
                modifier = Modifier.padding(8.dp).align(Alignment.CenterHorizontally),
                color = MaterialTheme.colors.onSurface
            )
            Row {
                RelativeOutlineInput(
                    avgCruiseSpeed, "Average cruise speed, kt", relocationRequester, scope,
                    component.isFloatIncorrect(avgCruiseSpeed, false), Modifier.weight(1f)
                ) { new -> avgCruiseSpeed = new; component.onCruiseSpeedChange(new) }

                RelativeOutlineInput(
                    avgFuelFlow, "Average fuel flow, gph", relocationRequester, scope,
                    component.isFloatIncorrect(avgFuelFlow, false), Modifier.weight(1f)
                ) { new -> avgFuelFlow = new; component.onFuelFlowChange(new) }

            }
            Text(
                text = "You should not change params below, but you may on your own risk",
                modifier = Modifier.padding(8.dp).align(Alignment.CenterHorizontally),
                color = MaterialTheme.colors.onSurface
            )
            Row {
                RelativeOutlineInput(
                    reserveTime, "Reserve time, min", relocationRequester, scope,
                    component.isIntIncorrect(reserveTime), Modifier.weight(1f)
                ) { new -> reserveTime = new; component.onReserveTimeChange(new) }

                RelativeOutlineInput(
                    fuelCapacity, "Fuel capacity, g", relocationRequester, scope,
                    component.isFloatIncorrect(fuelCapacity, false), Modifier.weight(1f)
                ) { new -> fuelCapacity = new; component.onFuelCapacityChange(new) }

            }
        }
    }
}


