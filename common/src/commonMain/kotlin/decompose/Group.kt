package decompose

import com.arkivanov.decompose.value.Value

interface Group {
    val group: Value<Model>

    data class Model(
        val items: List<ItemComponent> = List(10) { index -> ItemComponent(Item.Model(caption = "Item no. $index")) },
        val caption: String = "Test group"
    )
}