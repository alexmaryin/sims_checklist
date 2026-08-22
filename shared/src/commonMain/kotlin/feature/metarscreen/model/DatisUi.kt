package feature.metarscreen.model

import kotlinx.serialization.Serializable
import services.atisService.model.DatisEntry

@Serializable
data class DatisUi(
    val entries: List<DatisEntry> = emptyList(),
    val error: String? = null
)
