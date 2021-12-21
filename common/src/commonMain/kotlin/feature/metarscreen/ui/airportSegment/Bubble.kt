package feature.metarscreen.ui.airportSegment

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ui.loadXmlPicture

object Bubble {

    val cornersForLeftHead = RoundedCornerShape(
        topStart = 20.dp,
        topEnd = 0.dp,
        bottomStart = 0.dp,
        bottomEnd = 10.dp
    )

    val cornersForRightHead = RoundedCornerShape(
        topStart = 0.dp,
        topEnd = 20.dp,
        bottomStart = 10.dp,
        bottomEnd = 0.dp
    )

    val cornersForLeftTail = RoundedCornerShape(
        topStart = 0.dp,
        topEnd = 10.dp,
        bottomStart = 20.dp,
        bottomEnd = 0.dp
    )

    val cornersForRightTail = RoundedCornerShape(
        topStart = 10.dp,
        topEnd = 0.dp,
        bottomStart = 0.dp,
        bottomEnd = 20.dp
    )

    @Composable
    fun CrossWindLeftText(value: Int) {
        Text(text = "$value Kt")
        Image(
            imageVector = loadXmlPicture("arrow_right"),
            contentDescription = "Left crosswind",
            modifier = Modifier
                .size(20.dp),
            colorFilter = ColorFilter.tint(Color.DarkGray)
        )
    }

    @Composable
    fun CrossWindRightText(value: Int) {
        Image(
            imageVector = loadXmlPicture("arrow_left"),
            contentDescription = "Right crosswind",
            modifier = Modifier
                .size(20.dp),
            colorFilter = ColorFilter.tint(Color.DarkGray)
        )
        Text(text = "$value Kt")
    }

    @Composable
    fun AirplaneIcon(modifier: Modifier) = Image(
        imageVector = loadXmlPicture("ic_airplane"),
        contentDescription = "Airplane",
        modifier = modifier
            .padding(10.dp)
            .size(30.dp),
        colorFilter = ColorFilter.tint(Color.DarkGray)
    )

    @Composable
    fun RunwayLabel(name: String, modifier: Modifier) = Text(
        text = name,
        textAlign = TextAlign.Center,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
        modifier = modifier
    )
}