package decompose

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value

class GroupComponent : Group {
    private val _group = MutableValue(Group.Model())
    override val group: Value<Group.Model> get() = _group
}