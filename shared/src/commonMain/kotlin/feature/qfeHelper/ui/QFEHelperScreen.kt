package feature.qfeHelper.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import feature.qfeHelper.QFEEvent
import feature.qfeHelper.QFEHelper
import feature.qfeHelper.QFEHelperState
import java.util.*

@Composable
fun QFEHelperScreen(component: QFEHelper) {

    val state: QFEHelperState by component.state.subscribeAsState()
    println("recompose: $state")

    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    var fieldICAO by remember { mutableStateOf(state.airportICAO) }
    var fieldElevation by remember { mutableStateOf("") }
    var fieldQFE by remember { mutableStateOf(state.qfeMmHg.toString()) }
    var fieldHeightPlus by remember { mutableStateOf(state.heightPlusMeters) }

    LaunchedEffect(state.elevationMeters, state.qfeMmHg) {
        fieldElevation = state.elevationMeters.toString()
//        fieldQFE = state.qfeMmHg.toString()
    }

    state.error?.let {
        LaunchedEffect(key1 = scaffoldState.snackbarHostState) {
            scaffoldState.snackbarHostState.showSnackbar(
                message = it,
                actionLabel = "Close"
            )
        }
    }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = { Text("QFE Helper") },
                navigationIcon = {
                    IconButton(onClick = component.onBack) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back button")
                    }
                }
            )
        }
    ) {
        Column {
            OutlinedTextField(
                modifier = Modifier.padding(6.dp),
                label = { Text("Airport ICAO") },
                placeholder = { Text("enter 4 symbols, i.e. KJFK") },
                value = fieldICAO,
                onValueChange = { new -> fieldICAO = new },
                trailingIcon = {
                    IconButton(onClick = { component.onEvent(QFEEvent.SubmitICAO(fieldICAO.uppercase(Locale.getDefault()), scope)) }) {
                        Icon(imageVector = Icons.Default.Send, contentDescription = "submit")
                    }
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Characters)
            )

            AnimatedVisibility(
                visible = state.airportName != null,
                modifier = Modifier.padding(6.dp),
                enter = slideInVertically(),
                exit = slideOutVertically()
                ) {
                Text(
                    text = state.airportName ?: "",
                    fontSize = 12.sp,
                    color = Color.DarkGray,
                    fontStyle = FontStyle.Italic
                )
            }

            OutlinedTextField(
                modifier = Modifier.padding(6.dp),
                label = { Text("Elevation (meters)") },
                placeholder = { Text("airport elevation in meters") },
                value = fieldElevation,
                onValueChange = { new -> fieldElevation = new },
                trailingIcon = {
                    IconButton(onClick = {
                        fieldElevation.toIntOrNull()?.let {
                            component.onEvent(QFEEvent.SubmitElevationMeters(it))
                        }
                    }) {
                        Icon(imageVector = Icons.Default.Send, contentDescription = "submit")
                    }
                },
                isError = fieldElevation.toIntOrNull() == null,
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            AnimatedVisibility(
                visible = state.elevationMeters > 0,
                modifier = Modifier.padding(6.dp),
                enter = slideInVertically(),
                exit = slideOutVertically()
            ) {
                Text(
                    text = "${state.elevationFeet} feet",
                    fontSize = 12.sp,
                    color = Color.DarkGray,
                    fontStyle = FontStyle.Italic
                )
            }

            OutlinedTextField(
                modifier = Modifier.padding(6.dp),
                label = { Text("QFE (${state.qfeMilliBar} mBar)") },
                placeholder = { Text("airport QFE in mmHg") },
                value = fieldQFE,
                onValueChange = { new -> fieldQFE = new },
                trailingIcon = {
                    IconButton(onClick = {
                        fieldQFE.toIntOrNull()?.let {
                            component.onEvent(QFEEvent.SubmitQFEmmHg(it))
                        }
                    }) {
                        Icon(imageVector = Icons.Default.Send, contentDescription = "submit")
                    }
                },
                isError = fieldQFE.toIntOrNull() == null,
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            Text(
                modifier = Modifier.padding(6.dp),
                text = "QNH: ${state.qnh} hPa",
                fontSize = 12.sp,
                color = Color.DarkGray,
                fontStyle = FontStyle.Italic
            )
        }

    }
}