package ui

import androidx.compose.foundation.layout.Column
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.text.input.TextFieldValue
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
        val state: State<FuelCalculator.ComponentData> = component.state.subscribeAsState()

        Column {
            TextField(
                value = TextFieldValue(state.value.tripDistance.toString()),
                onValueChange = { component.onTripDistanceChange(it.text.toFloat()) },
            )
            TextField(
                value = TextFieldValue(state.value.alterDistance.toString()),
                onValueChange = { component.onAlterDistanceChange(it.text.toFloat()) },
            )
            TextField(
                value = TextFieldValue(state.value.headWindComponent.toString()),
                onValueChange = { component.onHeadWindChange(it.text.toInt()) }
            )
        }
    }
}