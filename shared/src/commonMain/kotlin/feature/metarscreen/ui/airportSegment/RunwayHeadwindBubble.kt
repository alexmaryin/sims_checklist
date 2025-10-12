package feature.metarscreen.ui.airportSegment

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import feature.metarscreen.model.WindUi
import ui.loadXmlPicture

@Composable
fun RunwayHeadwindBubble(name: String, windUi: WindUi) {

    Box(
        modifier = Modifier
            .border(4.dp, Color(0xff00c853), RoundedCornerShape(20.dp))
            .size(100.dp)
    ) {

        val cornersForLabel = if(windUi is WindUi.LeftCrossHeadWindUi) Bubble.cornersForLeftHead else Bubble.cornersForRightHead
        val alignForLabel = if(windUi is WindUi.LeftCrossHeadWindUi) Alignment.TopStart else Alignment.TopEnd
        val alignForHead = if(windUi is WindUi.LeftCrossHeadWindUi) Alignment.TopEnd else Alignment.TopStart
        val alignForPlane = if(windUi is WindUi.LeftCrossHeadWindUi) Alignment.BottomEnd else Alignment.BottomStart

        Box(
            modifier = Modifier
                .border(4.dp, Color(0xff00c853), cornersForLabel)
                .align(alignForLabel)
                .size(50.dp),
        ) {
            Bubble.RunwayLabel(name, Modifier.align(Alignment.Center))
        }

        Column(
            modifier = Modifier.align(alignForHead).padding(10.dp),
        ) {
            Text(text = "${windUi.straight} Kt", textAlign = TextAlign.Center)
            Image(
                imageVector = loadXmlPicture("arrow_down"),
                contentDescription = "Headwind speed",
                modifier = Modifier
                    .size(20.dp).align(Alignment.CenterHorizontally),
                colorFilter = ColorFilter.tint(Color.DarkGray)
            )
        }

        Bubble.AirplaneIcon(Modifier.align(alignForPlane))

        Row(
            modifier = Modifier
                .align(if(windUi is WindUi.LeftCrossHeadWindUi) Alignment.BottomStart else Alignment.BottomEnd)
                .padding(10.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            if(windUi is WindUi.LeftCrossHeadWindUi) {
                Bubble.CrossWindLeftText(windUi.cross)
            } else {
                Bubble.CrossWindRightText(windUi.cross)
            }
        }
    }
}