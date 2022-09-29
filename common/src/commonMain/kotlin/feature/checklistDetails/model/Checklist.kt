package feature.checklistDetails.model

import kotlinx.serialization.Serializable

const val CHECKLIST_LINE = "LINE"

@Serializable
data class Checklist(
    val id: Int,
    val caption: String,
    val items: List<Item>
) {
    val isCompleted: Boolean get() =
        items.filterNot { it.caption == CHECKLIST_LINE }.all { it.checked }
}
