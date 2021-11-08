package feature.metarscreen.ui

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import feature.metarscreen.MetarScanner
import feature.metarscreen.MetarUiEvent
import feature.metarscreen.WindViewState
import feature.metarscreen.model.ErrorType
import ui.Dialog
import ui.inputModifier
import ui.modifierForWindFace

@OptIn(ExperimentalAnimationApi::class, androidx.compose.ui.ExperimentalComposeUiApi::class)
@Composable
fun MetarScreen(component: MetarScanner) {

    val state: WindViewState by component.state.subscribeAsState()

    var userAngle by remember { mutableStateOf(state.data.metarAngle ?: state.data.userAngle) }
    var icaoInput by remember { mutableStateOf("") }
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    if (state.isError) {
        LaunchedEffect(key1 = scaffoldState.snackbarHostState) {
            scaffoldState.snackbarHostState.showSnackbar(
                message = when (state.error?.type) {
                    ErrorType.SERVER_ERROR -> state.error!!.message
                    ErrorType.METAR_PARSE_ERROR -> "Metar has no correct wind information"
                    else -> "Can\\'t fetch metar from server"
                },
                actionLabel = "Close"
            )
        }
    }

    if (state.showInfo) {
        Dialog(
            onDismissRequest = { component.onEvent(MetarUiEvent.DismissInfoDialog) },
            title = "Metar scan",
            text = """
        This feature shows wind direction selected manually or fetched from METAR service.
        This may help you to choice preferred runway at airport for landing or take-off 
        (as you know, you should landing or taking-off with headwind).
        To change direction manually move the slider to needed wind direction.
        To fetch METAR info enter an ICAO of station or airport and press Submit or Done button on keyboard.
        Someday feature will show you other METAR info in visual form.
            """.trimIndent()
        )
    }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = { Text("Metar scan") },
                navigationIcon = {
                    IconButton(onClick = component.onBack) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back button")
                    }
                },
                actions = {
                    IconButton(onClick = { component.onEvent(MetarUiEvent.ShowInfoDialog) }) {
                        Icon(imageVector = Icons.Default.Info, contentDescription = "Information")
                    }
                }
            )
        }
    ) {
        val scrollState = rememberScrollState()

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .verticalScroll(scrollState)
        ) {

            val keyboardController = LocalSoftwareKeyboardController.current

            fun submitICAO() {
                keyboardController?.hide()
                component.onEvent(MetarUiEvent.SubmitICAO(icaoInput.uppercase(), scope))
                icaoInput = ""
            }

            BoxWithConstraints(modifier = modifierForWindFace().align(Alignment.CenterHorizontally)) {
                WindSegment(this, component)
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Slider(
                    value = (state.data.metarAngle ?: userAngle).toFloat(),
                    onValueChange = { new -> userAngle = new.toInt() },
                    modifier = Modifier.weight(1f),
                    valueRange = 1f..360f,
                    steps = 360,
                    onValueChangeFinished = { component.onEvent(MetarUiEvent.SubmitAngle(userAngle)) }
                )
                Text("${state.data.metarAngle ?: userAngle}°", fontSize = 24.sp)
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    value = icaoInput,
                    onValueChange = { new -> icaoInput = new },
                    placeholder = { Text("enter ICAO") },
                    singleLine = true,
                    enabled = state.isLoading.not(),
                    keyboardActions = KeyboardActions(onDone = { submitICAO() }),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done,
                        capitalization = KeyboardCapitalization.Characters,
                        keyboardType = KeyboardType.Ascii
                    ),
                    modifier = inputModifier().weight(1f)
                )

                Button(
                    onClick = { submitICAO() },
                    enabled = state.isLoading.not()
                ) {
                    if (state.isLoading) {
                        CircularProgressIndicator()
                    } else {
                        Text("Submit")
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))

            AnimatedVisibility(
                visible = state.data.rawMetar.isNotBlank(),
                enter = fadeIn() + slideInVertically(),
                exit = fadeOut() + slideOutVertically()
            ) {
                Column {
                    Text(
                        text = state.data.airport,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colors.secondary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "METAR: ${state.data.rawMetar}",
                        fontSize = 14.sp,
                        color = MaterialTheme.colors.secondary
                    )
                }
            }
        }
    }
}

