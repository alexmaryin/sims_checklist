package model

data class Item(
    val caption: String,
    val details: String = "",
    var checked: Boolean = false
) {
    fun toggle() { if(caption != "LINE") checked = !checked }
}
