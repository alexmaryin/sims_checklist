package ru.alexmaryin.simschecklist

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import commonUi.utils.SimColors

object Themes {
    val light = lightColorScheme(
        primary = SimColors.backgroundBrown,
        primaryContainer = SimColors.backgroundBrown,
        onPrimaryContainer = SimColors.textLight,
        secondary = SimColors.accentBrown,
        onSecondary = SimColors.textLight,
        secondaryContainer = SimColors.lightSurface,
        onSecondaryContainer = SimColors.accentGreen,
        tertiaryContainer = SimColors.accentBrown,
        onTertiaryContainer = SimColors.textLight,
        inversePrimary = SimColors.accentGreen,
        onPrimary = SimColors.textLight,
        onSurface = SimColors.textBlack,
        inverseOnSurface = SimColors.textBlack,
        surfaceVariant = SimColors.backgroundLight,
        surfaceDim = SimColors.lightSurface
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
        surfaceVariant = SimColors.textDark,
        surfaceDim = SimColors.textDark
    )
}
