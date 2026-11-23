package commonUi.utils

import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import feature.checklistDetails.model.ChecklistCategory

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

    // Checklist category colors (same in light and dark themes, dark text on top)
    val procedureBlue = Color(0xff81d4fa)
    val nonNormalRed = Color(0xffe57373)
    val nonNormalRedSoft = Color(0xffef9a9a)
    val nonNormalRedVivid = Color(0xffff8a80)

    /**
     * Card colors for the checklists list: container to content (text/icon tint).
     * NORMAL keeps the legacy behavior (default surface, green when completed);
     * PROCEDURE and NON-NORMAL keep their category color regardless of completion.
     * Swap the active NON-NORMAL constant here to test red candidates.
     */
    @Composable
    fun checklistCardColors(category: ChecklistCategory, isCompleted: Boolean): Pair<Color, Color> =
        when (category) {
            ChecklistCategory.NORMAL ->
                if (isCompleted) MaterialTheme.colorScheme.inversePrimary to MaterialTheme.colorScheme.inverseOnSurface
                else Color.Unspecified to MaterialTheme.colorScheme.onSurface
            ChecklistCategory.PROCEDURE -> procedureBlue to textBlack
            ChecklistCategory.NON_NORMAL -> nonNormalRed to textBlack
        }


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
