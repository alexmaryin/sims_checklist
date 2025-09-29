package feature.checklistDetails.model

import kotlinx.serialization.Serializable

@Serializable
data class Item(
    val caption: String,
    val action: String = "",
    val details: String = "",
    val checked: Boolean = false
)
