import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color
import ui.utils.SimColors

object Themes {
    val light = lightColorScheme(
        primary = SimColors.backgroundBrown,
        primaryContainer = SimColors.backgroundBrown,
        secondary = SimColors.accentBrown,
        onSecondary = SimColors.textLight,
        onSecondaryContainer = SimColors.accentGreen,
        inversePrimary = SimColors.accentGreen,
        onPrimary = SimColors.textLight,
        onSurface = SimColors.textBlack,
        inverseOnSurface = SimColors.textBlack,
        surfaceVariant = SimColors.backgroundLight
    )

    val dark = darkColorScheme(
        primary = SimColors.backgroundLight,
        primaryContainer = SimColors.textBlack,
        secondary = SimColors.backgroundGray,
        onSecondary = SimColors.textBlack,
        onSecondaryContainer = SimColors.textBrown,
        inversePrimary = SimColors.accentGreen,
        onPrimary = SimColors.textLight,
        onSurface = SimColors.textUltraLight,
        inverseOnSurface = SimColors.textBlack,
        surfaceVariant = SimColors.textDark
    )
}