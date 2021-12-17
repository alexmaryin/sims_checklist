package feature.metarscreen.ui

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import feature.metarscreen.model.WindComponent

@Composable
fun RunwayWindInfo(runwayName: String, wind: WindComponent) {

    Text(
        text = buildString {
            append("For runway $runwayName ")
            append(if (wind.crossWind >= 1) "crosswind is ${wind.crossWind} kt\n" else "no crosswind reported\n")
            if (wind.headWind >= 1) append("Headwind - ${wind.headWind} Kt")
            if (wind.tailWind >= 1) append("Tailwind - ${wind.tailWind} Kt")
        },
        fontWeight = FontWeight.Light
    )
}