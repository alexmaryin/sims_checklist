package decompose

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.reduce

class ItemComponent(data: Item.Model = Item.Model()) : Item {
    private val _item = MutableValue(data)
    override val item: Value<Item.Model> get() = _item

    override fun onClick() {
        _item.reduce { it.copy(checked = !it.checked) }
    }
}