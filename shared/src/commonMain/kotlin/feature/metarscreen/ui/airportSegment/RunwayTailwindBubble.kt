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
import feature.metarscreen.model.Wind
import ui.loadXmlPicture

@Composable
fun RunwayTailwindBubble(name: String, wind: Wind) {

    Box(
        modifier = Modifier
            .border(4.dp, MaterialTheme.colorScheme.error, RoundedCornerShape(20.dp))
            .size(100.dp)
    ) {

        val cornersForLabel = if(wind is Wind.LeftCrossTailWind) Bubble.cornersForLeftTail else Bubble.cornersForRightTail
        val alignForLabel = if(wind is Wind.LeftCrossTailWind) Alignment.BottomStart else Alignment.BottomEnd
        val alignForTail = if(wind is Wind.LeftCrossTailWind) Alignment.BottomEnd else Alignment.BottomStart
        val alignForPlane = if(wind is Wind.LeftCrossTailWind) Alignment.TopEnd else Alignment.TopStart

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
                imageVector = loadXmlPicture("arrow_up"),
                contentDescription = "Tailwind speed",
                modifier = Modifier
                    .size(20.dp).align(Alignment.CenterHorizontally),
                colorFilter = ColorFilter.tint(Color.DarkGray)
            )
            Text(text = "${wind.straight} Kt", textAlign = TextAlign.Center)
        }

        Bubble.AirplaneIcon(Modifier.align(alignForPlane))

        Row(
            modifier = Modifier
                .align(if(wind is Wind.LeftCrossTailWind) Alignment.TopStart else Alignment.TopEnd)
                .padding(10.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            if(wind is Wind.LeftCrossTailWind) {
                Bubble.CrossWindLeftText(wind.cross)
            } else {
                Bubble.CrossWindRightText(wind.cross)
            }
        }
    }

}