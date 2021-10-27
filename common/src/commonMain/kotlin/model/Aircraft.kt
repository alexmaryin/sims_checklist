package model

data class Aircraft(
    val id: Int,
    val name: String,
    val performance: Performance,
    val checklists: List<Checklist>,
) {
    fun getChecklist(id: Int): Checklist? = checklists.firstOrNull { it.id == id }
}
