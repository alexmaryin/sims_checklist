package commonUi.utils

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.RichTooltip
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle

private val hintString = buildAnnotatedString {
    append(
        "To serve the main purpose of the provided airport information — " +
                "visualizing headwind and crosswind components—the runway headings are given in "
    )
    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) { append("true courses") }
    append(", not magnetic.\n\nSince the wind information in METAR reports is always referenced to ")
    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) { append("true north") }
    append(
        ", we use the true course of each runway to accurately display crosswind and headwind " +
                "components relative to the aircraft’s track."
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RunwayTooltip(body: @Composable () -> Unit) = TooltipBox(
    positionProvider = TooltipDefaults.rememberRichTooltipPositionProvider(),
    tooltip = {
        RichTooltip(title = { Text("NOTICE") }) { Text(hintString) }
    },
    state = rememberTooltipState(isPersistent = true)
) { body() }