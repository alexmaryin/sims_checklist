package feature.metarscreen.ui.airportSegment

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import feature.metarscreen.model.WindUi
import org.jetbrains.compose.resources.painterResource
import sims_checklist.shared.generated.resources.Res
import sims_checklist.shared.generated.resources.arrow_up

@Composable
fun RunwayTailwindBubble(name: String, windUi: WindUi) {

    Box(
        modifier = Modifier
            .border(4.dp, MaterialTheme.colorScheme.error, RoundedCornerShape(20.dp))
            .size(100.dp)
    ) {

        val cornersForLabel = if(windUi is WindUi.LeftCrossTailWindUi) Bubble.cornersForLeftTail else Bubble.cornersForRightTail
        val alignForLabel = if(windUi is WindUi.LeftCrossTailWindUi) Alignment.BottomStart else Alignment.BottomEnd
        val alignForTail = if(windUi is WindUi.LeftCrossTailWindUi) Alignment.BottomEnd else Alignment.BottomStart
        val alignForPlane = if(windUi is WindUi.LeftCrossTailWindUi) Alignment.TopEnd else Alignment.TopStart

        Box(
            modifier = Modifier
                .border(4.dp, MaterialTheme.colorScheme.error, cornersForLabel)
                .align(alignForLabel)
                .size(50.dp),
        ) {
            Bubble.RunwayLabel(name, Modifier.align(Alignment.Center))
        }

        Column(
            modifier = Modifier.align(alignForTail).padding(10.dp),
        ) {
            Image(
                painter = painterResource(Res.drawable.arrow_up),
                contentDescription = "Tailwind speed",
                modifier = Modifier
                    .size(20.dp).align(Alignment.CenterHorizontally),
                colorFilter = ColorFilter.tint(Color.DarkGray)
            )
            Text(text = "${windUi.straight} Kt", textAlign = TextAlign.Center)
        }

        Bubble.AirplaneIcon(Modifier.align(alignForPlane))

        Row(
            modifier = Modifier
                .align(if(windUi is WindUi.LeftCrossTailWindUi) Alignment.TopStart else Alignment.TopEnd)
                .padding(10.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            if(windUi is WindUi.LeftCrossTailWindUi) {
                Bubble.CrossWindLeftText(windUi.cross)
            } else {
                Bubble.CrossWindRightText(windUi.cross)
            }
        }
    }

}