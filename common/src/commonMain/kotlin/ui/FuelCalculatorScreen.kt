package ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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

        var tripDistance by remember { component.tripDistance }
        var alterDistance by remember { component.alterDistance }
        var headWind by remember { component.headWindComponent }
        val correctInput by remember { component.correctInput }
        val blockFuel by remember { component.blockFuel }
        val fuelExceed by remember { component.fuelExceed }

        SideEffect {
            if (correctInput()) component.calculateFuel()
        }

        Column {
            Text(
                text = "Calculate fuel quantity for your trip on ${component.aircraft.name}",
                modifier = Modifier.padding(8.dp).align(Alignment.CenterHorizontally)
            )
            Row {
                OutlinedTextField(
                    value = tripDistance,
                    modifier = inputModifier.weight(1f),
                    onValueChange = { new -> tripDistance = new },
                    label = { Text("Trip distance, sm") },
                    isError = component.isFloatIncorrect(tripDistance, false),
                    trailingIcon = { ValidatorIcon(component.isFloatIncorrect(tripDistance, false)) }
                )
                OutlinedTextField(
                    value = alterDistance,
                    modifier = inputModifier.weight(1f),
                    onValueChange = { new -> alterDistance = new },
                    label = { Text("Alter distance, sm") },
                    isError = component.isFloatIncorrect(alterDistance),
                    trailingIcon = { ValidatorIcon(component.isFloatIncorrect(alterDistance)) }
                )
            }

            Row {
                OutlinedTextField(
                    value = headWind,
                    modifier = inputModifier.weight(1f),
                    onValueChange = { new -> headWind = new },
                    label = { Text("Headwind component, kt") },
                    isError = component.isIntIncorrect(headWind),
                    trailingIcon = { ValidatorIcon(component.isIntIncorrect(headWind)) }
                )
                OutlinedTextField(
                    value = blockFuel.toString(),
                    onValueChange = {},
                    textStyle = LocalTextStyle.current.copy(
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.ExtraBold,
                        color = if(fuelExceed()) MaterialTheme.colors.error else Color.Unspecified
                    ),
                    modifier = inputModifier.weight(1f),
                    readOnly = true,
                    label = { Text(if (fuelExceed()) "Fuel exceed!" else "Calculated block fuel") },
                    isError = fuelExceed(),
                    trailingIcon = { ValidatorIcon(fuelExceed()) }
                )
            }
        }
    }
}