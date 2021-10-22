package decompose

import com.arkivanov.decompose.value.Value

interface Checklist {
    val checklist: Value<Model>

    data class Model(
        val groups: List<GroupComponent> = emptyList(),
        val caption: String = ""
    )
}