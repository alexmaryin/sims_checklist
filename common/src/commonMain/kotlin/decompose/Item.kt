package decompose

import com.arkivanov.decompose.value.Value

interface Item {
    val item: Value<Model>
    fun onClick()

    data class Model(
        val caption: String = "Test item",
        val details: String = "details for test item",
        var checked: Boolean = false
    )
}