package model

import kotlinx.serialization.Serializable

@Serializable
data class Checklist(
    val id: Int,
    val caption: String,
    val items: List<Item>
) {
    val isCompleted: Boolean get() = items.all { it.checked }
    fun clear() {
        items.forEach { it.toggle(false) }
    }
}
