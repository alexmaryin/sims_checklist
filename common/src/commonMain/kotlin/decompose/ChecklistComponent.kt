package decompose

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value

class ChecklistComponent : Checklist {
    private val _checklist = MutableValue(Checklist.Model())
    override val checklist: Value<Checklist.Model> get() = _checklist
}