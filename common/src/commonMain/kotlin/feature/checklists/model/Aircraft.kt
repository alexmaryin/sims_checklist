package feature.checklists.model

import feature.checklistDetails.model.Checklist
import kotlinx.serialization.Serializable
import feature.fuelcalculator.model.Performance

@Serializable
data class Aircraft(
    val id: Int,
    val name: String,
    val performance: Performance,
    val checklists: List<Checklist>,
    val photo: String = ""
)
