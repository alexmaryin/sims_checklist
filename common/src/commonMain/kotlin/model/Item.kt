package model

import kotlinx.serialization.Serializable

@Serializable
data class Item(
    val caption: String,
    val action: String = "",
    val details: String = "",
    var checked: Boolean = false
) {
    fun toggle(value: Boolean? = null) { if(caption != "LINE") checked = value ?: !checked }
}
