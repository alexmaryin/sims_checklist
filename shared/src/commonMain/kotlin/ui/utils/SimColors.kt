package ui.utils

import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@OptIn(ExperimentalMaterial3Api::class)
object SimColors {
    // Light theme
    val accentGreen = Color(0xff00c853)
    val backgroundBrown = Color(0xff3e2723)
    val accentBrown = Color(0xff6a4f4b)
    val lightSurface = Color(0xffefebe9)
    val textDark = Color(0xff212121)
    val textLight = Color(0xfffff3e0)
    val textBlack = Color(0xff1b0000)
    // Dark theme
    val backgroundLight = Color(0xffd7ccc8)
    val backgroundGray = Color(0xffa69b97)
    val textBrown = Color(0xff4e342e)
    val textUltraLight = Color(0xfffbe9e7)


    @Composable
    fun topBarColors() = TopAppBarDefaults.topAppBarColors(
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        titleContentColor = MaterialTheme.colorScheme.onPrimary,
        navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
        actionIconContentColor = MaterialTheme.colorScheme.onPrimary
    )

    @Composable
    fun buttonColors() = ButtonDefaults.buttonColors(
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.onPrimary
    )
}
