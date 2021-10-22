package view

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import decompose.Page

@Composable
fun PageUi(component: Page, modifier: Modifier = Modifier) {
    ChecklistUi(component.checklist, modifier)
}