import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import ui.utils.SimColors

object Themes {
    val light = lightColors(
        primary = SimColors.backgroundBrown,
        primaryVariant = SimColors.accentBrown,
        surface = SimColors.lightSurface,
        secondary = SimColors.textDark,
        secondaryVariant = SimColors.accentGreen,
        onPrimary = SimColors.textLight,
        onSecondary = SimColors.textDark,
        onSurface = SimColors.textBlack
    )

    val dark = darkColors(
        primary = SimColors.backgroundLight,
        primaryVariant = SimColors.backgroundBrown,
        surface = SimColors.textDark,
        secondary = SimColors.backgroundGray,
        secondaryVariant = SimColors.accentGreen,
        onPrimary = SimColors.textBlack,
        onSecondary = SimColors.textBrown,
        onSurface = SimColors.textUltraLight
    )
}