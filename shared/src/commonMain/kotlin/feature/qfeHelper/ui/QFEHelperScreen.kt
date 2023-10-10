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

    val state: State<QFEHelperState> = component.state.subscribeAsState()

    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    var fieldICAO by remember { mutableStateOf(state.value.airportICAO) }
    var fieldElevation by remember { mutableStateOf(state.value.elevationMeters) }
    var fieldQFE by remember { mutableStateOf(state.value.qfeMmHg) }
    var fieldHeightPlus by remember { mutableStateOf(state.value.heightPlusMeters) }

    state.value.error?.let {
        LaunchedEffect(scaffoldState.snackbarHostState) {
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
                    IconButton(onClick = {
                        component.onEvent(
                            QFEEvent.SubmitICAO(
                                fieldICAO.uppercase(Locale.getDefault()),
                                scope
                            )
                        )
                    }) {
                        Icon(imageVector = Icons.Default.Send, contentDescription = "submit")
                    }
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Characters)
            )

            AnimatedVisibility(
                visible = state.value.airportName != null,
                modifier = Modifier.padding(6.dp),
                enter = slideInVertically(),
                exit = slideOutVertically()
            ) {
                Text(
                    text = state.value.airportName ?: "",
                    fontSize = 12.sp,
                    color = Color.DarkGray,
                    fontStyle = FontStyle.Italic
                )
            }

            OutlinedTextField(
                modifier = Modifier.padding(6.dp),
                label = { Text("Elevation (meters)") },
                placeholder = { Text("airport elevation in meters") },
                value = "$fieldElevation",
                onValueChange = { new ->
                    new.toIntOrNull()?.let { fieldElevation = it }
                },
                trailingIcon = {
                    IconButton(onClick = {
                        component.onEvent(QFEEvent.SubmitElevationMeters(fieldElevation))
                    }) {
                        Icon(imageVector = Icons.Default.Send, contentDescription = "submit")
                    }
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            AnimatedVisibility(
                visible = state.value.elevationMeters > 0,
                modifier = Modifier.padding(6.dp),
                enter = slideInVertically(),
                exit = slideOutVertically()
            ) {
                Text(
                    text = "${state.value.elevationFeet} feet",
                    fontSize = 12.sp,
                    color = Color.DarkGray,
                    fontStyle = FontStyle.Italic
                )
            }

            OutlinedTextField(
                modifier = Modifier.padding(6.dp),
                label = { Text("QFE (${state.value.qfeMilliBar} mBar)") },
                placeholder = { Text("airport QFE in mmHg") },
                value = "$fieldQFE",
                onValueChange = { new ->
                    new.toIntOrNull()?.let { fieldQFE = it }
                },
                trailingIcon = {
                    IconButton(onClick = {
                        component.onEvent(QFEEvent.SubmitQFEmmHg(fieldQFE))
                    }) {
                        Icon(imageVector = Icons.Default.Send, contentDescription = "submit")
                    }
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            Text(
                modifier = Modifier.padding(6.dp),
                text = "QNH: ${state.value.qnh} hPa",
                fontSize = 12.sp,
                color = Color.DarkGray,
                fontStyle = FontStyle.Italic
            )
        }
    }
}