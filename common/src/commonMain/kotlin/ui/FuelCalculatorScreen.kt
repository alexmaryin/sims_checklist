package ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import decompose.FuelCalculator

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
    ) {
        val inputModifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp).fillMaxWidth()

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
        Column(modifier = Modifier.verticalScroll(scrollState, enabled = true)) {
            Text(
                text = "Calculate fuel quantity for your trip on ${component.aircraft.name}",
                modifier = Modifier.padding(8.dp).align(Alignment.CenterHorizontally)
            )
            Row {
                OutlinedTextField(
                    value = tripDistance,
                    modifier = inputModifier.weight(1f),
                    onValueChange = { new -> tripDistance = new; component.onTripDistanceChange(new) },
                    label = { Text("Trip distance, sm") },
                    isError = component.isFloatIncorrect(tripDistance, false),
                    trailingIcon = { ValidatorIcon(component.isFloatIncorrect(tripDistance, false)) }
                )
                OutlinedTextField(
                    value = alterDistance,
                    modifier = inputModifier.weight(1f),
                    onValueChange = { new -> alterDistance = new; component.onAlterDistanceChange(new) },
                    label = { Text("Alter distance, sm") },
                    isError = component.isFloatIncorrect(alterDistance),
                    trailingIcon = { ValidatorIcon(component.isFloatIncorrect(alterDistance)) }
                )
            }

            Row {
                OutlinedTextField(
                    value = headWind,
                    modifier = inputModifier.weight(1f),
                    onValueChange = { new -> headWind = new; component.onHeadwindChange(new) },
                    label = { Text("Headwind component, kt") },
                    isError = component.isIntIncorrect(headWind),
                    trailingIcon = { ValidatorIcon(component.isIntIncorrect(headWind)) }
                )
                OutlinedTextField(
                    value = state.blockFuel().toString(),
                    onValueChange = {},
                    textStyle = LocalTextStyle.current.copy(
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.ExtraBold,
                        color = if (state.fuelExceed) MaterialTheme.colors.error else Color.Unspecified
                    ),
                    modifier = inputModifier.weight(1f),
                    readOnly = true,
                    label = { Text(if (state.fuelExceed) "Fuel exceed!" else "Calculated block fuel, g") },
                    isError = state.fuelExceed,
                    trailingIcon = { ValidatorIcon(state.fuelExceed) }
                )
            }
            Row {
                OutlinedTextField(
                    value = taxiFuel,
                    modifier = inputModifier.weight(1f),
                    onValueChange = { new -> taxiFuel = new; component.onTaxiChange(new) },
                    label = { Text("Taxi fuel, g") },
                    isError = component.isFloatIncorrect(taxiFuel),
                    trailingIcon = { ValidatorIcon(component.isFloatIncorrect(taxiFuel)) }
                )
                OutlinedTextField(
                    value = "$contingency%",
                    modifier = inputModifier.weight(1f),
                    onValueChange = { new -> contingency = new.substringBefore("%"); component.onContingencyChange(new.substringBefore("%")) },
                    label = { Text("Contingency fuel, % of trip") },
                    isError = component.isFloatIncorrect(contingency),
                    trailingIcon = { ValidatorIcon(component.isFloatIncorrect(contingency)) }
                )
            }
            Divider(modifier = Modifier.padding(vertical = 6.dp, horizontal = 6.dp))
            Text(
                text = "You may change params below due to the performance table",
                modifier =  Modifier.padding(8.dp).align(Alignment.CenterHorizontally)
            )
            Row {
                OutlinedTextField(
                    value = avgCruiseSpeed,
                    modifier = inputModifier.weight(1f),
                    onValueChange = { new -> avgCruiseSpeed = new; component.onCruiseSpeedChange(new) },
                    label = { Text("Average cruise speed, kt") },
                    isError = component.isFloatIncorrect(avgCruiseSpeed, false),
                    trailingIcon = { ValidatorIcon(component.isFloatIncorrect(avgCruiseSpeed, false)) }
                )
                OutlinedTextField(
                    value = avgFuelFlow,
                    modifier = inputModifier.weight(1f),
                    onValueChange = { new -> avgFuelFlow = new; component.onFuelFlowChange(new) },
                    label = { Text("Average fuel flow, gph") },
                    isError = component.isFloatIncorrect(avgFuelFlow, false),
                    trailingIcon = { ValidatorIcon(component.isFloatIncorrect(avgFuelFlow, false)) }
                )
            }
            Text(
                text = "You should not change params below, but you may on your own risk",
                modifier =  Modifier.padding(8.dp).align(Alignment.CenterHorizontally)
            )
            Row {
                OutlinedTextField(
                    value = reserveTime,
                    modifier = inputModifier.weight(1f),
                    onValueChange = { new -> reserveTime = new; component.onReserveTimeChange(new) },
                    label = { Text("Reserve time, min") },
                    isError = component.isIntIncorrect(reserveTime),
                    trailingIcon = { ValidatorIcon(component.isIntIncorrect(reserveTime)) }
                )
                OutlinedTextField(
                    value = fuelCapacity,
                    modifier = inputModifier.weight(1f),
                    onValueChange = { new -> fuelCapacity = new; component.onFuelCapacityChange(new) },
                    label = { Text("Fuel capacity, g") },
                    isError = component.isFloatIncorrect(fuelCapacity, false),
                    trailingIcon = { ValidatorIcon(component.isFloatIncorrect(fuelCapacity, false)) }
                )
            }
        }
    }
}