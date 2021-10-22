package decompose

import com.arkivanov.decompose.value.Value

interface Group {
    val group: Value<Model>

    data class Model(
        val items: List<ItemComponent> = emptyList(),
        val caption: String = ""
    )
}