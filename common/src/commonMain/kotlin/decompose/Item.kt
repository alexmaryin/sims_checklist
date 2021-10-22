package decompose

import com.arkivanov.decompose.value.Value

interface Item {
    val item: Value<Model>
    fun onClick()

    data class Model(
        val caption: String = "",
        val details: String = "",
        var checked: Boolean = false
    )
}