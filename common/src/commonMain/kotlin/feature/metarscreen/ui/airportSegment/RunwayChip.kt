package feature.metarscreen.ui.airportSegment

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.boundsInParent
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize

@Composable
fun RunwayChip(text: String, isDefault: Boolean = false, onClick: (Rect) -> Unit) {

    var chipRect by remember { mutableStateOf(Rect.Zero) }

    Box(
        modifier = Modifier
            .background(MaterialTheme.colors.background, RoundedCornerShape(30.dp))
            .border(4.dp, color = Color(0xFF888888), RoundedCornerShape(30.dp))
            .onGloballyPositioned {
                chipRect = it.boundsInParent()
            }
    ) {

        if (isDefault) onClick(chipRect)

        Text(
            modifier = Modifier
                .padding(12.dp)
                .clickable { onClick(chipRect) },
            text = text,
            color = Color(0xFF2C2C2C),
            fontSize = 24.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}
