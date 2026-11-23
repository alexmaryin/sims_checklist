package feature.checklistDetails.model

import kotlinx.serialization.Serializable

const val CHECKLIST_LINE = "LINE"

@Serializable
enum class ChecklistCategory { NORMAL, NON_NORMAL, PROCEDURE }

@Serializable
data class Checklist(
    val id: Int,
    val caption: String,
    val items: List<Item>,
    val isCompleted: Boolean = false,
    val category: ChecklistCategory = ChecklistCategory.NORMAL
)