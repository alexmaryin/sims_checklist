package ui.utils

import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun largeWithShadow(multi: Int  = 1) = LocalTextStyle.current.copy(
    fontSize = 20.sp,
    fontWeight = FontWeight.Normal,
    shadow = Shadow(color = Color.DarkGray, offset = Offset(2f * multi, 2f * multi), blurRadius = 2f * multi),
    color = MaterialTheme.colorScheme.onSurface
)
