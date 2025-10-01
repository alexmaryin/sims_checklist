import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import ui.utils.SimColors

object Themes {
    val light = lightColorScheme(
        primary = SimColors.backgroundBrown,
        primaryContainer = SimColors.accentBrown,
        surface = SimColors.lightSurface,
        secondary = SimColors.textDark,
        secondaryContainer = SimColors.accentGreen,
        onPrimary = SimColors.textLight,
        onSecondary = SimColors.textDark,
        onSurface = SimColors.textBlack
    )

    val dark = darkColorScheme(
        primary = SimColors.backgroundLight,
        primaryContainer = SimColors.backgroundBrown,
        surface = SimColors.textDark,
        secondary = SimColors.backgroundGray,
        secondaryContainer = SimColors.accentGreen,
        onPrimary = SimColors.textBlack,
        onSecondary = SimColors.textBrown,
        onSurface = SimColors.textUltraLight
    )
}