package decompose

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.reduce

class ItemComponent : Item {
    private val _item = MutableValue(Item.Model())
    override val item: Value<Item.Model> get() = _item

    override fun onClick() {
        _item.reduce { it.copy(checked = !it.checked) }
    }
}