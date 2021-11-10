package ru.alexmaryin.simschecklist

import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.ui.graphics.Color

object Themes {
    val light = lightColors(
        primary = Color(0xff3e2723),
        primaryVariant = Color(0xff6a4f4b),
        secondary = Color(0xff00c853),
        secondaryVariant = Color(0xff5efc82),
        onPrimary = Color(0xfffff59d),
        onSecondary = Color(0xff4e342e)
    )

    val dark = darkColors(
        primary = Color(0xff1b0000),
        primaryVariant = Color(0xff3e2723),
        secondary = Color(0xff009624),
        secondaryVariant = Color(0xff00c853),
        onPrimary = Color(0xfffff59d),
        onSecondary = Color(0xff4e342e)
    )
}


// https://material.io/resources/color/#!/?view.left=0&view.right=0&primary.color=3E2723&secondary.color=689F38&primary.text.color=F0F4C3&secondary.text.color=FFF3E0