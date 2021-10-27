package model

data class Checklist(
    val id: Int,
    val caption: String,
    val items: List<Item>
) {
    val isCompleted: Boolean get() = items.all { it.checked }
}
