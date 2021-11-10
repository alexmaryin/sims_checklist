import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.ui.graphics.Color

object Themes {
    val light = lightColors(
        primary = Color(0xff3e2723),
        primaryVariant = Color(0xff6a4f4b),
        surface = Color(0xffefebe9),
        secondary = Color(0xffd7ccc8),
        secondaryVariant = Color(0xff00c853),
        onPrimary = Color(0xfffff3e0),
        onSecondary = Color(0xff212121),
        onSurface = Color(0xff1b0000)
    )

    val dark = darkColors(
        primary = Color(0xff1b0000),
        primaryVariant = Color(0xff3e2723),
        secondary = Color(0xffa69b97),
        secondaryVariant = Color(0xff00c853),
        onPrimary = Color(0xfffff59d),
        onSecondary = Color(0xff4e342e),
        onSurface = Color(0xff1b0000)
    )
}