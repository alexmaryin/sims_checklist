package model

data class Aircraft(
    val id: Int,
    val name: String,
    val performance: Performance,
    val checklists: List<Checklist>,
)
