package feature.coldTemperature.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.dp
import commonUi.utils.SimColors
import services.coldTemperature.ApproachSegment

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddWaypointSheet(
    isVisible: Boolean,
    defaultAltitude: Int,
    onSubmit: (String, Int, ApproachSegment) -> Unit,
    onDismissRequest: () -> Unit,
) {
    if (isVisible) {
        val waypointName = rememberTextFieldState("")
        val waypointElevation = rememberTextFieldState(defaultAltitude.toString())
        var selectedSegment by remember { mutableStateOf(ApproachSegment.INTERMEDIATE) }

        ModalBottomSheet(onDismissRequest = onDismissRequest) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text = "Add waypoint",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(Modifier.height(12.dp))

                OutlinedTextField(
                    state = waypointName,
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Waypoint name") },
                    lineLimits = TextFieldLineLimits.SingleLine,
                    inputTransformation = InputTransformation.allCaps(Locale.current),
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Characters,
                    ),
                )
                Spacer(Modifier.height(12.dp))

                OutlinedTextField(
                    state = waypointElevation,
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Altitude, ft") },
                    lineLimits = TextFieldLineLimits.SingleLine,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                    )
                )
                Spacer(Modifier.height(12.dp))

                Text(
                    text = "Approach Segment",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Medium
                )
                Spacer(Modifier.height(8.dp))

                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    SingleChoiceSegmentedButtonRow(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 6.dp),
                    ) {
                        ApproachSegment.entries.forEachIndexed { index, segment ->
                            SegmentedButton(
                                shape = SegmentedButtonDefaults.itemShape(index = index, count = ApproachSegment.entries.size),
                                onClick = { selectedSegment = segment },
                                selected = selectedSegment == segment,
                                icon = {},
                                colors = SegmentedButtonDefaults.colors(
                                    activeContainerColor = MaterialTheme.colorScheme.primaryContainer,
                                    activeContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                                    activeBorderColor = MaterialTheme.colorScheme.outline,
                                    inactiveContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                                    inactiveContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                    inactiveBorderColor = MaterialTheme.colorScheme.outline,
                                )
                            ) {
                                Text(
                                    text = segment.displayName,
                                    style = MaterialTheme.typography.labelMedium
                                )
                            }
                        }
                    }
                }

                Spacer(Modifier.height(8.dp))

                Text(
                    text = "Default value follows the previous point altitude or airport elevation if this is the first point.",
                    style = MaterialTheme.typography.bodySmall
                )
                Spacer(Modifier.height(16.dp))

                Button(
                    onClick = {
                        val altitude = waypointElevation.text.toString().toInt()
                        val name = waypointName.text.toString()
                        onSubmit(name, altitude, selectedSegment)
                        waypointName.clearText()
                        onDismissRequest()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = waypointName.text.isNotBlank(),
                    colors = SimColors.buttonColors()
                ) {
                    Text("Add waypoint")
                }
                Spacer(Modifier.height(24.dp))
            }
        }
    }
}
